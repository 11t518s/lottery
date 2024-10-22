ALTER TABLE lottery_result
    ADD CONSTRAINT unique_draw_ticket_id UNIQUE (draw_ticket_id);