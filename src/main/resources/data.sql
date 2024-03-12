-- Roles and their hierarchy
INSERT INTO role_hierarchy(CHILD_NAME, PARENT_NAME) VALUES
                                                        ('ROLE_ADMIN', null),
                                                        ('ROLE_MANAGER', 'ROLE_ADMIN'),
                                                        ('ROLE_USER', 'ROLE_MANAGER'),
                                                        ('ROLE_GUEST', 'ROLE_USER'),
                                                        ('ROLE_BLOCK', 'ROLE_GUEST');

-- Define all roles
INSERT INTO role(ROLES_ID, ROLE, ROLE_DESC) VALUES
                                                (1, 'ROLE_ADMIN', '어드민'),
                                                (2, 'ROLE_MANAGER', '매니저'),
                                                (3, 'ROLE_USER', '유저'),
                                                (4, 'ROLE_GUEST', '게스트'),
                                                (5, 'ROLE_BLOCK', '차단');

-- Define all resources
INSERT INTO resource(RESOURCES_ID, HTTP_METHOD, ORDER_NUM, RESOURCE_NAME, RESOURCE_TYPE) VALUES
                                                                                             (1, null, 1, '/authority/**', 'url'),
                                                                                             (2, null, 2, '/member/**', 'url'),
                                                                                             (3, null, 3, '/user/**', 'url'),
                                                                                             (4, null, 4, '/shop/**', 'url'),
                                                                                             (5, null, 5, '/item/**', 'url'),
                                                                                             (6, null, 6, '/order/**', 'url'),
                                                                                             (7, null, 7, '/board/**', 'url');

-- Define resource access per role
INSERT INTO role_resources(RESOURCE_ID, ROLE_ID) VALUES
                                                     (1, 1), -- Admin has access to authority
                                                     (2, 2), -- Manager has access to member
                                                     (4, 3), -- User has access to shop
                                                     (5, 3), -- User has access to item
                                                     (6, 3), -- User has access to order
                                                     (7, 3); -- User has access to board

-- Define ip access
INSERT INTO lunch.access_ip (ip_id, ip_address, block) VALUES
                                                           (0, '0:0:0:0:0:0:0:1', false),
                                                           (1, '127.0.0.1', false)
