INSERT INTO users (`first_name`, `last_name`, `username`, `password`, `role`, `address`, `phone_number`, `status`,
                   `reservation_request_notification`, `reservation_cancellation_notification`, `owner_rating_notification`,
                   `accommodation_rating_notification`, `owner_replied_to_request_notification`, `token`, `deleted`, `reported`, `favourite_accommodations`)
VALUES
    ('GuestFirstName', 'GuestLastName', 'TESTGOST1@gmail.com', 'guest', 2, 'Test adresa gost', '123456789', 0,
     false, false, false, false, true, ' ', false, false, ' '),
    ('ADMINFirstName', 'ADMINLastName', 'ADMIN@gmail.com', '$2a$10$o3OdXouxWgXvKVCLRzTOau8rRNKZKp9E.MuIsGjERivCMvNagaHkK', 2, 'Test adresa ADMIN', '123456789', 0,
    false, false, false, false, true, ' ', false, false, ' '),
    ('GOSTFirstName', 'GOSTLastName', 'GOST@gmail.com', '$2a$10$9CSCJqvrXWp698qyz1OWSOdfcg.D94cm.kL9yJwWZB30Md8HF4vG.', 1, 'Test adresa GOST', '123456789', 0,
    false, false, false, false, true, ' ', false, false, ' '),
    ('OWNERFirstName', 'OWNERLastName', 'OWNER@gmail.com', '$2a$10$6reNUQ0cl3UgNVG1Po8ABuOIgw048kyoxAktD.EbIw47xlCtjEnHi', 0, 'Test adresa OWNER', '123456789', 0,
    false, false, false, false, true, ' ', false, false, ' ');

INSERT INTO accommodations (`id`, `name`, `description`, `location_id`, `min_guests`, `max_guests`, `type`, `owner_id`, `status`, `cancellation_deadline`, `reservation_confirmation`, `deleted`) VALUES
    (1,'ime1', 'opis1',  NULL, 2, 5, 'ROOM', 'OWNER@gmail.com', 'APPROVED', 5, 'MANUAL',false),
    (2,'ime2', 'opis2',  NULL, 2, 5, 'APARTMENT', 'OWNER@gmail.com', 'APPROVED', 5, 'MANUAL',false),
    (3,'ime3', 'opis3',  NULL, 2, 5, 'APARTMENT', 'OWNER@gmail.com', 'APPROVED', 5, 'MANUAL',false),
    (4,'ime4', 'opis4',  NULL, 2, 5, 'APARTMENT', 'OWNER@gmail.com', 'APPROVED', 5, 'MANUAL',false);


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
    (9,'2024-02-08', '2024-02-18', false),
    (10,'2024-02-08', '2024-02-18', false),
    (11,'2024-02-08', '2024-02-18', false),
    (12,'2024-02-08', '2024-02-18', false),
    (13,'2024-02-08', '2024-02-18', false),
    (14,'2024-02-10', '2024-02-17', false);

INSERT INTO price_card (`id`,`time_slot_id`, `price`, `type`, `deleted`)
VALUES (1,1, 150.0, 'PERGUEST',false),
        (3,9, 170.0, 'PERGUEST',false);

INSERT INTO accommodations_prices (`accommodation_id`,`prices_id`)
VALUES (2,1),
       (3,3);


INSERT INTO reservations
(`id`, `accommodation_id`, `user_id`, `time_slot_id`, `status`, `number_of_guests`, `price`, `price_type`)
VALUES
    (1, 3, 'TESTGOST1@gmail.com', 9, 'PENDING', 2, 100.0, 'PERUNIT'),
    (2, 4, NULL, 10, 'PENDING', 2, 100.0, 'PERGUEST'),
    (3, 4, NULL, 11, 'APPROVED', 2, 100.0, 'PERGUEST'),
    (4, 2, NULL, 12, 'APPROVED', 2, 100.0, 'PERGUEST'),
    (5, 2, 'TESTGOST1@gmail.com', 3, 'PENDING', 2, 100.0, 'PERGUEST'),
    (6, 2, 'TESTGOST1@gmail.com', 4, 'APPROVED', 2, 100.0, 'PERGUEST'),
    (7, 2, 'TESTGOST1@gmail.com', 5, 'PENDING', 2, 100.0, 'PERGUEST'),
    (8, 2, 'TESTGOST1@gmail.com', 6, 'PENDING', 2, 100.0, 'PERGUEST'),
    (9, 1, 'TESTGOST1@gmail.com', 7, 'PENDING', 2, 100.0, 'PERGUEST'),
    (10, 1, 'TESTGOST1@gmail.com', 13, 'PENDING', 2, 100.0, 'PERGUEST'),
    (11, 3, 'TESTGOST1@gmail.com', 14, 'PENDING', 2, 100.0, 'PERGUEST');

