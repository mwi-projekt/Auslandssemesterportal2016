FROM mysql:5.7

ADD ["db_dump.sql", "/docker-entrypoint-initdb.d/dump.sql"]

#RUN /etc/init.d/mysql start && \
 #       mysql -u root -p$MYSQL_ROOT_PASSWORD  -e "GRANT ALL PRIVILEGES ON *.* TO 'mwi'@'%' IDENTIFIED BY 'mwi2014';FLUSH PRIVILEGES;"

EXPOSE 3306