CREATE TABLE IF NOT EXISTS POSTS_TBL (
    post_id varchar(8) NOT NULL,
    profe_id varchar(6) NOT NULL,
    title varchar(200) default NULL,
    content varchar(3000) default NULL,
    status BOOLEAN default false,
    pinned BOOLEAN default false,
    post_photo varchar(200) default NULL,
    publish_date timestamp NULL DEFAULT NULL,
    update_date TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    created_date timestamp NOT NULL default CURRENT_TIMESTAMP,
    PRIMARY KEY (post_id)
);

CREATE TABLE IF NOT EXISTS PROFES_TBL (
    profe_id varchar(6) NOT NULL,
    profe_name varchar(20) NOT NULL,
    profe_rango TINYINT (1) default NULL,
    profe_status BOOLEAN default true,
    profe_photo varchar(200) default NULL,
    PRIMARY KEY (profe_id)
);

CREATE TABLE IF NOT EXISTS MEMBER_TBL (
    member_id varchar(6) NOT NULL,
    member_name varchar(20) NOT NULL,
    member_status BOOLEAN default true,
    member_photo varchar(200) default NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE IF NOT EXISTS SCHEDULE_TBL (
    event_id varchar(6) NOT NULL,
    profe_id varchar(6) NOT NULL,
    event_name varchar(150) NOT NULL,
    event_datetime timestamp NOT NULL,
    PRIMARY KEY (event_id)
);
