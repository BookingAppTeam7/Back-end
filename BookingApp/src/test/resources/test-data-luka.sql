INSERT INTO users (`first_name`, `last_name`, `username`, `password`, `role`, `address`, `phone_number`, `status`,
                   `reservation_request_notification`, `reservation_cancellation_notification`, `owner_rating_notification`,
                   `accommodation_rating_notification`, `owner_replied_to_request_notification`, `token`, `deleted`, `reported`, `favourite_accommodations`)
VALUES
    ('GuestFirstName', 'GuestLastName', 'TESTGOST1@gmail.com', '$2a$10$fmYJc8K1u42618Jm8D./Le15tVqYzqY6xFMQP/gfHnMAQXuZjL.lS', 2, 'Test adresa gost', '123456789', 0,
     false, false, false, false, true, ' ', false, false, '');


INSERT INTO accommodations (`id`, `name`, `description`, `location_id`, `min_guests`, `max_guests`, `type`, `owner_id`, `status`, `cancellation_deadline`, `reservation_confirmation`, `deleted`) VALUES
        (1,'ime1', 'opis1',  NULL, 2, 5, 'ROOM', 'OWNER@gmail.com', 'APPROVED', 5, 'MANUAL',false),
        (2,'ime2', 'opis3',  NULL, 2, 5, 'APARTMENT', 'OWNER@gmail.com', 'APPROVED', 5, 'MANUAL',false),
        (3,'ime3', 'opis3',  NULL, 2, 5, 'APARTMENT', 'OWNER@gmail.com', 'PENDING', 5, 'MANUAL',false);


INSERT INTO time_slot (`id`, `start_date`, `end_date`, `deleted`)
VALUES
    (1,'2024-01-27', '2024-02-15', false),
    (2,'2024-01-29', '2024-02-10', false),
    (3,'2024-02-28', '2024-03-30', false),
    (4,'2024-02-28', '2024-03-30', false),
    (5,'2024-01-26', '2024-01-29', false),
    (6,'2024-02-08', '2024-02-18', false),
    (7,'2024-02-08', '2024-02-18', false),
    (8,'2024-02-08', '2024-02-18', false),
    (9,'2024-02-03', '2024-02-07',false);

INSERT INTO price_card (`id`,`time_slot_id`, `price`, `type`, `deleted`)
VALUES (1,1, 150.0, 'PERGUEST',false);

INSERT INTO accommodations_prices (`accommodation_id`,`prices_id`)
VALUES (2,1);


INSERT INTO reservations
(`id`, `accommodation_id`, `user_id`, `time_slot_id`, `status`, `number_of_guests`, `price`, `price_type`)
VALUES
    (1, 2, 'TESTGOST1@gmail.com', 2, 'PENDING', 2, 100.0, 'PERUNIT'),
    (2, 2, NULL, NULL, 'PENDING', 2, 100.0, 'PERGUEST'),
    (3, 2, NULL, NULL, 'PENDING', 2, 100.0, 'PERGUEST'),
    (4, 2, NULL, NULL, 'PENDING', 2, 100.0, 'PERGUEST'),
    (5, 2, 'TESTGOST1@gmail.com', 3, 'PENDING', 2, 100.0, 'PERGUEST'),
    (6, 2, 'TESTGOST1@gmail.com', 4, 'PENDING', 2, 100.0, 'PERGUEST'),
    (7, 2, 'TESTGOST1@gmail.com', 5, 'PENDING', 2, 100.0, 'PERGUEST'),
    (8, 2, 'TESTGOST1@gmail.com', 6, 'PENDING', 2, 100.0, 'PERGUEST'),
    (9, 1, 'TESTGOST1@gmail.com', 7, 'PENDING', 2, 100.0, 'PERGUEST'),
    (10,2,'TESTGOST1@gmail.com',9,'APPROVED',3,100.0,'PERGUEST');

