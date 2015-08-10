select goc.name, count(*) Count from vm_template vt, guest_os go, guest_os_category goc
  where 
  vt.guest_os_id = go.id
  and go.category_id = goc.id
  group by goc.name;
  
  