INSERT INTO roles (role) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role) VALUES ('ROLE_MANGER');
INSERT INTO roles (role) VALUES ('ROLE_USER');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'khj@gmail.com', '김현진', 'test5678((');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin@gmail.com', '어드민', 'admin5678((');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'user@gmail.com', '유저', 'user5678((');

INSERT INTO member (created_date, last_modified_date, email, name, password)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'manager@gmail.com', '매니저', 'manager5678((');

INSERT INTO member_role (member_id, roles_id) values (1, 1);
INSERT INTO member_role (member_id, roles_id) values (1, 2);
INSERT INTO member_role (member_id, roles_id) values (1, 3);
INSERT INTO member_role (member_id, roles_id) values (2, 1);
INSERT INTO member_role (member_id, roles_id) values (3, 3);
INSERT INTO member_role (member_id, roles_id) values (4, 2);

INSERT INTO account (created_date, last_modified_date, account_number, bank_name, member_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '3333-01-0630167', '카카오뱅크', 1);

INSERT INTO shop (name) values ('맥도날드');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '슈비버거', '6300', '1');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '슈슈버거', '6000', '1');


INSERT INTO shop (name) values ('맘스터치');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '싸이버거', '5300', '2');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '망한버거', '25300', '2');


INSERT INTO shop (name) values ('상무초밥');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '우럭초밥', '1200', '3');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '계란초밥 2피스', '2400', '3');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '커플세트(소) 2인', '35000', '3');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '우정세트(중) 3인', '55000', '3');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '초밥세트(특대) 6인', '130000', '3');
INSERT INTO item (created_date, last_modified_date, create_by, last_modified_by, name, price, shop_id)
values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'test', 'test', '오늘의초밥', '9900', '3');


INSERT INTO orders (order_date, status, member_id) values (CURRENT_TIMESTAMP, 'ORDER', 1);
insert into order_item (item_id, order_id, order_price, order_quantity) values (1, 1, 6300, 1);
insert into order_item (item_id, order_id, order_price, order_quantity) values (2, 1, 6000, 2);

INSERT INTO orders (order_date, status, member_id) values ('2022-11-20 10:20:00', 'ORDER', 3);
insert into order_item (item_id, order_id, order_price, order_quantity) values (1, 2, 6300, 1);
insert into order_item (item_id, order_id, order_price, order_quantity) values (2, 2, 6000, 2);

INSERT INTO orders (order_date, status, member_id) values ('2022-11-20 20:20:00', 'ORDER', 3);
insert into order_item (item_id, order_id, order_price, order_quantity) values (4, 3, 25300, 5);

INSERT INTO orders (order_date, status, member_id) values ('2022-11-21 09:20:00', 'ORDER', 3);
insert into order_item (item_id, order_id, order_price, order_quantity) values (5, 4, 1200, 5);
insert into order_item (item_id, order_id, order_price, order_quantity) values (6, 4, 2400, 10);

INSERT INTO orders (order_date, status, member_id) values ('2022-12-24 10:20:00', 'ORDER', 3);
insert into order_item (item_id, order_id, order_price, order_quantity) values (3, 5, 5300, 1);
insert into order_item (item_id, order_id, order_price, order_quantity) values (1, 5, 6300, 1);
insert into order_item (item_id, order_id, order_price, order_quantity) values (10, 5, 9900, 1);
