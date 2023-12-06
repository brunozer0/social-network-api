create database quarkus_social;

use quarkus_social;

create table Usuario (
id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
name varchar(100) not null,
age integer not null
);

create table Posts (
id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
post_text varchar(150) not null,
date_time datetime not null,
user_id BIGINT NOT NULL,

FOREIGN KEY (user_id) REFERENCES User(id)
)



create table Followers (
id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,

user_id BIGINT NOT NULL,
follower_id BIGINT NOT NULL,
FOREIGN KEY (user_id) REFERENCES User(id)
FOREIGN KEY (follower_id) REFERENCES User(id)

)

