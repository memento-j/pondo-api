CREATE TABLE scores (
    id            UUID PRIMARY KEY,
    user_id       UUID,
    device_id     UUID NOT NULL,
    game          VARCHAR(20) NOT NULL,
    puzzle        INT NOT NULL,
    total_score   INT NOT NULL,
    round_scores  INT[] NOT NULL,
    hints_used    BOOLEAN[] NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL,
    UNIQUE (game, puzzle, device_id)
);

CREATE INDEX idx_scores_percentile ON scores (game, puzzle, total_score);