delete
from compilations_events_collation;

delete
from compilations;

delete
from locations;

delete
from events;

delete
from users;

delete
from requests;

delete
from categories;

alter table compilations
    alter column compilation_id restart with 1;

alter table users
    alter column user_id restart with 1;

alter table categories
    alter column category_id restart with 1;

alter table events
    alter column event_id restart with 1;

alter table requests
    alter column request_id restart with 1;


create table if not exists public.users
(
    user_id    integer generated by default as identity
        constraint users_pk
            primary key,
    user_email varchar(254) not null
        constraint users_pk_2
            unique,
    user_name  varchar(250) not null
);

create table if not exists public.categories
(
    category_id   integer generated by default as identity
        constraint categories_pk
            primary key,
    category_name varchar(50) not null
        constraint categories_name_key
            unique
);

create table if not exists public.events
(
    event_id                 integer generated by default as identity
        constraint events_pk
            primary key,
    event_annotation         varchar      not null,
    category_id              integer      not null
        constraint events_categories_category_id_fk
            references public.categories,
    event_created            timestamp    not null,
    event_description        varchar      not null,
    event_date               timestamp    not null,
    initiator_id             integer      not null
        constraint events_users_user_id_fk
            references public.users,
    event_paid               boolean      not null,
    event_participant_limit  integer      not null,
    event_published          timestamp,
    event_request_moderation boolean      not null,
    event_title varchar(120) not null,
    event_state              varchar(50)  not null
);

create table if not exists public.locations
(
    event_id     integer          not null
        constraint locations_pk
            primary key
        constraint locations_events_event_id_fk
            references public.events
            on delete cascade,
    location_lat double precision not null,
    location_lon double precision not null
);

create table if not exists public.requests
(
    request_id      integer generated by default as identity
        constraint requests_pk
            primary key,
    event_id        integer     not null
        constraint requests_events_event_id_fk
            references public.events
            on delete cascade,
    request_created timestamp   not null,
    requester_id    integer     not null
        constraint requests_users_user_id_fk
            references public.users
            on delete cascade,
    request_status  varchar(50) not null
);

create table if not exists public.compilations
(
    compilation_id     integer generated by default as identity
        constraint compilations_pk
            primary key,
    compilation_pinned boolean      not null,
    compilation_title  varchar(255) not null
);

create table if not exists public.compilations_events_collation
(
    compilation_id integer not null
        constraint compilations_events_collation_compilations_compilation_id_fk
            references public.compilations
            on delete cascade,
    event_id       integer
        constraint compilations_events_collation_events_event_id_fk
            references public.events
            on delete cascade
);