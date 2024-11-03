-- 테이블 수정: TEXT 필드를 VARCHAR로 변경, VARCHAR도 너무 길지 않은 것들은 줄이는 방향으로 수정
ALTER TABLE lottery_mission
    MODIFY COLUMN name VARCHAR(100),
    MODIFY COLUMN type VARCHAR(50);

ALTER TABLE lottery_round
    MODIFY COLUMN numbers VARCHAR(100);

ALTER TABLE user
    MODIFY COLUMN name VARCHAR(100);

ALTER TABLE user_lottery_draw_ticket
    MODIFY COLUMN numbers VARCHAR(100);