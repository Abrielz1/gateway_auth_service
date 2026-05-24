CREATE SCHEMA IF NOT EXISTS security;
SET SEARCH_PATH TO security;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO
$$
    BEGIN
    CREATE TYPE security.role_type_enum AS ENUM (
        'ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER'
        );
    EXCEPTION
        WHEN duplicate_object THEN null;
    END
$$;

CREATE TABLE IF NOT EXISTS security.users(
  id                        BIGSERIAL PRIMARY KEY NOT NULL,
  user_uuid                 UUID UNIQUE NOT NULL,
  date_of_birth             DATE NOT NULL,
  is_enabled                BOOLEAN NOT NULL,
  is_deleted                BOOLEAN NOT NULL DEFAULT false,
  deleted_at                TIMESTAMPTZ DEFAULT null,
  registration_timestamp    TIMESTAMP WITH TIME ZONE,
  last_login_timestamp      TIMESTAMP WITH TIME ZONE,
  version                   BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS security.user_roles (
    user_id BIGINT NOT NULL,
    role security.role_type_enum NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS security.employees (
 user_id                 BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
 hire_date               DATE NOT NULL,
 employee_internal_id    VARCHAR(100) UNIQUE NOT NULL,
 termination_date        DATE,
 position                VARCHAR(255),
 department              VARCHAR(255),
 security_cleared        BOOLEAN NOT NULL DEFAULT false,
 mentor_id               BIGINT,
 FOREIGN KEY (mentor_id) REFERENCES security.employees(user_id)
);

CREATE TABLE IF NOT EXISTS security.customers (
user_id                 BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
registration_source     VARCHAR(255),
is_banned               BOOLEAN NOT NULL DEFAULT false,
invited_by_id           BIGINT,
FOREIGN KEY (invited_by_id) REFERENCES security.users(id)
);

CREATE TABLE IF NOT EXISTS security.password_data(
id                      BIGSERIAL PRIMARY KEY,
password                VARCHAR(2048) NOT NULL,
time_when_set           TIMESTAMPTZ NOT NULL,
time_to_live            TIMESTAMPTZ NOT NULL,
is_active               BOOLEAN NOT NULL,
version                 BIGINT NOT NULL DEFAULT 0,
user_id                 BIGINT NOT NULL,
FOREIGN KEY (user_id)   REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS security.email_data(
id BIGSERIAL            PRIMARY KEY,
email                   VARCHAR(255) NOT NULL UNIQUE,
version                 BIGINT NOT NULL DEFAULT 0,
user_id                 BIGINT NOT NULL,
FOREIGN KEY (user_id)   REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS security.login_data(
id                      BIGSERIAL PRIMARY KEY,
login                   VARCHAR(256) NOT NULL UNIQUE,
user_id                 BIGINT NOT NULL,
version                 BIGINT DEFAULT 0,
FOREIGN KEY (user_id)   REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS security.phone_data(
 id                      BIGSERIAL PRIMARY KEY,
 phone                   VARCHAR(64) NOT NULL UNIQUE,
 version                 BIGINT NOT NULL DEFAULT 0,
 user_id                 BIGINT NOT NULL,
 FOREIGN KEY (user_id)   REFERENCES security.users(id) ON DELETE CASCADE
);

CREATE INDEX idx_users_not_deleted ON security.users (is_deleted,id) WHERE is_deleted = false;
CREATE INDEX idx_users_last_login ON security.users (last_login_timestamp);
CREATE INDEX idx_users_dob ON security.users (date_of_birth);
CREATE INDEX idx_users_registration_brin ON security.users USING brin (registration_timestamp); -- специально использую brin для ранжирования
CREATE INDEX idx_users_active_registration ON security.users (is_deleted, registration_timestamp) WHERE is_deleted = false;

CREATE INDEX idx_employees_mentor ON security.employees(mentor_id);

CREATE INDEX idx_customers_invited_by ON security.customers(invited_by_id);

CREATE INDEX idx_phone_data_user_id ON security.phone_data(user_id);

CREATE INDEX idx_email_data_user_id ON security.email_data(user_id);

CREATE INDEX idx_password_data_user_id ON security.password_data(user_id);

CREATE INDEX idx_login_data_user_id ON security.login_data(user_id);

