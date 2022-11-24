INSERT INTO roles (role)
VALUES ('ROLE_ADMIN');
INSERT INTO roles (role)
VALUES ('ROLE_MANGER');
INSERT INTO roles (role)
VALUES ('ROLE_USER');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'khj@gmail.com', '김현진', 'test5678((');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin@gmail.com', '어드민', 'admin5678((');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'user@gmail.com', '유저', 'user5678((');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'manager@gmail.com', '매니저', 'manager5678((');

INSERT INTO member_role (member_id, roles_id)
values (1, 1);
INSERT INTO member_role (member_id, roles_id)
values (1, 2);
INSERT INTO member_role (member_id, roles_id)
values (1, 3);
INSERT INTO member_role (member_id, roles_id)
values (2, 1);
INSERT INTO member_role (member_id, roles_id)
values (3, 3);
INSERT INTO member_role (member_id, roles_id)
values (4, 2);

INSERT INTO account (created_date, last_modified_date, account_number, bank_name, member_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '3333-01-0630167', '카카오뱅크', 1);

INSERT INTO shop (created_date, last_modified_date, create_by, last_modified_by, name)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '맥도날드');
INSERT INTO shop (created_date, last_modified_date, create_by, last_modified_by, name)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '맘스터치');

INSERT INTO ITEM (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '슈비버거', '6300', '1');

INSERT INTO ITEM (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '슈슈버거', '6000', '1');

INSERT INTO ITEM (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '싸이버거', '5300', '2');

INSERT INTO ITEM (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '망한버거', '25300', '2');

INSERT INTO ORDERS (order_date, status, member_id)
values (CURRENT_TIMESTAMP, 'ORDER', 1);

insert into ORDER_ITEM (item_id, order_id, order_price, order_quantity)
values (1, 1, 6300, 1);