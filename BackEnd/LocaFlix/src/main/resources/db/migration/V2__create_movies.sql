CREATE TABLE movies (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    rate_stars INTEGER NOT NULL,
    release_year INTEGER NOT NULL,
    CONSTRAINT ck_movies_rate_stars CHECK (rate_stars >= 0 AND rate_stars <= 5),
    CONSTRAINT ck_movies_release_year CHECK (release_year > 0)
);
