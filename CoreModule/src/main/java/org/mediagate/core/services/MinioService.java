package org.mediagate.core.services;

public interface MinioService {

    String putFile(String nameBucket, String base64Image);

    void removeAllFiles(String nameBucket);

    void removeFile(String nameBucket, String objectName);

    String getUrlFiles(String nameBucket, String nameFile);
}
