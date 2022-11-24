drop table if exists account CASCADE;
drop table if exists member_role CASCADE;
drop table if exists order_item CASCADE;
drop table if exists orders CASCADE;
drop table if exists roles CASCADE;
drop table if exists item CASCADE;
drop table if exists shop CASCADE;
drop table if exists member CASCADE;

create table roles
(
    roles_id bigint auto_increment,
    role     varchar(255),
    primary key (roles_id),
    constraint UK_ROLE unique (role)
);

create table member
(
    member_id          bigint auto_increment,
    created_date       timestamp,
    last_modified_date timestamp,
    email              varchar(255),
    name               varchar(255),
    password           varchar(255),
    primary key (member_id)
);

create table member_role
(
    member_roles_id bigint auto_increment,
    member_id       bigint,
    roles_id        bigint,
    primary key (member_roles_id),
    constraint FK_MEMBER_MEMBER_ROLE foreign key (member_id) references member (member_id),
    constraint UK_MEMBER_ROLE unique (member_id, roles_id)
);

create table account
(
    account_id         bigint auto_increment,
    created_date       timestamp,
    last_modified_date timestamp,
    account_number     varchar(255),
    bank_name          varchar(255),
    member_id          bigint,
    primary key (account_id),
    constraint FK_MEMBER_ACCOUNT foreign key (member_id) references member (member_id),
    constraint UK_ACCOUNT_MEMBER unique (member_id)
);

create table shop
(
    shop_id bigint auto_increment,
    created_date       timestamp,
    last_modified_date timestamp,
    create_by          varchar(255),
    last_modified_by   varchar(255),
    name    varchar(10) not null,
    primary key (shop_id),
    constraint UK_SHOP unique (name)
);

create table item
(
    item_id            bigint auto_increment,
    created_date       timestamp,
    last_modified_date timestamp,
    create_by          varchar(255),
    last_modified_by   varchar(255),
    name               varchar(255),
    price              integer not null,
    shop_id            bigint,
    primary key (item_id),
    constraint FK_SHOP_ITEM foreign key (shop_id) references shop (shop_id),
    constraint UK_NAME_SHOP unique (name, shop_id)
);

create table orders
(
    order_id   bigint auto_increment,
    order_date timestamp,
    status     varchar(255),
    member_id  bigint,
    primary key (order_id),
    constraint FK_MEMBER_ORDERS foreign key (member_id) references member (member_id)
);

create table order_item
(
    order_item_id  bigint auto_increment,
    order_price    integer not null,
    order_quantity integer not null,
    item_id        bigint,
    order_id       bigint,
    primary key (order_item_id),
    constraint FK_ITEM_ORDER_ITEM foreign key (item_id) references item (item_id),
    constraint FK_ORDER_ORDER_ITEM foreign key (order_id) references orders (order_id)
);



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

INSERT INTO shop (name)
values ('맥도날드');
INSERT INTO shop (name)
values ('맘스터치');

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


SELECT *
FROM MEMBER;

SELECT *
FROM ROLES;

SELECT *
FROM MEMBER_ROLE;

SELECT *
FROM SHOP;

SELECT *
FROM ITEM;

SELECT *
FROM ORDERS;

SELECT *
FROM ORDER_ITEM;

SELECT *
FROM ACCOUNT;