package com.luolei.template.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 第三方登录用户信息
 *
 * @author 罗雷
 * @date 2018/1/2 0002
 * @time 17:35
 */
@Entity
@Table(name = "_social_user_connection")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SocialUserConnection extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "user_id", length = 255, nullable = false)
    private String userId;

    @NotNull
    @Column(name = "provider_id", length = 255, nullable = false)
    private String providerId;

    @NotNull
    @Column(name = "provider_user_id", length = 255, nullable = false)
    private String providerUserId;

    @NotNull
    @Column(nullable = false)
    private Long rank;

    @Column(name = "display_name", length = 255)
    private String displayName;

    @Column(name = "profile_url", length = 255)
    private String profileURL;

    @Column(name = "image_url", length = 255)
    private String imageURL;

    @NotNull
    @Column(name = "access_token", length = 255, nullable = false)
    private String accessToken;

    @Column(length = 255)
    private String secret;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "expire_time")
    private Long expireTime;

    public SocialUserConnection() {}
    public SocialUserConnection(String userId,
                                String providerId,
                                String providerUserId,
                                Long rank,
                                String displayName,
                                String profileURL,
                                String imageURL,
                                String accessToken,
                                String secret,
                                String refreshToken,
                                Long expireTime) {
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.rank = rank;
        this.displayName = displayName;
        this.profileURL = profileURL;
        this.imageURL = imageURL;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SocialUserConnection user = (SocialUserConnection) o;

        if (!getId().equals(user.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
