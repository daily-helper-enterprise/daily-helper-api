CREATE TABLE members (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE NOT NULL,
    removed_at TIMESTAMP NULL,
    updated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    name VARCHAR(255) NOT NULL,

    email VARCHAR(150) NOT NULL,
    username VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,

    ip VARCHAR(45),
    role_id BIGINT,

    CONSTRAINT uk_members_email UNIQUE (email),
    CONSTRAINT uk_members_username UNIQUE (username),

    CONSTRAINT fk_members_role
        FOREIGN KEY (role_id) REFERENCES public.roles(id)
        ON DELETE SET NULL
);
