package com.example.demo.service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GoogleCloudStorageService {

    private Storage storage;
    private final String bucketName = "habitaciones"; // Nombre de tu bucket

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.credentials.location}")
    private Resource credentialsLocation;

    @PostConstruct
    public void init() throws IOException {
        try (InputStream credentialsStream = credentialsLocation.getInputStream()) {
            this.storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build()
                .getService();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Google Cloud Storage credentials", e);
        }
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        // Crear objeto BlobId para identificar el archivo en el bucket
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Subir el archivo al bucket
        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(file.getBytes()));
        }

        // Retornar el nombre del archivo o alguna otra información que necesites
        return fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        return blob.getContent();
    }
}
