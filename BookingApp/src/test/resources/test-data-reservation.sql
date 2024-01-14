INSERT INTO reservations
(id, accommodation_id, user_id, time_slot_id, status, number_of_guests, price, price_type)
VALUES
    (2, NULL, NULL, NULL, 'PENDING', 2, 100.0, 'PERGUEST'),
    (3, NULL, NULL, NULL, 'APPROVED', 2, 100.0, 'PERGUEST');

-- INSERT INTO accommodations (id,name, description, location_id, min_guests, max_guests, type, owner_id, status, cancellation_deadline, reservation_confirmation, deleted) VALUES
--     (1L,'ime', 'opis', 1L, 2, 5, 'APARTMENT', 'OWNER@gmail.com', 'PENDING', 5, 'MANUAL',false);
--
-- INSERT INTO location (id, address, city, country, x, y, deleted) VALUES
--     (1L, 'Test adresa', 'Test grad', 'Test drzava', 1.00, 4.00, false);
--
-- INSERT INTO users
-- (firstName, lastName, username, password, role, address, phoneNumber, status,
--  reservationRequestNotification, reservationCancellationNotification, ownerRatingNotification,
--  accommodationRatingNotification, ownerRepliedToRequestNotification, token, deleted, reported, favouriteAccommodations)
-- VALUES
--     ('GuestFirstName', 'GuestLastName', 'GUEST@gmail.com', 'guest', 'GUEST', 'Test adresa gost', '123456789', 'ACTIVE',
--      false, false, false, false, true, ' ', false, false, ' ');
--
--
-- INSERT INTO users
-- (firstName, lastName, username, password, role, address, phoneNumber, status,
--  reservationRequestNotification, reservationCancellationNotification, ownerRatingNotification,
--  accommodationRatingNotification, ownerRepliedToRequestNotification, token, deleted, reported, favouriteAccommodations)
-- VALUES
--     ('OwnerFirstName', 'OwnerLastName', 'OWNER@gmail.com', 'owner', 'OWNER', 'Test adresa owner', '123456789', 'ACTIVE',
--      false, true, true, true, false, ' ', false, false, ' ');
--
--
--
-- INSERT INTO time_slot (id, start_date, end_date, deleted)
-- VALUES (1L, '2024-01-15 08:00:00', '2024-01-17 12:00:00', false);
--
