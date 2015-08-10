 select pnsp.provider_name, count(*)  from physical_network_service_providers pnsp, physical_network pn, data_center dc
 where pn.data_center_id = dc.id
 and pnsp.physical_network_id = pn.id
 and pnsp.provider_name not in ('VirtualRouter','SecurityGroupProvider','VpcVirtualRouter', 'InternalLbVm', 'BaremetalPxeProvider')
 and pnsp.state = 'Enabled'
 group by pnsp.provider_name;
 