select do.use_local_storage, count(*) 
 from volumes v, disk_offering do 
 where v.disk_offering_id = do.id
 and v.volume_type = 'ROOT'
 group by do.use_local_storage;
 