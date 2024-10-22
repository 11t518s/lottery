ALTER TABLE user_lottery_draw_ticket
    ADD created_at datetime NOT NULL;

ALTER TABLE lottery_result
    ADD draw_ticket_id BIGINT NOT NULL,
    ADD is_receive_reward BIT(1) NOT NULL;
