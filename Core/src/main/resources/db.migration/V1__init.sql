CREATE TABLE product_category_relation
(
    category_id SMALLINT,
    product_id  BIGINT
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
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE role
(
    id   SMALLINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    role VARCHAR(50),
    CONSTRAINT role_check CHECK (role IN ('ADMIN', 'CUSTOMER', 'MODERATOR'))
);

CREATE TABLE address
(
    id      BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    country VARCHAR(255),
    city    VARCHAR(255),
    street  VARCHAR(255),
    block   VARCHAR(50),
    house   VARCHAR(50)
);

CREATE TABLE user
(
    phone_number BIGINT PRIMARY KEY NOT NULL,
    address_id   BIGINT,
    email        VARCHAR(255),
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    role_id      SMALLINT,
    password     VARCHAR(50),
    birthday     TIMESTAMP(0),
    CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES address (id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE bucket
(
    id       BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id  BIGINT,
    item     VARCHAR(255),
    discount SMALLINT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user (phone_number)
);

CREATE TABLE order_info
(
    id              BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    bucket_id       BIGINT,
    date_time       TIMESTAMP(0),
    status          VARCHAR(100),
    payment_method  VARCHAR(50),
    delivery_method VARCHAR(50),
    order_price     DOUBLE,
    CONSTRAINT fk_bucket FOREIGN KEY (bucket_id) REFERENCES bucket (id),
    CONSTRAINT status_check CHECK (status IN ('completed', 'in_progress', 'waiting_payment')),
    CONSTRAINT payment_method_check CHECK (payment_method IN ('card', 'paypal', 'cash')),
    CONSTRAINT delivery_method_check CHECK (delivery_method IN ('DHL', 'NOVAPOSHTA', 'FEDEX'))
);