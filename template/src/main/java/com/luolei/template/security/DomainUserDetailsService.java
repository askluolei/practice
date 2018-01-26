package com.luolei.template.security;

import com.luolei.template.domain.AccessPermission;
import com.luolei.template.domain.Role;
import com.luolei.template.domain.User;
import com.luolei.template.repository.AccessPermissionRepository;
import com.luolei.template.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 罗雷
 * @date 2018/1/3 0003
 * @time 15:34
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final AccessPermissionRepository accessPermissionRepository;

    public DomainUserDetailsService(UserRepository userRepository, AccessPermissionRepository accessPermissionRepository) {
        this.userRepository = userRepository;
        this.accessPermissionRepository = accessPermissionRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }

            List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            /**
             * 这里将扩展跟spring-security 结合
             */
            List<GrantedAuthority> accessPermissions = new ArrayList<>();
            for (Role role : user.getRoles()) {
                if (role.getName().startsWith("ROLE_")) {
                    List<AccessPermission> permissionList = accessPermissionRepository.findByRoleName(role.getName());
                    if (!permissionList.isEmpty()) {
                        permissionList.forEach(accessPermission -> {
                            accessPermissions.add(new SimpleGrantedAuthority(accessPermission.getProtectedResource() + ":" + accessPermission.getHatchPermission().name()));
                        });
                    }
                }
            }
            grantedAuthorities.addAll(accessPermissions);
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                    user.getPassword(),
                    grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                "database"));
    }
}
