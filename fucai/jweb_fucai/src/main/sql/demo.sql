CREATE DATABASE IF NOT EXISTS demo default charset utf8 COLLATE utf8_general_ci;

create table samplemodel (
    id int unsigned not null auto_increment primary key,
    lastName varchar(40) not null,
    firstName varchar(40) not null,
    email varchar(80) not null,
    unique index subscriber_idx1 (email)
) engine = InnoDb;

insert into samplemodel(lastName, firstName, email) values ("James", "Gosling", "james.gosling@java.com");
insert into samplemodel(lastName, firstName, email) values ("Rod", "Johnson", "rod.johnson@spring.io");
insert into samplemodel(lastName, firstName, email) values ("Michael", "Monty", "michael.monty@mysql.com");

