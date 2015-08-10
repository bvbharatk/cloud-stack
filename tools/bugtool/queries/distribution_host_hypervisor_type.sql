select hypervisor_type, count(*) from host
 where hypervisor_type is not null
 group by hypervisor_type;
