-- 기존 인덱스 삭제
DROP INDEX idx_lottery_result_round ON lottery_result;
DROP INDEX idx_lottery_result_ranking ON lottery_result;
DROP INDEX idx_lottery_result_uid ON lottery_result;

DROP INDEX idx_user_lottery_draw_ticket_numbers ON user_lottery_draw_ticket;
DROP INDEX idx_user_lottery_draw_ticket_bonus_number ON user_lottery_draw_ticket;
DROP INDEX idx_user_lottery_draw_ticket_round ON user_lottery_draw_ticket;
DROP INDEX idx_user_lottery_draw_ticket_uid ON user_lottery_draw_ticket;

DROP INDEX idx_user_lottery_mission_uid ON user_lottery_mission;
DROP INDEX idx_user_lottery_mission_created_at ON user_lottery_mission;
DROP INDEX idx_user_lottery_mission_mission_id ON user_lottery_mission;

-- lottery_result 테이블의 복합 인덱스 생성
CREATE INDEX idx_lottery_result_round_uid ON lottery_result (round, uid);
CREATE INDEX idx_lottery_result_round_ranking ON lottery_result (round, ranking);

-- user_lottery_draw_ticket 테이블의 복합 인덱스 생성
CREATE INDEX idx_user_lottery_draw_ticket_uid_round_id ON user_lottery_draw_ticket (uid, round, id);

-- user_lottery_mission 테이블의 복합 인덱스 생성
CREATE INDEX idx_user_lottery_mission_uid_created_at ON user_lottery_mission (uid, created_at);
CREATE INDEX idx_user_lottery_mission_uid_mission_id ON user_lottery_mission (uid, mission_id);
