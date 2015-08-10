select vt.hypervisor_type, count(*) Count from vm_template vt
 group by vt.hypervisor_type;
