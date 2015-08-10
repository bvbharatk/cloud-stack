select goc.name, count(*) Count from vm_instance vi, guest_os go, guest_os_category goc
  where
  vi.guest_os_id = go.id
  and go.category_id = goc.id
  group by goc.name;
  
  