# Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255)
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
    user_id    integer DEFAULT nextval('user_id_seq') PRIMARY KEY ,
    email      varchar(255) NOT NULL UNIQUE,
    name       varchar(255) NOT NULL,
    password   varchar(255) NOT NULL,
);

# --- !Downs

DROP TABLE task;
DROP SEQUENCE task_id_seq;

DROP TABLE if exists user;
DROP SEQUENCE if exists user_id_seq;