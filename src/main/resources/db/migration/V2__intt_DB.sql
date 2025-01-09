create sequence groups_seq start 1 increment 50;

create sequence students_seq start 1 increment 50;

create table groups (
    recruitment_year integer not null,
    id bigint not null,
    department varchar(255),
    name varchar(255) not null unique,
    primary key (id)
);

create table students (
    birth_date date not null,
    sex smallint not null check (sex between 0 and 1),
    status smallint not null check (status between 0 and 2),
    group_id bigint,
    id bigint not null,
    firstname varchar(255) not null,
    patronymic varchar(255),
    surname varchar(255) not null,
    primary key (id)
);

alter table if exists students
    add constraint FKmsev1nou0j86spuk5jrv19mss
    foreign key (group_id) references groups