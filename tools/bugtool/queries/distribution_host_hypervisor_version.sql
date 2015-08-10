select hypervisor_type, REPLACE(hypervisor_version, \" \",\"\"), count(*) from host
 where hypervisor_type is not null
 group by hypervisor_type, REPLACE(hypervisor_version, \" \",\"\");
 
