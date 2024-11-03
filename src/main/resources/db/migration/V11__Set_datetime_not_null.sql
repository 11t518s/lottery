-- 테이블의 datetime 필드를 NOT NULL로 변경
ALTER TABLE lottery_mission
    MODIFY COLUMN created_at datetime NOT NULL;

ALTER TABLE user_lottery_mission
    MODIFY COLUMN created_at datetime NOT NULL;