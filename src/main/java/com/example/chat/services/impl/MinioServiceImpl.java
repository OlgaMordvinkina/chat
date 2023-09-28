package com.example.chat.services.impl;

import com.example.chat.services.MinioService;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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
    public String getFile(String nameBucket, String nameFile) {
        if (nameFile != null) {
            try (InputStream stream =
                         minioClient.getObject(GetObjectArgs
                                 .builder()
                                 .bucket(nameBucket)
                                 .object(nameFile)
                                 .build())) {
                byte[] fileBytes = IOUtils.toByteArray(stream);
                return Base64.getEncoder().encodeToString(fileBytes);
            } catch (MinioException e) {
                e.getStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    @SneakyThrows
    public List<String> getFiles(String nameBucket) {
        List<String> images = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(nameBucket).build());
            for (Result<Item> result : results) {
                try (InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(nameBucket).object(result.get().objectName()).build())) {
                    byte[] fileBytes = IOUtils.toByteArray(stream);
                    String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                    images.add("data:image/jpg;base64," + base64Image);
                }
            }
        } catch (MinioException e) {
            return images;
        }
        return images;
    }
}
