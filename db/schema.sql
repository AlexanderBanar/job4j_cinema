create database cinema;

CREATE TABLE accounts (
                          id SERIAL PRIMARY KEY,
                          username VARCHAR NOT NULL,
                          email VARCHAR NOT NULL UNIQUE,
                          phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE tickets (
                         id SERIAL PRIMARY KEY,
                         session_id INT NOT NULL,
                         row INT NOT NULL,
                         cell INT NOT NULL,
                         account_id INT,
                         CONSTRAINT ticket_unique UNIQUE (session_id, row, cell)
);

insert into tickets(session_id, row, cell, account_id) values (1, 1, 1, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 1, 2, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 1, 3, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 2, 1, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 2, 2, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 2, 3, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 3, 1, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 3, 2, 0);
insert into tickets(session_id, row, cell, account_id) values (1, 3, 3, 0);