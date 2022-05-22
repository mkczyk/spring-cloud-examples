# spring-cloud-examples

Spring Cloud Examples


## S3

Minio:

```
docker run -p 9000:9000 -p 9001:9001 -d --name minio -v /mnt/minio:/data minio/minio server /data --console-address ":9001"
```

Web console: http://localhost:9001/

Username: minioadmin

Password: minioadmin
