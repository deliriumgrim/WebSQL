-- DROP DATABASE info_21;

CREATE TABLE IF NOT EXISTS peers
(
    nickname VARCHAR PRIMARY KEY,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks
(
    title       VARCHAR PRIMARY KEY,
    parent_task VARCHAR DEFAULT NULL REFERENCES tasks (title) ON DELETE CASCADE,
    max_xp      INTEGER
);

CREATE TABLE IF NOT EXISTS checks
(
    id   BIGSERIAL PRIMARY KEY,
    peer VARCHAR NOT NULL REFERENCES peers (nickname) ON DELETE CASCADE,
    task VARCHAR NOT NULL REFERENCES tasks (title) ON DELETE CASCADE,
    date DATE    NOT NULL
);


CREATE TYPE CHECK_STATUS AS ENUM ('Start', 'Success', 'Failure');

CREATE TABLE IF NOT EXISTS p2p
(
    id            BIGSERIAL PRIMARY KEY,
    check_id      BIGINT REFERENCES checks (id) ON DELETE CASCADE,
    checking_peer VARCHAR REFERENCES peers (nickname) ON DELETE CASCADE,
    state         CHECK_STATUS NOT NULL,
    time          TIME         NOT NULL,
    UNIQUE (check_id, checking_peer, state)
);


CREATE TABLE IF NOT EXISTS verter
(
    id       BIGSERIAL PRIMARY KEY,
    check_id BIGINT REFERENCES checks (id) ON DELETE CASCADE,
    state    CHECK_STATUS NOT NULL,
    time     TIME         NOT NULL,
    UNIQUE (check_id, state)
);

CREATE TABLE IF NOT EXISTS transferred_points
(
    id            BIGSERIAL PRIMARY KEY,
    checking_peer VARCHAR NOT NULL REFERENCES peers (nickname) ON DELETE CASCADE,
    checked_peer  VARCHAR NOT NULL REFERENCES peers (nickname) ON DELETE CASCADE,
    points_amount INTEGER DEFAULT 1 CHECK ( points_amount >= 0 )
);

CREATE TABLE IF NOT EXISTS friends
(
    id     BIGSERIAL PRIMARY KEY,
    peer_1 VARCHAR REFERENCES peers (nickname) ON DELETE CASCADE,
    peer_2 VARCHAR REFERENCES peers (nickname) ON DELETE CASCADE,
    UNIQUE (peer_1, peer_2)
);

CREATE TABLE IF NOT EXISTS recommendations
(
    id               BIGSERIAL PRIMARY KEY,
    peer             VARCHAR NOT NULL REFERENCES peers (nickname) ON DELETE CASCADE,
    recommended_peer VARCHAR NOT NULL REFERENCES peers (nickname) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS xp
(
    id        BIGSERIAL PRIMARY KEY,
    check_id  BIGINT REFERENCES checks (id)  ON DELETE CASCADE UNIQUE,
    xp_amount INTEGER CHECK ( xp_amount > 0 )
);

-- Добавить триггер для отслеживания даты входа и выхода(нужно что бы все происходило в один день)
CREATE TABLE IF NOT EXISTS time_tracking
(
    id    BIGSERIAL PRIMARY KEY,
    peer  VARCHAR NOT NULL REFERENCES peers (nickname)  ON DELETE CASCADE,
    date  DATE    NOT NULL,
    time  TIME    NOT NULL,
    state INTEGER CHECK ( state IN (1, 2) )
);

CREATE OR REPLACE PROCEDURE export(
    table_name VARCHAR,
    source VARCHAR,
    delimiter VARCHAR(1) DEFAULT ',')
    LANGUAGE plpgsql
AS
$$
BEGIN
    EXECUTE format('COPY %I TO %L WITH DELIMITER %L CSV HEADER', table_name, source, delimiter);
END;
$$;

-- CALL export('xp', '/Users/mymac/Desktop/daily routin/platform-21/SQL2_Info21_v1.0-0/test3.csv');


CREATE OR REPLACE PROCEDURE import(
    name_table VARCHAR,
    source VARCHAR,
    delimiter VARCHAR(1) DEFAULT ',')
    LANGUAGE plpgsql
AS
$$
DECLARE
    result VARCHAR;
    name_tmp VARCHAR;
    max_id BIGINT;
BEGIN
    EXECUTE format('COPY %I FROM %L WITH DELIMITER %L CSV HEADER', name_table, source, delimiter);
    name_tmp := name_table;

    SELECT column_name INTO result
    FROM information_schema.columns
    WHERE table_name = name_tmp
    LIMIT 1;

    IF (result = 'id')
    THEN
        EXECUTE 'SELECT max(id) FROM ' || name_tmp INTO max_id;
        name_tmp :=  name_tmp || '_id_seq';
        EXECUTE 'SELECT setval(' || quote_literal(name_tmp) || ', ' || max_id || ')';
    END IF;
END;
$$;

-- CALL import('xp', '/Users/mymac/Desktop/daily routin/platform-21/SQL2_Info21_v1.0-0/test3.csv');
