DROP TABLE IF EXISTS users;


 CREATE TABLE users
 (
     id       SERIAL PRIMARY KEY,
     username VARCHAR(250) NOT NULL,
     password VARCHAR(250) NOT NULL,

     UNIQUE (username)
 );
