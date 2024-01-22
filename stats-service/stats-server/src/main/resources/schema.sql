-- delete
-- from hits;
--
-- alter table hits
--     alter column hit_id restart with 1;


create table if not exists hits
(
    hit_id        integer generated by default as identity
        constraint hits_pk
            primary key,
    app_name      varchar(50)  not null,
    app_uri       varchar(200) not null,
    user_ip       varchar(50)  not null,
    hit_timestamp timestamp    not null
);