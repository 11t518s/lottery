-- 복합 인덱스 생성
-- user_lottery_draw_ticket 테이블의 복합 인덱스 생성
CREATE INDEX idx_user_lottery_draw_ticket_uid_round_id ON user_lottery_draw_ticket (id, uid, round);

-- user_lottery_mission 테이블의 복합 인덱스 생성
CREATE INDEX idx_user_lottery_mission_uid_created_at ON user_lottery_mission (uid, created_at);
CREATE INDEX idx_user_lottery_mission_uid_mission_id ON user_lottery_mission (uid, mission_id);
