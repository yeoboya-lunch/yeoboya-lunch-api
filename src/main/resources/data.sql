INSERT INTO lunch.role (roles_id, role, role_desc) VALUES (1, 'ROLE_ADMIN', "어드민");
INSERT INTO lunch.role (roles_id, role, role_desc) VALUES (2, 'ROLE_MANGER', "매니저");
INSERT INTO lunch.role (roles_id, role, role_desc) VALUES (3, 'ROLE_USER', "유저");
INSERT INTO lunch.role (roles_id, role, role_desc) VALUES (4, 'ROLE_BLOCK', "차단");

INSERT INTO lunch.role_hierarchy (rolehierarchy_id, child_name, parent_name) VALUES (1, 'ROLE_ADMIN', null);
INSERT INTO lunch.role_hierarchy (rolehierarchy_id, child_name, parent_name) VALUES (2, 'ROLE_MANAGER', 'ROLE_ADMIN');
INSERT INTO lunch.role_hierarchy (rolehierarchy_id, child_name, parent_name) VALUES (3, 'ROLE_USER', 'ROLE_MANAGER');

INSERT INTO access_ip (id, ip_address) VALUES (1, '127.0.0.1')
