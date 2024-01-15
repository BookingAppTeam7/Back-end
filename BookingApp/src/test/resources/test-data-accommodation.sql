-- Insert Location data
INSERT INTO Location (address, city, country, x, y, deleted) VALUES
    ('LocationAddress', 'LocationCity', 'LocationCountry', 1.234, 5.678, false);
-- Add more rows as needed


-- Insert TimeSlot data
INSERT INTO time_slot (id, start_date, end_date, deleted)
VALUES (6, '2023-02-10 10:59:00', '2023-02-15 10:59:00', false);


-- Insert PriceCard data
INSERT INTO price_card(id,time_slot_id, price, type, deleted) VALUES
    (1,6, 2000, 'PERGUEST', false);
-- Add more rows as needed

-- Insert Accommodation data
INSERT INTO accommodations (id,name, description, location_id, min_guests, max_guests, type, owner_id, status, cancellation_deadline, reservation_confirmation, deleted) VALUES
    (1,'ime', 'opis', 1, 2, 5, 'APARTMENT', 'owner', 'PENDING', 5, 'MANUAL', false);

