select  hv.data_center_name Zone, hv.pod_name Pod, 
   hv.cluster_name Cluster, 
   hv.name Host, hv.hypervisor_type Hypervisor, 
   hv.type Type, hv.status Status 
   from host_view hv 
   order by hv.data_center_id, hv.pod_id, hv.cluster_id, hv.id;
