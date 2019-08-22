.read sp19data.sql

-- Q2
CREATE TABLE obedience AS
  SELECT seven, animal FROM students;

-- Q3
CREATE TABLE smallest_int AS
  SELECT time, smallest FROM students WHERE smallest > 2 ORDER BY smallest LIMIT 20;

-- Q4
CREATE TABLE matchmaker AS
  select s1.pet, s1.song, s1.color, s2.color from 
  	students as s1, students as s2 where
  	s1.pet = s2.pet and s1.song = s2.song and s1.time < s2.time;;
