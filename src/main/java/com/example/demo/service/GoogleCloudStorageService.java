package com.example.demo.service;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GoogleCloudStorageService {

    private final Storage storage;
    private final String bucketName;

    public GoogleCloudStorageService(
        @Value("${google.cloud.storage.bucket-name}") String bucketName) {
        // Se inicializa el cliente de almacenamiento de GCP con la configuración predeterminada de Spring
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(file.getBytes()));
        }
        return fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        return blob.getContent();
    }
}