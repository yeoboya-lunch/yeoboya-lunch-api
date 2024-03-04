-- 기존 테이블 삭제
DROP TABLE IF EXISTS order_item CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS account CASCADE;
DROP TABLE IF EXISTS member_info CASCADE;
DROP TABLE IF EXISTS member_role CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS item CASCADE;
DROP TABLE IF EXISTS shop CASCADE;
DROP TABLE IF EXISTS role CASCADE;

-- 역할(Role) 테이블 생성
CREATE TABLE role
(
    roles_id BIGINT AUTO_INCREMENT,
    role     VARCHAR(255),
    PRIMARY KEY (roles_id),
    CONSTRAINT UK_ROLE UNIQUE (role)
);

-- 회원(Member) 테이블 생성
CREATE TABLE member
(
    member_id          BIGINT AUTO_INCREMENT,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    email              VARCHAR(255),
    name               VARCHAR(255),
    password           VARCHAR(255),
    PRIMARY KEY (member_id)
);

-- 회원 역할(Member_Role) 관계 테이블 생성
CREATE TABLE member_role
(
    member_roles_id BIGINT AUTO_INCREMENT,
    member_id       BIGINT,
    roles_id        BIGINT,
    PRIMARY KEY (member_roles_id),
    CONSTRAINT FK_MEMBER_MEMBER_ROLE FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT FK_ROLES_MEMBER_ROLE FOREIGN KEY (roles_id) REFERENCES role (roles_id),
    CONSTRAINT UK_MEMBER_ROLE UNIQUE (member_id, roles_id)
);

-- 회원 정보(Member_Info) 테이블 생성
CREATE TABLE member_info
(
    member_info_id BIGINT AUTO_INCREMENT,
    member_id      BIGINT,
    bio            VARCHAR(255) NOT NULL,
    nick_name      VARCHAR(255),
    phone_number   VARCHAR(255),
    PRIMARY KEY (member_info_id),
    CONSTRAINT FK_MEMBER_INFO FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT UK_MEMBER_MEMBER_INFO UNIQUE (member_id)
);

-- 계정(Account) 테이블 생성
CREATE TABLE account
(
    account_id         BIGINT AUTO_INCREMENT,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    account_number     VARCHAR(255),
    bank_name          VARCHAR(255),
    member_id          BIGINT,
    PRIMARY KEY (account_id),
    CONSTRAINT FK_MEMBER_ACCOUNT FOREIGN KEY (member_id) REFERENCES member (member_id),
    CONSTRAINT UK_ACCOUNT_MEMBER UNIQUE (member_id)
);

-- 상점(Shop) 테이블 생성
CREATE TABLE shop
(
    shop_id            BIGINT AUTO_INCREMENT,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    create_by          VARCHAR(255),
    last_modified_by   VARCHAR(255),
    name               VARCHAR(10) NOT NULL,
    PRIMARY KEY (shop_id),
    CONSTRAINT UK_SHOP UNIQUE (name)
);

-- 상품(Item) 테이블 생성
CREATE TABLE item
(
    item_id            BIGINT AUTO_INCREMENT,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    create_by          VARCHAR(255),
    last_modified_by   VARCHAR(255),
    name               VARCHAR(255),
    price              INTEGER NOT NULL,
    shop_id            BIGINT,
    PRIMARY KEY (item_id),
    CONSTRAINT FK_SHOP_ITEM FOREIGN KEY (shop_id) REFERENCES shop (shop_id),
    CONSTRAINT UK_NAME_SHOP UNIQUE (name, shop_id)
);

-- 주문(Orders) 테이블 생성
CREATE TABLE orders
(
    order_id        BIGINT AUTO_INCREMENT,
    order_date      TIMESTAMP,
    status          VARCHAR(255),
    member_id       BIGINT,
    delivery_fee    BINARY,
    PRIMARY KEY (order_id),
    CONSTRAINT FK_MEMBER_ORDERS FOREIGN KEY (member_id) REFERENCES member (member_id)
);

-- 주문 항목(Order_Item) 테이블 생성
CREATE TABLE order_item
(
    order_item_id  BIGINT AUTO_INCREMENT,
    order_price    INTEGER NOT NULL,
    order_quantity INTEGER NOT NULL,
    item_id        BIGINT,
    order_id       BIGINT,
    PRIMARY KEY (order_item_id),
    CONSTRAINT FK_ITEM_ORDER_ITEM FOREIGN KEY (item_id) REFERENCES item (item_id),
    CONSTRAINT FK_ORDER_ORDER_ITEM FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

-- 초기 데이터 삽입
-- 역할 데이터 삽입
INSERT INTO role (role)
VALUES ('ROLE_ADMIN'),
       ('ROLE_MANGER'),
       ('ROLE_USER');

-- 회원 데이터 삽입
INSERT INTO member (created_date, last_modified_date, email, name, password)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'khj@gmail.com', '김현진', 'test5678(('),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin@gmail.com', '어드민', 'admin5678(('),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'user@gmail.com', '유저', 'user5678(('),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'manager@gmail.com', '매니저', 'manager5678((');

-- 회원 역할 관계 데이터 삽입
INSERT INTO member_role (member_id, roles_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (3, 3),
       (4, 2);

-- 계정 데이터 삽입
INSERT INTO account (created_date, last_modified_date, account_number, bank_name, member_id)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '3333-01-0630167', '카카오뱅크', 1);

-- 상점 및 상품 데이터 삽입
INSERT INTO shop (name)
VALUES ('맥도날드');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '슈비버거', 6300, 1),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '슈슈버거', 6000, 1);

INSERT INTO shop (name)
VALUES ('맘스터치');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '싸이버거', 5300, 2),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '망한버거', 25300, 2);

INSERT INTO shop (name)
VALUES ('상무초밥');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
VALUES (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', '우럭초밥', 1200, 3),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', '계란초밥 2피스', 2400, 3),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', '커플세트(소) 2인', 35000, 3),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', '우정세트(중) 3인', 55000, 3),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', '초밥세트(특대) 6인', 130000, 3),
       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin', 'admin', '오늘의초밥', 9900, 3);

-- 주문 데이터 삽입
-- member_id는 주문을 하는 회원의 ID를 사용해야 합니다.
-- 이 예제에서는 member_id = 1 (김현진)으로 가정합니다.
INSERT INTO orders (order_date, status, member_id)
VALUES ('2022-11-20 10:20:00', 'ORDER', 1),
       ('2022-11-20 20:20:00', 'ORDER', 3),
       ('2022-11-21 09:20:00', 'ORDER', 3),
       ('2022-12-24 10:20:00', 'ORDER', 3);

-- 주문 항목 데이터 삽입
-- order_id와 item_id는 적절한 주문 및 상품의 ID를 사용해야 합니다.
-- 이 예제에서는 주문 ID와 상품 ID를 가정하여 작성합니다.
INSERT INTO order_item (item_id, order_id, order_price, order_quantity)
VALUES (1, 1, 6300, 1),  -- '슈비버거' 1개 주문
       (2, 1, 6000, 2),  -- '슈슈버거' 2개 주문
       (4, 2, 25300, 5), -- '망한버거' 5개 주문
       (5, 3, 1200, 5),  -- '우럭초밥' 5개 주문
       (6, 3, 2400, 10), -- '계란초밥 2피스' 10개 주문
       (10, 4, 9900, 1); -- '오늘의초밥' 1개 주문





