-- 역할과 그 계층 관계 설정
INSERT INTO role_hierarchy(CHILD_NAME, PARENT_NAME)
VALUES ('ROLE_ADMIN', null),
       ('ROLE_MANAGER', 'ROLE_ADMIN'),
       ('ROLE_USER', 'ROLE_MANAGER'),
       ('ROLE_GUEST', 'ROLE_USER'),
       ('ROLE_BLOCK', 'ROLE_GUEST');

-- 역할 정의
INSERT INTO role(ROLES_ID, ROLE, ROLE_DESC)
VALUES (1, 'ROLE_ADMIN', '어드민'),
       (2, 'ROLE_MANAGER', '매니저'),
       (3, 'ROLE_USER', '유저'),
       (4, 'ROLE_GUEST', '게스트'),
       (5, 'ROLE_BLOCK', '차단');


-- IP접근 설정
INSERT INTO access_ip (ip_id, ip_address, block)
VALUES (0, '0:0:0:0:0:0:0:1', false),
       (1, '127.0.0.1', false);

-- 토큰 무시 URL 설정
INSERT INTO token_ignore_urls (token_ignore_id, is_ignore, url)
VALUES (1, true, '/'),
       (2, true, '/redoc.html');

-- 상점
INSERT INTO shop (SHOP_ID, CREATED_BY, LAST_MODIFIED_BY, NAME)
VALUES (1, 'admin', 'admin', '테스트상점');

-- 아이템
INSERT INTO item (CREATED_BY, LAST_MODIFIED_BY, NAME, PRICE, SHOP_ID, ITEM_ID)
VALUES ('admin', 'admin', 'Apple', '1200', 1, 1);

-- 주문
INSERT INTO orders (DELIVERY_FEE, LAST_ORDER_TIME, MEMO, ORDER_DATE, STATUS, TITLE, MEMBER_ID, SHOP_ID, ORDER_ID)
VALUES (1000, CURRENT_TIMESTAMP() + INTERVAL '30' MINUTE, '신상메뉴 나왔습니다!!', '2024-06-28 15:13:59.625000', 'END', '사과 드실분', 1, 1, 1);

INSERT INTO group_order (ORDER_DATE, MEMBER_ID, ORDER_ID, GROUP_ORDER_ID)
VALUES (CURRENT_TIMESTAMP() , 1, 1, 1);

-- QNA
INSERT INTO inquiry (CREATED_BY, LAST_MODIFIED_BY, CONTENT, EMAIL, LOGIN_ID, SUBJECT, INQUIRY_ID)
VALUES ('admin', 'admin', '내용', 'admin@yeoboya.com', 'admin', '내용 이야기', 1);

-- Notice
INSERT INTO PUBLIC.NOTICE (CREATED_BY, LAST_MODIFIED_BY, ATTACHMENT_URL, AUTHOR, CATEGORY, CONTENT, END_DATE, PRIORITY,
    START_DATE, STATUS, TAGS, TITLE, VIEW_COUNT, NOTICE_ID
)
VALUES
    ('admin', 'admin', 'http://example.com/attachment', 'Admin', 'General', 'This is the content of the new notice.',
        '2024-07-05 15:21:37.243022', 1, '2024-06-28 15:21:37.243011', 'ACTIVE', 'announcement, general', 'New Notice Title', 0, 1);

-- BANNER
INSERT INTO banner (DISPLAY_LOCATION, DISPLAY_ORDER, END_DATE, START_DATE, TITLE, BANNER_ID)
VALUES ('MAIN_PAGE', 1, '2024-12-31 23:59:59.000000', '2024-01-01 00:00:01.000000', 'rCKJUcr4wV', 1);

INSERT INTO banner_file (EXTENSION, FILE_NAME, FILE_PATH, ORIGINAL_FILE_NAME, SIZE, BANNER_ID, BANNER_FILE_ID)
VALUES ('application/octet-stream', '20240628151026669960.jpg', 'banner/2024/06/28/1', 'test.jpg', 23032, 1, 1);
