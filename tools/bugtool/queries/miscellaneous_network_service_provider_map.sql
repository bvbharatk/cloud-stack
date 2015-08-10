select distinct service, provider from ntwk_offering_service_map where provider not in ('VirtualRouter', 'SecurityGroupProvider', 'InternalLbVm', 'VpcVirtualRouter');
