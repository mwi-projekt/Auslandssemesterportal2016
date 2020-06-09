# Build

- docker-compose pull
- docker-compose build
- docker-compose up -d

# Dev Deployment
mvn tomcat7:deploy -P dev

# Create new SQL Dump for mysql, mwi & mwi_v1 from running docker-environment for next restart
docker exec -i auslandssemesterportal2016_mwi_mysql_1 mysqldump -uroot -pmwi2014 --databases mysql mwi mwi_v1 --skip-comments > ./docker/mysql/v2_dump/v2_dump.sql