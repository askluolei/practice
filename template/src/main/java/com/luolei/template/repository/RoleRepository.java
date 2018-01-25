package com.luolei.template.repository;

import com.luolei.template.domain.Role;

/**
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:46
 */
public interface RoleRepository extends BaseRepository<Role, String> {

    Role findByName(String name);
}
