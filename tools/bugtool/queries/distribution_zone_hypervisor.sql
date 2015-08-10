select distinct data_center_id, hypervisor_type 
 from host where hypervisor_type is not null and hypervisor_type != 'None';
