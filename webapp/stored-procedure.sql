USE moviedb;

DELIMITER $$
CREATE PROCEDURE add_new_star (IN starname varchar(100), IN boa varchar(100))
BEGIN
declare tempid int;
declare newid varchar(10);
declare newboa int;
IF(isnull(boa) = 1) then
	SET newboa = -1;
else
	SET newboa = (SELECT CAST(boa as SIGNED));
end if;
	SET tempid = (SELECT CAST((select substring(max(stars.id), 3) from stars) AS SIGNED));
    SET tempid = tempid + 1;
    SET newid = (select concat('nm',tempid));
    IF(newboa = -1) then
		insert into stars(id,name) values(newid,starname);
	else
		insert into stars(id,name,birthYear) values(newid,starname,newboa);
	end if;
    SELECT CONCAT("New Star with ID ",newid) as answer;
END
$$
DELIMITER ;
-- ----------------------------------------------------
DELIMITER $$
CREATE PROCEDURE add_new_movie (IN ntitle varchar(100), IN ndir varchar(100), IN nyear varchar(100),IN nstar varchar(100), IN ngenre varchar(100))
BEGIN
declare checkmovie varchar(10);
declare checkstar varchar(10);
declare checkSIM varchar(10);
declare checkgenre int;
declare newgid int;
declare newMid varchar(10);
declare newSid varchar(10);
declare tempid int;
declare star_status varchar(100);
declare genre_status varchar(100);
declare final_starid varchar(100);
declare final_genreid varchar(100);
SET checkmovie = (select movies.id from movies where movies.title = ntitle and movies.director = ndir and movies.year = nyear limit 1);
-- check if movie exist
IF(isnull(checkmovie) = 1) then
	SET tempid = (SELECT CAST((select substring(max(movies.id), 4) from movies) AS SIGNED));
    SET tempid = tempid + 1;
    SET newMid = (select concat('tt0',tempid));
	insert into movies(id,title,year,director) values(newMid,ntitle,nyear,ndir);
    -- start check stars
    SET checkstar = (select stars.id from stars where stars.name = nstar limit 1);
    IF(isnull(checkstar) = 1) then
		SET tempid = (SELECT CAST((select substring(max(stars.id), 3) from stars) AS SIGNED));
		SET tempid = tempid + 1;
		SET newSid = (select concat('nm',tempid));
        SET final_starid = newSid;
        SET star_status = (select concat('Generate a new star with id: ',final_starid, '. '));
		insert into stars(id,name) values(newSid,nstar);
        insert into stars_in_movies(starId,movieId) values(newSid,newMid);
	else
		-- star exist
        SET final_starid = checkstar;
        SET star_status = (select concat('Star already exist! Id is: ',final_starid, '. '));
        set checkSIM = (select stars_in_movies.starId from stars_in_movies where starId = checkstar and movieId = newMid);
        if(isnull(checkSIM) = 1) then
			insert into stars_in_movies(starId,movieId) values(checkstar,newMid);
		end if;
	end if;
	-- star check genre
    SET checkgenre = (select genres.id from genres where genres.name = ngenre limit 1);
    IF(isnull(checkgenre) = 1) then
		insert into genres(name) values(ngenre);
		SET newgid = (select genres.id from genres where genres.name = ngenre);
        SET final_genreid = newgid;
        SET genre_status = (select concat('Generate a new genre with id: ',final_genreid, '.'));
        insert into genres_in_movies(genreId,movieId) values(newgid,newMid);
	else
		-- genre exist
        SET final_genreid = checkgenre;
        SET genre_status = (select concat('Genre already exist! Id is: ',final_genreid, '.'));
        SET tempid = (select genres_in_movies.genreId from genres_in_movies where genreId = checkgenre and movieId = newMid);
		if(isnull(tempid) = 1) then
			insert into genres_in_movies(genreId,movieId) values(checkgenre,newMid);
		end if;
	end if;
    SELECT CONCAT("Generate a new movie with id: ", newMid, ". ", star_status, genre_status ) as answer;
else
    SELECT CONCAT("Movie already exist!") as answer;
end if;
END
$$
DELIMITER ;
-- ----------------------------------------------------
DELIMITER $$
CREATE PROCEDURE insert_new_star (IN starname varchar(100), IN boa varchar(100))
BEGIN
declare tempid int;
declare newid varchar(10);
declare newboa int;
declare checkstar varchar(10);
IF(isnull(boa) = 1) then
	SET newboa = -1;
else
	SET newboa = (SELECT CAST(boa as SIGNED));
end if;
	SET tempid = (SELECT CAST((select substring(max(stars.id), 3) from stars) AS SIGNED));
    SET tempid = tempid + 1;
    SET newid = (select concat('nm',tempid));
    IF(newboa = -1) then
		insert into stars(id,name) values(newid,starname);
        -- SELECT CONCAT("yes") as answer;
	else
		SET checkstar = (select id from stars where name = starname and birthYear = boa);
        IF(isnull(checkstar) = 1) then
			insert into stars(id,name,birthYear) values(newid,starname,newboa);
            -- SELECT CONCAT("yes") as answer;
		else
			SELECT CONCAT("no") as answer;
		end if;
	end if;
END
$$
DELIMITER ;

-- ----------------------------------------------------
DELIMITER $$
CREATE PROCEDURE insert_new_movie (IN ntitle varchar(100), IN ndir varchar(100), IN nyear varchar(100), IN ngenre varchar(100))
BEGIN
declare checkmovie varchar(10);
declare checkmoviewithg varchar(10);
declare checkgenre int;
declare newgid int;
declare newMid varchar(10);
declare tempid int;
SET checkmovie = (select movies.id from movies where movies.title = ntitle and movies.director = ndir and movies.year = nyear limit 1);
-- check if movie exist
IF(isnull(checkmovie) = 1) then
	SET tempid = (SELECT CAST((select substring(max(movies.id), 4) from movies) AS SIGNED));
    SET tempid = tempid + 1;
    SET newMid = (select concat('tt0',tempid));
	insert into movies(id,title,year,director) values(newMid,ntitle,nyear,ndir);
	-- start check genre
    SET checkgenre = (select genres.id from genres where genres.name = ngenre limit 1);
    IF(isnull(checkgenre) = 1) then
		insert into genres(name) values(ngenre);
		SET newgid = (select genres.id from genres where genres.name = ngenre);
        insert into genres_in_movies(genreId,movieId) values(newgid,newMid);
	else
		-- genre exist
        SET tempid = (select genres_in_movies.genreId from genres_in_movies where genreId = checkgenre and movieId = newMid);
		if(isnull(tempid) = 1) then
			insert into genres_in_movies(genreId,movieId) values(checkgenre,newMid);
		end if;
	end if;
    -- SELECT CONCAT("yes") as answer;
else
	SET checkmoviewithg = (select movies.id from movies,genres,genres_in_movies where movies.title = ntitle and movies.director = ndir and movies.year = nyear and genres_in_movies.movieId = movies.id and genres_in_movies.genreId = genreId and genres.name = ngenre limit 1);
	if(isnull(checkmoviewithg) = 1 ) then
		SET checkgenre = (select genres.id from genres where genres.name = ngenre limit 1);
        IF(isnull(checkgenre) = 1) then
			insert into genres(name) values(ngenre);
			SET newgid = (select genres.id from genres where genres.name = ngenre);
			insert into genres_in_movies(genreId,movieId) values(newgid,checkmovie);
		else
				insert into genres_in_movies(genreId,movieId) values(checkgenre,checkmovie);
		end if;
        -- SELECT CONCAT("yes") as answer;
	else
		SELECT CONCAT("Movie ",ntitle," already exist! And genre ",ngenre," already exist!") as answer;
	end if;
end if;
END
$$
DELIMITER ;

-- ---------------------------------------

DELIMITER $$
CREATE PROCEDURE CAST (IN mtitle varchar(100), IN starname varchar(100), IN dir varchar(100))
BEGIN
declare checkmovie varchar(10);
declare checkstar varchar(10);
declare checkSIM varchar(10);
declare checkdir varchar(100);

SET checkdir = (select id from movies where director = dir limit 1);
-- director does not exist!
IF (isnull(checkdir) = 1) then
	SELECT CONCAT("E1: Director ",dir," does not exist!") as answer;
else

	-- check movie exist
	SET checkmovie = (select id from movies where title = mtitle and director = dir limit 1);
	-- movie not found
	IF (isnull(checkmovie) = 1) then
		SELECT CONCAT("E2: Movie ",mtitle," does not exist!") as answer;
	else
		SET checkstar = (select id from stars where name = starname limit 1);
		-- star not found
		IF (isnull(checkstar) = 1) then
			SELECT CONCAT("E3: Star ",starname," does not exist!") as answer;
		else
			-- check SIM exisit
			SET checkSIM = (select starId from stars_in_movies where starId = checkstar and movieId = checkmovie limit 1);
			-- SIM not exisit
			IF(isnull(checkSIM) = 1) then
				insert into stars_in_movies(starId,movieId) values(checkstar,checkmovie);
				-- SELECT CONCAT("yes") as answer;
			else
				-- SIM exisit!
				SELECT CONCAT("E4: The relationship between movie ",mtitle," and star ",starname," already exist!") as answer;
			end if;
		end if;
	end if;
end if;
END
$$
DELIMITER ;

-- ------------------------------------
DELIMITER $$
CREATE PROCEDURE test (IN test varchar(100))
BEGIN
declare checkdir varchar(10);
SET checkdir = (select id from movies where director = test limit 1);
if(isnull(checkdir) = 1) then
	select concat('No');
end if;
END
$$
DELIMITER ;

-- ----------------------------
DELIMITER $$
CREATE PROCEDURE test2 (IN ntitle varchar(100), IN dir varchar(100))
BEGIN
declare checkid varchar(10);
SET checkid = (select id from movies where director = dir and title = ntitle limit 1);
if(isnull(checkid) = 1) then
	select concat('No');
end if;
END
$$
DELIMITER ;