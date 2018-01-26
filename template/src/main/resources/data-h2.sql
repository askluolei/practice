insert into _role(id, name, introduce, created_by, created_date, last_modified_by, last_modified_date) VALUES
(1, 'ROLE_ADMIN', '管理员', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
(2, 'ROLE_USER', '用户', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into _access_permission(id, role_name, protected_resource, hatch_permission, created_by, created_date, last_modified_by, last_modified_date) VALUES
    (1, 'ROLE_USER', 'TEST', 'READ', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (2, 'ROLE_ADMIN', 'TEST', 'READ', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (3, 'ROLE_ADMIN', 'TEST', 'READALL', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (4, 'ROLE_ADMIN', 'TEST', 'DELETE', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (5, 'ROLE_ADMIN', 'TEST', 'CREATE', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (6, 'ROLE_ADMIN', 'TEST', 'UPDATE', 'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into _user(id, login, password_hash, first_name, last_name, email, activated, lang_key,image_url, activation_key, reset_key, reset_date,
                  created_by, created_date, last_modified_by, last_modified_date) VALUES
    (1, 'system', '$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG', 'system', 'system', 'system@localhost', true, 'zh-cn','', '', '', current_timestamp(),
     'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (2, 'anonymoususer', '$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO', 'anonymoususer', 'anonymoususer', 'anonymoususer@localhost', true, 'zh-cn','', '', '', current_timestamp(),
     'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (3, 'admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', 'admin', 'admin', 'admin@localhost', true, 'zh-cn','', '', '', current_timestamp(),
     'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP()),
    (4, 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K', 'user', 'user', 'user@localhost', true, 'zh-cn','', '', '', current_timestamp(),
     'system', CURRENT_TIMESTAMP(), 'system', CURRENT_TIMESTAMP());

insert into _user_roles(user_id, role_id) VALUES
    (1, 1),
    (1, 2),
    (3, 1),
    (3, 2),
    (4, 2);
