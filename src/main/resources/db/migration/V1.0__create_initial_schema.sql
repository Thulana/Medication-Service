CREATE TABLE IF NOT EXISTS medication(
    id VARCHAR(20) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    released_date LONG
);

CREATE TABLE IF NOT EXISTS disease(
    id VARCHAR(20) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS medication_disease(
    medication_id VARCHAR(20) NOT NULL,
    disease_id VARCHAR(20) NOT NULL
);

CREATE VIRTUAL TABLE IF NOT EXISTS medication_search_index USING FTS4(
    medication_id VARCHAR(20) NOT NULL PRIMARY KEY,
    content TEXT
);


