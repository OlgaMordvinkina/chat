# Создать и запустить минио контейнер (название minio)
#docker run --name minio     --publish 9000:9000     --publish 9001:9001  --volume /home/olga/dev/data:/data   minio/minio:latest  server /data --console-address ":9001"

# Запустить существующий контейнер
docker start minio
# Запустить кейклок в докере
docker-compose -f AuthModule/docker-compose.yaml up -d
