SET search_path TO security;

DO $$
DECLARE
admin_uuid UUID := '00000000-0000-0000-0000-000000000001';
    admin_id   BIGINT;
BEGIN
    IF NOT EXISTS (SELECT 1 FROM security.users WHERE user_uuid = admin_uuid) THEN

        INSERT INTO security.users (user_uuid, is_enabled, is_deleted, registration_timestamp)
        VALUES (admin_uuid, true, false, NOW())
        RETURNING id INTO admin_id;

INSERT INTO security.login_data (login, user_id)
VALUES ('admin_super', admin_id);

INSERT INTO security.password_data (password, time_when_set, time_to_live, is_active, user_id)
VALUES (
           '$2a$10$EixZaYVK1fsbw1ZfbX3OXe.P0sYF0uRxG7l7qW7sD6nVq0p0wFZ2O',
           NOW(),
           '2099-12-31 23:59:59+00',
           true,
           admin_id
       );

INSERT INTO security.admins (user_id, is_active)
VALUES (admin_id, true);

INSERT INTO security.email_data (email, user_id)
VALUES ('admin@nemo.com', admin_id);

INSERT INTO security.user_roles (user_id, role)
VALUES (admin_id, 'ROLE_ADMIN');

END IF;
END $$;