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
                                                                                             (2, null, 2, '/resource/**', 'url'),
                                                                                             (3, null, 3, '/member/**', 'url'),
                                                                                             (4, null, 4, '/user/**', 'url'),
                                                                                             (5, null, 5, '/shop/**', 'url'),
                                                                                             (6, null, 6, '/item/**', 'url'),
                                                                                             (7, null, 7, '/order/**', 'url'),
                                                                                             (8, null, 8, '/board/**', 'url');

-- Define resource access per role
INSERT INTO role_resources(RESOURCE_ID, ROLE_ID) VALUES
                                                     (1, 1),
                                                     (2, 2);


-- Define ip access
INSERT INTO lunch.access_ip (ip_id, ip_address, block) VALUES
                                                           (0, '0:0:0:0:0:0:0:1', false),
                                                           (1, '127.0.0.1', false)


INSERT INTO token_ignore_urls (token_ignore_id, is_ignore, url) VALUES (1, true, '/splash');
INSERT INTO token_ignore_urls (token_ignore_id, is_ignore, url) VALUES (2, true, '/order/recruits');
INSERT INTO token_ignore_urls (token_ignore_id, is_ignore, url) VALUES (3, true, '/board');
