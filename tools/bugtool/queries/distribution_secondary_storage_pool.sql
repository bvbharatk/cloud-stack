select image_provider_name, count(*) from image_store
 group by image_provider_name;