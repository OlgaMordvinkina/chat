package com.example.chat.services.impl;

import com.example.chat.services.MinioService;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private static final int DEFAULT_PART_SIZE = 5 * 1024 * 1024;  // 5 MB
    private final MinioClient minioClient;

    @SneakyThrows
    public void createBucket(String nameBucket) {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(nameBucket).build());

        if (!bucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(nameBucket)
                            .build());
        }
    }

    @SneakyThrows
    @Override
    public String putFile(String nameBucket, String base64Image) {
        String nameFile = UUID.randomUUID().toString();
        byte[] imageBytes = Base64.getDecoder().decode(base64Image.replaceFirst("^data:image/[^;]+;base64,", ""));
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(nameBucket).build());

            if (!bucketExists) {
                createBucket(nameBucket);
            }

            if (nameBucket.startsWith("user") || nameBucket.startsWith("chat")) {
                removeAllFiles(nameBucket);
            }

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(nameBucket)
                    .object(nameFile)
                    .stream(inputStream, imageBytes.length, DEFAULT_PART_SIZE)
                    .build());
        }
        return nameFile;
    }

    @SneakyThrows
    @Override
    public void removeAllFiles(String nameBucket) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(nameBucket)
                .build());
        for (Result<Item> objectResult : results) {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(nameBucket)
                    .object(objectResult.get().objectName())
                    .build());
        }
    }

    @SneakyThrows
    @Override
    public void removeFile(String nameBucket, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(nameBucket)
                    .object(objectName)
                    .build());
        } catch (MinioException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    @SneakyThrows
    public String getUrlFiles(String nameBucket, String nameFile) {
        if (nameBucket != null && nameFile != null) {
            int oneHourInSeconds = 3600;
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(nameBucket)
                    .object(nameFile)
                    .expiry(oneHourInSeconds)
                    .method(Method.GET)
                    .build());
        }
        return null;
    }
}
