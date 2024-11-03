CREATE TABLE lottery_mission
(
    id                     BIGINT AUTO_INCREMENT NOT NULL,
    name                   VARCHAR(255) NULL,
    max_coin_amount        INT    NOT NULL,
    daily_repeatable_count INT    NOT NULL,
    type                   VARCHAR(255) NULL,
    enabled                BIT(1) NOT NULL,
    created_at             datetime NULL,
    CONSTRAINT pk_lottery_mission PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE lottery_result
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    round  INT    NOT NULL,
    ranking INT    NOT NULL,
    uid    BIGINT NOT NULL,
    CONSTRAINT pk_lottery_result PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE INDEX idx_lottery_result_round ON lottery_result (round);
CREATE INDEX idx_lottery_result_ranking ON lottery_result (ranking);
CREATE INDEX idx_lottery_result_uid ON lottery_result (uid);

CREATE TABLE lottery_round
(
    id           INT NOT NULL,
    round        INT NOT NULL,
    numbers      TEXT NULL,
    bonus_number INT NULL,
    CONSTRAINT pk_lottery_round PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE user
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE user_lottery_draw_ticket
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    numbers      TEXT NULL,
    bonus_number INT NOT NULL,
    round        INT NOT NULL,
    uid           BIGINT NOT NULL,
    CONSTRAINT pk_user_lottery_draw_ticket PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


CREATE TABLE user_lottery_info
(
    uid         BIGINT NOT NULL,
    total_score INT    NOT NULL,
    CONSTRAINT pk_user_lottery_info PRIMARY KEY (uid)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE user_lottery_mission
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    mission_id BIGINT NOT NULL,
    amount     INT    NOT NULL,
    created_at datetime NULL,
    uid        BIGINT NOT NULL,
    CONSTRAINT pk_user_lottery_mission PRIMARY KEY (id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;