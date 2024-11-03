-- 기존 테이블 삭제
DROP TABLE IF EXISTS lottery_result;

-- 기존 테이블에 새로운 컬럼을 추가하는 DDL
ALTER TABLE user_lottery_draw_ticket
    ADD COLUMN ranking INT DEFAULT NULL,  -- 로또 결과 순위, NULL은 결과가 아직 없는 상태
ADD COLUMN is_receive_reward BOOLEAN DEFAULT FALSE;  -- 보상 수령 여부, 기본값은 false
