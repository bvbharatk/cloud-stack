select deployment_planner Planner, count(*) Count from service_offering
 where deployment_planner is not null
 group by deployment_planner;
 