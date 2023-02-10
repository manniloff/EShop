CREATE TABLE payment_method
(
    id SMALLINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(50),
    details VARCHAR(255)
);

CREATE TABLE payment
(
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    payment_method_id SMALLINT,
    payment_date TIMESTAMP(0),
    status VARCHAR(15),
    order_id BIGINT,
    CONSTRAINT status_check CHECK (status IN('pending','complete','refunded','failed','revoked','cancelled')),
    CONSTRAINT fk_payment_method FOREIGN KEY(payment_method_id) REFERENCES payment_method(id)
);