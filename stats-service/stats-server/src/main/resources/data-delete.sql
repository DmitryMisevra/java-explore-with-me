delete
from hits;

alter table hits
    alter column hit_id restart with 1;