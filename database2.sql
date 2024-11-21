create database tamnhatthoc2;
use tamnhatthoc2;
create table user_tbl(
uid int primary key auto_increment,
username varchar(100) not null unique,
pw varchar(100) not null,
avatar varchar(100),
score float,
date_create timestamp default current_timestamp,
last_online datetime 
);
use tamnhatthoc2;
create table friend_tbl(
id int primary key auto_increment,
uid int,
uid2 int,
foreign key (uid) references user_tbl(uid),
foreign key(uid2) references user_tbl(uid)
);
use tamnhatthoc2;
create table match_tbl(
id int primary key auto_increment,
num_seed int not null,
num_type int not null,
time_match int,
date_play datetime,
room_id varchar(50)
);
use tamnhatthoc2;
create table user_match(
id int primary key auto_increment,
time_win int,
match_tblid int,
user_tbluid int,
foreign key (match_tblid) references match_tbl(id),
foreign key (user_tbluid) references user_tbl(uid)
);
