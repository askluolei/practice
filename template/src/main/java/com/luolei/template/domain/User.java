package com.luolei.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luolei.template.config.Constants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:19
 */
@Entity
@Table(name = "_user")
@Getter
@Setter
@ToString(exclude = {"authorities"})
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 100)
    @Column(length = 100, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 6)
    @Column(name = "lang_key", length = 6)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;
        return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
