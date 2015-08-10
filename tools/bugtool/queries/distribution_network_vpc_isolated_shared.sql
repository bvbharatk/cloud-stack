select guest_type, vpc_id, count(*) from networks
 where guest_type is not null
 group by guest_type, (vpc_id - vpc_id);
