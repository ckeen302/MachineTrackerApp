-- Users Table
CREATE TABLE IF NOT EXISTS users (
                                     user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     username TEXT NOT NULL,
                                     password_hash TEXT NOT NULL,
                                     role TEXT NOT NULL
);

-- Machines Table
CREATE TABLE IF NOT EXISTS machines (
                                        machine_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        name TEXT NOT NULL,
                                        type TEXT,
                                        location TEXT,
                                        status TEXT,
                                        year INTEGER,
                                        tournament_eligible BOOLEAN,
                                        rules TEXT
);

-- Locations Table
CREATE TABLE IF NOT EXISTS locations (
                                         location_id INTEGER PRIMARY KEY AUTOINCREMENT,
                                         name TEXT NOT NULL,
                                         floor TEXT,
                                         room TEXT
);