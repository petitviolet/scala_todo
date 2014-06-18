# Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE task (
    id integer NOT NULL DEFAULT nextval('task_id_seq'),
    label varchar(255),
    created datetime NOT NULL,
    modified datetime NOT NULL
);

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
    id integer DEFAULT nextval('user_id_seq') PRIMARY KEY,
    email varchar(255) NOT NULL UNIQUE,
    name  varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    created datetime NOT NULL,
    modified datetime NOT NULL
);

CREATE SEQUENCE user_task_id_seq;
CREATE TABLE user_task (
    id integer DEFAULT nextval('user_task_id_seq') PRIMARY KEY,
    user_id integer NOT NULL,
    task_id integer NOT NULL,
    created datetime NOT NULL,
    modified datetime NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE(user_id, task_id)
);

# --- !Downs

DROP TABLE if exists task;
DROP SEQUENCE if exists task_id_seq;

DROP TABLE if exists user;
DROP SEQUENCE if exists user_id_seq;

DROP TABLE if exists user_task;
DROP SEQUENCE if exists user_task_id_seq;