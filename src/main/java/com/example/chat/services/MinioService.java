package com.example.chat.services;

import java.util.List;

public interface MinioService {

    String putFile(String nameBucket, String base64Image);

    void removeAllFiles(String nameBucket);

    void removeFile(String nameBucket, String objectName);

    String getFile(String nameBucket, String nameFile);

    List<String> getFiles(String nameBucket);

    List<String> getFilesByNames(String nameBucket, List<String> objectNames);
}
