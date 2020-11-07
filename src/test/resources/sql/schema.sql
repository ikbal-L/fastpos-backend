CREATE DATABASE dbtesting2;
use  dbtesting2;

create or replace table additive
(
    id bigint auto_increment
    primary key,
    backgroundString varchar(255) null,
    description varchar(255) null,
    `rank` int not null
);

create or replace table category
(
    id bigint auto_increment
    primary key,
    backgroundString varchar(255) null,
    name varchar(255) null,
    `rank` int not null
);

create or replace table customer
(
    id bigint auto_increment
    primary key,
    mobile varchar(255) null,
    name varchar(255) null
);

create or replace table product
(
    id bigint auto_increment
    primary key,
    availableStock int not null,
    backgroundString varchar(255) null,
    description varchar(255) null,
    isMuchInDemand bit not null,
    isPlatter bit not null,
    name varchar(255) null,
    price double not null,
    `rank` int not null,
    type varchar(255) null,
    unit varchar(255) null,
    category_id bigint null,
    constraint FKexqqeaksnmmku5py194ywp140
    foreign key (category_id) references category (id)
);

create or replace table products_additives
(
    product_id bigint not null,
    additive_id bigint not null,
    constraint FK8box3t5ly60kkr6tttbscosee
    foreign key (product_id) references product (id),
    constraint FKq3ultjqxcptt0t29uulwy57yu
    foreign key (additive_id) references additive (id)
);

create or replace table restaurent
(
    id bigint auto_increment
    primary key,
    address varchar(255) null,
    name varchar(255) null,
    serverLicenceKey varchar(255) null
);

create or replace table annex
(
    id bigint auto_increment
    primary key,
    address varchar(255) null,
    name varchar(255) null,
    serverLicenceKey varchar(255) null,
    restaurent_id bigint null,
    constraint FKlk20lf2tbvtuxgni82794pf0k
    foreign key (restaurent_id) references restaurent (id)
);

create or replace table tables
(
    id bigint auto_increment
    primary key,
    isVirtual bit not null,
    number int not null,
    seats int not null
);

create or replace table orders
(
    id bigint auto_increment
    primary key,
    additivesVisibility bit not null,
    buyerId varchar(255) null,
    discountAmount double not null,
    discountPercentage double not null,
    elapsedTime bigint null,
    givenAmount double not null,
    newTotal double not null,
    orderTime datetime null,
    orderTotal double not null,
    orderstate int null,
    productsVisibility bit not null,
    returnedAmount double not null,
    splittedFromId int not null,
    total double not null,
    totalDiscountAmount double not null,
    type int null,
    tables_id bigint null,
    constraint FKe5rvlcvymyef5878c1qpg86lu
    foreign key (tables_id) references tables (id)
);

create or replace table orderitem
(
    id bigint auto_increment
    primary key,
    discountAmount double not null,
    discountPercentatge double not null,
    name varchar(255) null,
    quantity int not null,
    total double not null,
    totalDiscountAmount double not null,
    unitPrice double not null,
    order_id bigint null,
    product_id bigint null,
    constraint FKc2tmocf2397u5byw57cysbp22
    foreign key (order_id) references orders (id),
    constraint FKg23j1vs750x8lkx2aesfk6n2
    foreign key (product_id) references product (id)
);

create or replace table orderitems_additives
(
    orderItem_id bigint not null,
    additive_id bigint not null,
    constraint FKc8viju2b0rvgnsod7v7atiyvi
    foreign key (additive_id) references additive (id),
    constraint FKn01e6niqtxpxrcqrlo0c7l2fa
    foreign key (orderItem_id) references orderitem (id)
);

create or replace table terminal
(
    id bigint auto_increment
    primary key,
    isActive bit not null,
    licenceKey varchar(255) null,
    annex_id bigint null,
    constraint FKdoj8w840c4jrsc6smx1wmpw89
    foreign key (annex_id) references annex (id)
);

