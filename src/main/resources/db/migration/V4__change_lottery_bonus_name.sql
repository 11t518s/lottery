ALTER TABLE lottery_round
    CHANGE bonus_number bonus INT NOT NULL;

ALTER TABLE user_lottery_draw_ticket
    CHANGE bonus_number bonus INT NOT NULL;
