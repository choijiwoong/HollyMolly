create table member
(
    innerId bigint generated by default as identity,
    name varchar(255),
    emailAddress varchar(255),
    password varchar(255),
    primary key (innerId)
)

--SHOW COLUMNS FROM `MEMBER`;
--DELETE * FROM MEMBER;