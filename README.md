# spring-cloud-examples

Spring Cloud Examples

Ports:
- orders-service: 7771
- invoices-service: 7772
- email-service: 7773
- eureka-server: 8861
- config-server: 8862
- admin-server: 8863
- minio-s3-example: 7781
- aws-s3-example: 7782
- cloud-s3-example: 7783
- Minio web console: 9001

## S3

Minio:

```
docker run -p 9000:9000 -p 9001:9001 -d --name minio -v /mnt/minio:/data minio/minio server /data --console-address ":9001"
```

Web console: http://localhost:9001/

Username: minioadmin

Password: minioadmin

## Eureka

Eureka server: http://localhost:8861/

# Config

Example order-service remote configuration: http://localhost:8862/orders-service/default

# Admin

Admin web: http://localhost:8863/wallboard
