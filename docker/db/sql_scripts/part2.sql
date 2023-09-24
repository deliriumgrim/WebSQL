-- 1) Написать процедуру добавления P2P проверки
-- Параметры: ник проверяемого, ник проверяющего, название задания, статус P2P проверки, время.
-- Если задан статус "начало", добавить запись в таблицу Checks (в качестве даты использовать сегодняшнюю).
-- и в качестве проверки указать только что добавленную запись, иначе указать проверку с незавершенным P2P этапом.
-- Добавить запись в таблицу P2P.

CREATE OR REPLACE PROCEDURE insert_p2p_check(peer_checked_name VARCHAR,
                                             peer_checking_name VARCHAR,
                                             task_title VARCHAR,
                                             state_of_p2p_check CHECK_STATUS,
                                             time_t TIME)
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (peer_checking_name = peer_checked_name)
    THEN
        RAISE EXCEPTION 'Проверяющий не может быть равен проверяемому';
    ELSEIF (state_of_p2p_check = 'Start')
    THEN
        INSERT INTO checks (peer, task, date)
        VALUES (peer_checked_name, task_title, now()::DATE);
        INSERT INTO p2p (check_id, checking_peer, state, time)
        VALUES ((SELECT MAX(id)
                 FROM checks), peer_checking_name,
                state_of_p2p_check,
                time_t);
    ELSE
        INSERT INTO p2p (check_id, checking_peer, state, time)
        VALUES ((SELECT MAX(id)
                 FROM checks), peer_checking_name,
                state_of_p2p_check,
                time_t);
    END IF;
END
$$;

-- CALL insert_p2p_check('drumfred', 'evaelfie', 'C4_s21_math', 'Start', '21:37');
-- CALL insert_p2p_check('drumfred', 'evaelfie', 'C4_s21_math', 'Success', '21:39');


-- 2) Написать процедуру добавления проверки Verter'ом
-- Параметры: ник проверяемого, название задания, статус проверки Verter'ом, время.
-- Добавить запись в таблицу Verter (в качестве проверки указать проверку соответствующего задания с самым поздним (по времени) успешным P2P этапом)

CREATE OR REPLACE PROCEDURE insert_verter_check(peer_checked_name VARCHAR,
                                                task_title VARCHAR,
                                                state_of_check CHECK_STATUS,
                                                time_t TIME)
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF ((SELECT count(c.id)
         FROM checks AS c
                  JOIN p2p p on c.id = p.check_id
         WHERE c.peer = peer_checked_name
           AND c.task = task_title
           AND p.state = 'Success') = 0)
    THEN
        RAISE EXCEPTION 'Проверка не находится в статусе Success';
    ELSE
        INSERT INTO verter (check_id, state, time)
        VALUES ((SELECT c.id
                 FROM checks AS c
                          JOIN p2p AS p ON c.id = p.check_id
                 WHERE c.peer = peer_checked_name
                   AND c.task = task_title
                   AND p.state = 'Success'
                 ORDER BY p.time DESC
                 LIMIT 1), state_of_check, time_t);
    END IF;
END
$$;

--1 тест Должен быть exception т.к. naruto завалил п2п
-- CALL insert_verter_check('naruto', 'C2_SimpleBashUtils', 'Success', now()::TIME);
-- CALL insert_verter_check('karim', 'C4_s21_math', 'Failure', now()::TIME);


-- 3) Написать триггер: после добавления записи со статутом "начало" в таблицу P2P,
-- изменить соответствующую запись в таблице TransferredPoints

CREATE OR REPLACE FUNCTION fnc_transferred_points_update_points() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF (NEW.state = 'Start')
    THEN
        UPDATE transferred_points
        SET points_amount = points_amount + 1
        WHERE checking_peer = NEW.checking_peer
          AND checked_peer = (SELECT peer
                              FROM checks AS c
                                       JOIN p2p p on c.id = p.check_id
                              WHERE NEW.check_id = c.id
                              LIMIT 1);
    END IF;
    RETURN NULL;
END
$$;

CREATE TRIGGER trg_transferred_points_update_points
    AFTER INSERT
    ON p2p
    FOR EACH ROW
EXECUTE FUNCTION fnc_transferred_points_update_points();

-- --transferred_points has changed
-- INSERT INTO p2p (check_id, checking_peer, state, time)
-- VALUES (3, 'drumfred', 'Start', '21:30');
-- --transferred_points hasn't changed
-- INSERT INTO p2p (check_id, checking_peer, state, time)
-- VALUES (3, 'drumfred', 'Success', '21:35');


-- 4) Написать триггер: перед добавлением записи в таблицу XP, проверить корректность добавляемой записи
-- Запись считается корректной, если:
-- Количество XP не превышает максимальное доступное для проверяемой задачи
-- Поле Check ссылается на успешную проверку
-- Если запись не прошла проверку, не добавлять её в таблицу.

CREATE OR REPLACE FUNCTION fnc_xp_insert_or_update() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF ((SELECT count(state)
         FROM p2p
         WHERE p2p.check_id = NEW.check_id
           AND p2p.state = 'Success') = 0)
    THEN
        RAISE EXCEPTION 'Проверка из таблица p2p не находится в состоянии Success';
    ELSEIF ((SELECT tasks.max_xp - NEW.xp_amount
             FROM tasks
                      JOIN checks c on tasks.title = c.task
             WHERE c.id = NEW.check_id) < 0)
    THEN
        RAISE EXCEPTION 'Количество xp превышает максимально допустимое';
    ELSE
        RETURN NEW;
    END IF;
END
$$;

CREATE OR REPLACE TRIGGER trg_xp_insert_or_update
    BEFORE INSERT OR UPDATE
    ON xp
    FOR EACH ROW
EXECUTE FUNCTION fnc_xp_insert_or_update();

-- INSERT INTO xp (check_id, xp_amount) VALUES (14, 100); --must be failed
-- INSERT INTO xp (check_id, xp_amount) VALUES (13, 100000); --must be failed
-- INSERT INTO xp (check_id, xp_amount) VALUES (20, 299);

INSERT INTO peers (nickname, birthday)
VALUES ('karim', '2003-05-31'),
       ('naruto', '1999-01-01'),
       ('terminator', '1950-01-01'),
       ('tyuuki', '2003-05-31'),
       ('drumfred', '1995-10-11'),
       ('meganfox', '1986-05-16'),
       ('evaelfie', '1997-01-01'),
       ('leeroy', '1997-02-09');

INSERT INTO tasks (title, parent_task, max_xp)
VALUES ('C2_SimpleBashUtils', NULL, 250),
       ('C3_s21_string+', 'C2_SimpleBashUtils', 500),
       ('C4_s21_math', 'C2_SimpleBashUtils', 300),
       ('C5_s21_decimal', 'C2_SimpleBashUtils', 350),
       ('C6_s21_matrix', 'C2_SimpleBashUtils', 200),
       ('C7_SmartCalc_v1.0', 'C6_s21_matrix', 500),
       ('C8_3DViewer_v1.0', 'C7_SmartCalc_v1.0', 750),
       ('DO1_Linux', 'C3_s21_string+', 300),
       ('DO2_LinuxMonitoring v2.0', 'DO1_Linux', 350);

INSERT INTO checks (peer, task, date)
VALUES ('karim', 'C2_SimpleBashUtils', '2022-01-02'),
       ('karim', 'C3_s21_string+', '2022-01-02'),
       ('karim', 'C4_s21_math', '2022-01-02'),
       ('karim', 'C5_s21_decimal', '2022-01-02'),
       ('drumfred', 'C5_s21_decimal', '2022-01-01'),
       ('drumfred', 'C6_s21_matrix', '2022-01-01'),
       ('drumfred', 'C7_SmartCalc_v1.0', '2022-01-01'),
       ('naruto', 'C2_SimpleBashUtils', '2022-01-01'),
       ('naruto', 'C2_SimpleBashUtils', '2022-01-01'),
       ('terminator', 'C7_SmartCalc_v1.0', '2022-01-01'),
       ('terminator', 'C8_3DViewer_v1.0', '2022-01-01'),
       ('meganfox', 'C6_s21_matrix', '2022-01-02'),
       ('evaelfie', 'C3_s21_string+', '2022-01-01'),
       ('evaelfie', 'C4_s21_math', '2022-01-01'),
       ('karim', 'DO1_Linux', '2022-01-02'),
       ('karim', 'C6_s21_matrix', '2022-01-02'),
       ('karim', 'C7_SmartCalc_v1.0', '2022-01-02'),
       ('karim', 'C8_3DViewer_v1.0', '2022-01-02'),
       ('karim', 'DO1_Linux', '2022-01-02');

INSERT INTO p2p (check_id, checking_peer, state, time)
VALUES (1, 'evaelfie', 'Start', '09:42'),
       (1, 'evaelfie', 'Success', '09:43'),
       (2, 'drumfred', 'Start', '12:00'),
       (2, 'drumfred', 'Success', '12:15'),
       (3, 'tyuuki', 'Start', '13:21'),
       (3, 'tyuuki', 'Success', '13:25'),
       (4, 'meganfox', 'Start', '15:55'),
       (4, 'meganfox', 'Success', '15:56'),
       (5, 'evaelfie', 'Start', '21:15'),
       (5, 'evaelfie', 'Success', '21:22'),
       (6, 'tyuuki', 'Start', '21:30'),
       (6, 'tyuuki', 'Success', '21:40'),
       (7, 'meganfox', 'Start', '21:50'),
       (7, 'meganfox', 'Success', '21:58'),
       (8, 'drumfred', 'Start', '08:50'),
       (8, 'drumfred', 'Failure', '08:52'),
       (9, 'terminator', 'Start', '09:30'),
       (9, 'terminator', 'Failure', '09:35'),
       (10, 'naruto', 'Start', '16:30'),
       (10, 'naruto', 'Success', '16:38'),
       (11, 'karim', 'Start', '17:00'),
       (11, 'karim', 'Success', '17:07'),
       (12, 'terminator', 'Start', '13:25'),
       (12, 'terminator', 'Success', '13:29'),
       (13, 'naruto', 'Start', '21:45'),
       (13, 'naruto', 'Success', '21:47'),
       (14, 'karim', 'Start', '21:59'),
       (14, 'karim', 'Failure', '22:02'),
       (15, 'terminator', 'Start', '18:25'),
       (15, 'terminator', 'Success', '18:26'),
       (16, 'drumfred', 'Start', '9:32'),
       (16, 'drumfred', 'Success', '9:36'),
       (17, 'naruto', 'Start', '9:40'),
       (17, 'naruto', 'Success', '9:45'),
       (18, 'terminator', 'Start', '9:50'),
       (18, 'terminator', 'Success', '9:55'),
       (19, 'meganfox', 'Start', '22:15'),
       (19, 'meganfox', 'Success', '22:25');

INSERT INTO verter (check_id, state, time)
VALUES (1, 'Success', '09:44'),
       (2, 'Success', '12:18'),
       (3, 'Success', '13:26'),
       (4, 'Success', '15:57'),
       (5, 'Success', '21:23'),
       (6, 'Success', '21:44'),
       (7, 'Success', '22:44'),
       (10, 'Success', '16:50'),
       (11, 'Failure', '17:30'),
       (12, 'Success', '13:50'),
       (13, 'Success', '21:50'),
       (16, 'Success', '9:37'),
       (17, 'Success', '9:46'),
       (18, 'Success', '10:00');

INSERT INTO transferred_points (checking_peer, checked_peer, points_amount)
VALUES ('karim', 'terminator', 1),
       ('karim', 'evaelfie', 1),
       ('naruto', 'terminator', 1),
       ('naruto', 'meganfox', 1),
       ('terminator', 'karim', 1),
       ('terminator', 'meganfox', 1),
       ('terminator', 'naruto', 1),
       ('tyuuki', 'karim', 1),
       ('tyuuki', 'drumfred', 1),
       ('drumfred', 'naruto', 1),
       ('meganfox', 'drumfred', 1),
       ('evaelfie', 'karim', 1),
       ('evaelfie', 'drumfred', 1),
       ('drumfred', 'karim', 1),
       ('naruto', 'karim', 1),
       ('meganfox', 'karim', 1);

INSERT INTO friends (peer_1, peer_2)
VALUES ('karim', 'evaelfie'),
       ('karim', 'drumfred'),
       ('terminator', 'meganfox'),
       ('terminator', 'evaelfie'),
       ('naruto', 'terminator'),
       ('tyuuki', 'karim'),
       ('tyuuki', 'drumfred'),
       ('drumfred', 'evaelfie'),
       ('drumfred', 'meganfox'),
       ('drumfred', 'naruto'),
       ('meganfox', 'evaelfie'),
       ('meganfox', 'tyuuki');

INSERT INTO recommendations (peer, recommended_peer)
VALUES ('karim', 'evaelfie'),
       ('karim', 'drumfred'),
       ('tyuuki', 'meganfox'),
       ('tyuuki', 'naruto'),
       ('drumfred', 'meganfox'),
       ('naruto', 'karim'),
       ('terminator', 'drumfred'),
       ('terminator', 'evaelfie');

INSERT INTO xp (check_id, xp_amount)
VALUES (1, 250),
       (2, 495),
       (3, 300),
       (4, 349),
       (5, 350),
       (6, 200),
       (7, 500),
       (10, 450),
       (12, 100),
       (13, 420),
       (16, 190),
       (17, 200),
       (18, 500),
       (15, 280),
       (19, 300);

INSERT INTO time_tracking (peer, date, time, state)
VALUES ('evaelfie', '2022-01-01', '21:30', 1),
       ('karim', '2022-01-02', '09:30', 1),
       ('naruto', '2022-01-01', '07:59', 1),
       ('terminator', '2022-01-01', '15:21', 1),
       ('drumfred', '2022-01-01', '21:00', 1),
       ('meganfox', '2022-01-02', '12:12', 1),
       ('tyuuki', '2022-01-02', '06:54', 1),
       ('evaelfie', '2022-01-01', '23:30', 2),
       ('karim', '2022-01-02', '18:32', 2),
       ('naruto', '2022-01-01', '12:01', 2),
       ('terminator', '2022-01-01', '21:30', 2),
       ('drumfred', '2022-01-01', '22:15', 2),
       ('meganfox', '2022-01-02', '18:19', 2),
       ('tyuuki', '2022-01-02', '16:38', 2),
       ('tyuuki', '2023-02-23', '14:13', 1),
       ('tyuuki', '2023-02-27', '14:31', 2),
       ('evaelfie', now()::date, '8:00', 1),
       ('evaelfie', now()::date, '12:00', 2),
       ('drumfred', now()::date, '8:00', 1),
       ('drumfred', now()::date, '15:00', 2),
       ('drumfred', now()::date, '15:30', 1),
       ('drumfred', now()::date, '19:00', 2),
       ('drumfred', now()::date, '19:30', 1),
       ('drumfred', now()::date, '23:00', 2),
       ('evaelfie', now()::date, '15:00', 1),
       ('evaelfie', now()::date, '23:30', 2),
       ('tyuuki', now()::date - interval '1 day', '08:00', 1),
       ('tyuuki', now()::date - interval '1 day', '10:00', 2),
       ('tyuuki', now()::date - interval '1 day', '10:30', 1),
       ('tyuuki', now()::date - interval '1 day', '12:00', 2),
       ('tyuuki', now()::date - interval '1 day', '12:10', 1),
       ('tyuuki', now()::date - interval '1 day', '12:20', 2),
       ('evaelfie', now()::date - interval '1 day', '10:00', 1),
       ('evaelfie', now()::date - interval '1 day', '14:00', 2),
       ('evaelfie', now()::date - interval '1 day', '15:00', 1),
       ('evaelfie', now()::date - interval '1 day', '23:30', 2),
       ('drumfred', now()::date - interval '3 day', '19:30', 1),
       ('drumfred', now()::date - interval '3 day', '23:00', 2),
       ('tyuuki', '2022-05-20', '08:00', 1),
       ('tyuuki', '2022-05-20', '12:12', 2),
       ('tyuuki', '2022-05-24', '20:00', 1),
       ('tyuuki', '2022-05-24', '23:50', 2),
       ('drumfred', '2022-10-12', '08:00', 1),
       ('drumfred', '2022-10-12', '12:12', 2),
       ('drumfred', '2022-10-13', '20:00', 1),
       ('drumfred', '2022-10-13', '23:50', 2),
       ('leeroy', '2022-02-20', '11:50', 1),
       ('leeroy', '2022-02-20', '13:50', 2);