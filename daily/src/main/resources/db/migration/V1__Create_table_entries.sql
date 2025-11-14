CREATE TABLE entries (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID NOT NULL,
    removed_at TIMESTAMP NULL,
    updated_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    member_id BIGINT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_entries_member
        FOREIGN KEY (member_id) REFERENCES members(id)
        ON DELETE SET NULL,
    CONSTRAINT chk_entries_type
        CHECK (type IN (
            'WHAT_I_DID_YESTERDAY',
            'WHAT_I_DID_TODAY',
            'WHAT_I_PRETEND_TO_DO'
        ))
);
