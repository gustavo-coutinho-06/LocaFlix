CREATE TABLE sessions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_sessions_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_sessions_user_id ON sessions (user_id);

CREATE TABLE rentals (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    movie_id UUID NOT NULL,
    rented_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_rentals_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_rentals_movie FOREIGN KEY (movie_id) REFERENCES movies (id),
    CONSTRAINT uq_rentals_user_movie UNIQUE (user_id, movie_id)
);
