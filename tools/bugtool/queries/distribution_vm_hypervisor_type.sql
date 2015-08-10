select Hypervisor_type, count(*) from vm_instance
  group by Hypervisor_type;

  