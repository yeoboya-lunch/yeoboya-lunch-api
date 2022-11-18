INSERT INTO roles (role) VALUES ('ROLE_ADMIN');
INSERT INTO roles (role) VALUES ('ROLE_MANGER');
INSERT INTO roles (role) VALUES ('ROLE_USER');

INSERT INTO member (created_date, last_modified_date, email, name, password)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'tester@gmail.com', '테스터', 'test5678((');

INSERT INTO member_role (member_id, roles_id)
    values (1, 1);

INSERT INTO shop (name) values ('맥도날드');
INSERT INTO shop (name) values ('맘스터치');

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
    values(1, 1, 6300, 1);