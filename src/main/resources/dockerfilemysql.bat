docker rm -f bp-mysql
docker run --detach --name=bp-mysql --env="MYSQL_ROOT_PASSWORD=mypassword" --publish 6603:3306 mysql

#View Logs
#docker logs test-mysql
# Get Port Mapping
#docker inspect test-mysql
#Get Docker Machine IP Address[This is needed to Connect (MYSQL Workbench/My SQL Client )from Host ]
# docker-machine ip
