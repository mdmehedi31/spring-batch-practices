CREATE TABLE users
(
    id            BIGINT NOT NULL,
    user_id       VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    gender        VARCHAR(255),
    email         VARCHAR(255),
    phone         VARCHAR(255),
    date_of_birth VARCHAR(255),
    job_title     VARCHAR(255),
    PRIMARY KEY (id)
);