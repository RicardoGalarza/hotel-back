package com.example.demo.service;

import java.io.IOException;
import java.nio.ByteBuffer;

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

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String bucketName = "imagenes-proyecto-hotel"; // Nombre de tu bucket

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        // Crear objeto BlobId para identificar el archivo en el bucket
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Subir el archivo al bucket
        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(file.getBytes()));
        }

        // Devuelve la URL pública del archivo
        return "https://storage.googleapis.com/imagenes-proyecto-hotel/" + bucketName + "/" + fileName;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        return blob.getContent();
    }
}