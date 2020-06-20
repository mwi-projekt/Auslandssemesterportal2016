# Build

- docker-compose pull
- docker-compose build
- docker-compose up -d

# Dev Deployment
mvn tomcat7:deploy -P dev

# Save db state for next local deployment (save dump)
When you change something in the db, we can save the state to our files so that we can use that state when we start the project next time. You only need to run the following command while the containers run.

In detail, this command saves the current SQL Dump for mysql (mwi & mwi_v2) from the running docker-environment. This dump will be automatically loaded, when you start the project next time. 

##### Command:
docker exec -i auslandssemesterportal2016_mwi_mysql_1 mysqldump -uroot -pmwi2014 --databases mysql mwi mwi_v1 --skip-comments > ./docker/mysql/v2_dump/v2_dump.sql