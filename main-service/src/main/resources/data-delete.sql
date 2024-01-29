delete
from users;

delete
from categories;

alter table users
    alter column user_id restart with 1;

alter table categories
    alter column category_id restart with 1;