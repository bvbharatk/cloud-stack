select go.display_name, count(*) from vm_instance vi, vm_template vt, guest_os go
 where 
 vm_type = 'SecondaryStorageVm' 
 and vi.removed is null 
 and vi.vm_template_id = vt.id 
 and vt.guest_os_id = go.id
 group by  go.display_name;