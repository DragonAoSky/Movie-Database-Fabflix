CREATE DATABASE IF NOT EXISTS moviedb;
USE moviedb;

CREATE TABLE movies(
    id VARCHAR(10) PRIMARY KEY,
    title VARCHAR(100) DEFAULT '',
    year INTEGER NOT NULL,
    director VARCHAR(100) DEFAULT ''
);

CREATE TABLE stars(
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) DEFAULT '',
    birthYear INTEGER 
);

CREATE TABLE stars_in_movies(
    starId VARCHAR(10) NOT NULL REFERENCES stars(id),
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id)
);

CREATE TABLE genres(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(32) DEFAULT ''
);

CREATE TABLE genres_in_movies(
    genreId INTEGER NOT NULL REFERENCES genres(id),
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id)
);

CREATE TABLE customers(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(50) DEFAULT '',
    lastName VARCHAR(50) DEFAULT '',
    ccId VARCHAR(20) REFERENCES creditcards(id),
    address VARCHAR(200) DEFAULT '',
    email VARCHAR(50) DEFAULT '',
    password VARCHAR(20) DEFAULT ''
);

CREATE TABLE employees(
    email varchar(50) primary key,
	password varchar(20) not null,
	fullname varchar(100)
);

CREATE TABLE customers_backup(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(50) DEFAULT '',
    lastName VARCHAR(50) DEFAULT '',
    ccId VARCHAR(20) REFERENCES creditcards(id),
    address VARCHAR(200) DEFAULT '',
    email VARCHAR(50) DEFAULT '',
    password VARCHAR(20) DEFAULT ''
);

CREATE TABLE sales(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    customerId integer NOT NULL REFERENCES customers(id),
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id),
    saleDate DATE NOT NULL
);

CREATE TABLE creditcards(
    id VARCHAR(20) PRIMARY KEY,
    firstName VARCHAR(50) DEFAULT '',
    lastName VARCHAR(50) DEFAULT '',
    expiration DATE NOT NULL
);

CREATE TABLE ratings(
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id),
    rating FLOAT NOT NULL,
    numVotes INTEGER NOT NULL
);

ALTER TABLE stars_in_movies ADD INDEX(starId);
ALTER TABLE stars_in_movies ADD INDEX(movieId);
ALTER TABLE genres_in_movies ADD INDEX(movieId);
ALTER TABLE ratings ADD INDEX(movieId);
ALTER TABLE ratings ADD INDEX(rating);
ALTER TABLE ratings ADD INDEX(numVotes);
-- ALTER TABLE `sales` ADD COLUMN `quantity` INTEGER NOT NULL DEFAULT '0';
-- ALTER TABLE `movies` add FULLTEXT(title);
ALTER TABLE employees MODIFY COLUMN password VARCHAR(128);
insert into employees(email,password,fullname) values('classta@email.edu','classta','TA CS122B');

-- CREATE view topmovies AS (select movies.id, movies.title, movies.year, movies.director, substring_index(group_concat(distinct genres.name SEPARATOR ','), ',', 3) as genres, ratings.rating 
--  from ratings, movies , genres, genres_in_movies
--  where (movies.id = ratings.movieId) AND (ratings.movieId = genres_in_movies.movieId) AND (genres_in_movies.genreId = genres.id) 
--  group by movies.id);

CREATE view topmovies AS (select * from
(select movies.id, movies.title, movies.year, movies.director, substring_index(group_concat(distinct genres.name order by genres.name SEPARATOR ','), ',', 3) as genres from movies , genres, genres_in_movies
where movies.id = genres_in_movies.movieId and genres_in_movies.genreId = genres.id group by movies.id) a 
LEFT JOIN
(select movies.id as tempid, ratings.rating from movies, ratings where movies.id = ratings.movieId group by movies.id)b on a.id = b.tempid);