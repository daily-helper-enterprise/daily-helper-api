CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP,
    removed_at TIMESTAMP,
    scrum_master_id BIGINT,
    CONSTRAINT fk_teams_scrum_master
        FOREIGN KEY (scrum_master_id) REFERENCES members(id)
        ON DELETE SET NULL
);
