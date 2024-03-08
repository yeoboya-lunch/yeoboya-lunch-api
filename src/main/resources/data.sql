INSERT INTO role (role_id, role_name, description)
VALUES
    (1, 'ROLE_ADMIN', '어드민'),
    (2, 'ROLE_MANAGER', '매니저'),
    (3, 'ROLE_USER', '유저'),
    (4, 'ROLE_GUEST', '게스트'),
    (5, 'ROLE_BLOCK', '차단');

INSERT INTO role_hierarchy (role_id, role, parent_role)
VALUES
    (1, 'ROLE_ADMIN', NULL),
    (2, 'ROLE_MANAGER', 'ROLE_ADMIN'),
    (3, 'ROLE_USER', 'ROLE_MANAGER'),
    (4, 'ROLE_GUEST', 'ROLE_USER'),
    (5, 'ROLE_BLOCK', 'ROLE_GUEST');

INSERT INTO access_ip (id, ip_address) VALUES (1, '127.0.0.1')
