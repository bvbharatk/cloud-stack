select pool_type Type, count(*) Count from storage_pool
 group by pool_type;