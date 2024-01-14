-- INSERT INTO users (firstName, lastName, username, password, role, address, phoneNumber, status, reservationRequestNotification, reservationCancellationNotification, ownerRatingNotification, accommodationRatingNotification, ownerRepliedToRequestNotification, token, deleted, reported, favouriteAccommodations)
-- VALUES ('Luka123', 'LastName', 'luka@gmail.com', 'password', 'USER', 'Address', 'PhoneNumber', 'ACTIVE', false, false, false, false, false, 'token', false, false, null);
-- Insert test data for the Review entity
INSERT INTO review (user_id, type, comment, grade, date_time, deleted, accommodation_id, owner_id, reported, status)
VALUES (null, null, 'Great experience!', 5, '2024-01-13T12:30:00', false, null, null, false, null);

-- INSERT INTO accommodation_photos (accommodation_accommodation_id, photo)
-- VALUES (2,'assets/images/apartment1.png'),
--        (2,'assets/images/apartment2.png'),
--        (2,'assets/images/apartment3.png'),
--        (2,'assets/images/apartment4.png'),
--        (2,'assets/images/apartment5.png'),
--        (3,'assets/images/apartment6.png'),
--        (3,'assets/images/apartment7.png');


-- INSERT INTO accommodation_taken_dates (accommodation_id, first_date, last_date)
-- VALUES (2, '2023-01-01', '2023-01-05'),
--        (2, '2023-02-01', '2023-02-05');
--
-- INSERT INTO accommodation_amenity (accommodation_id,amenity_name)
-- VALUES (2,'WIFI'),
--        (2, 'Parking');
--
-- INSERT INTO reservations (total_price, reservation_status, start_date, end_date, number_of_nights, accommodation_id, guest_id)
-- VALUES (200, 'CANCELLED', '2022-12-12', '2022-12-15', 3, 2, null),
--        (200, 'WAITING', '2023-10-10', '2023-10-15', 5, 2, null),
--        (100, 'REJECTED', '2023-01-01', '2023-01-03', 3, 2, null),
--        (50, 'ACCEPTED', '2024-01-10', '2024-01-12', 1, 2, null),
--        (150, 'ACCEPTED', '2024-01-25', '2024-01-28', 2, 2, null);
--
-- INSERT INTO accommodation_price (accommodation_id, price, start_date, end_date)
-- VALUES
--     (2, 3000, '2001-01-01', '2001-07-01'),
--     (2, 2000, '2001-01-01', '2001-08-01'),
--     (2, 4000, '2001-01-01', '2001-09-01');

