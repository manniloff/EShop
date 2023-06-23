CREATE TABLE product_category_relation
(
    category_id SMALLINT,
    product_id  BIGINT
);

CREATE TABLE bucket_product_relation
(
    product_id BIGINT,
    bucket_id  BIGINT
);

CREATE TABLE product_order_relation
(
    product_id BIGINT,
    order_id   BIGINT
);

CREATE TABLE category
(
    id    SMALLINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(50)
);

CREATE TABLE product
(
    id          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    category_id SMALLINT,
    title       VARCHAR(50),
    description VARCHAR(255),
    price       DOUBLE,
    sale        SMALLINT,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);

CREATE TABLE role
(
    id        SMALLINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    role_type VARCHAR(50),
    CONSTRAINT role_check CHECK (role_type IN ('ADMIN', 'CUSTOMER', 'MODERATOR'))
);

CREATE TABLE user_address_relation
(
    user_id    BIGINT,
    address_id BIGINT
);

CREATE TABLE address
(
    id           BIGINT PRIMARY KEY                                                                                 NOT NULL AUTO_INCREMENT,
    country      VARCHAR(255),
    city         VARCHAR(255),
    street       VARCHAR(255),
    block        VARCHAR(50),
    house        VARCHAR(50),
    full_address varchar(500) GENERATED ALWAYS AS (CONCAT(country, ' ', city, ' ', street, ' ', house, ' ', block)) NOT NULL UNIQUE
);

CREATE TABLE user
(
    id           BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    address_id   BIGINT,
    email        VARCHAR(255),
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    phone_number VARCHAR(12),
    role_id      SMALLINT,
    password     VARCHAR(50),
    birthday     DATE,
    full_name    VARCHAR(200) GENERATED ALWAYS AS (CONCAT(first_name, ' ', last_name)) VIRTUAL,
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address (id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE bucket
(
    id         BIGINT PRIMARY KEY NOT NULL,
    product_id BIGINT,
    CONSTRAINT fk_bucket_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE order_info
(
    id            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    trans_id      BINARY(16),
    creation_date TIMESTAMP(0),
    status        VARCHAR(100),
    product_id    BIGINT,
    order_price   DOUBLE,
    CONSTRAINT status_check CHECK (status IN
                                   ('completed', 'in_progress', 'waiting_payment', 'created', 'canceled', 'paid')),
    CONSTRAINT fk_order_product FOREIGN KEY (product_id) REFERENCES product (id)
);