delete
from compilations_events_collation;

delete
from compilations;

delete
from events;

delete
from users;

delete
from requests;

delete
from locations;

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