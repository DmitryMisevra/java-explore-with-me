delete
from users;

alter table users
    alter column user_id restart with 1;