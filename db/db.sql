create database quarkus_social;

use quarkus_social;

create table User (
id int auto_increment primary key not null,
name varchar(100) not null,
idade integer not null
);

create table Posts (
id int auto_increment primary key not null,
post_text varchar(150) not null,
data_post datetime default now(),
user_id int not null,

FOREIGN KEY (user_id) REFERENCES User(id)
)
