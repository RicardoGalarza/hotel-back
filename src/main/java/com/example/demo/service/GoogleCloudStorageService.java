package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class GoogleCloudStorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String bucketName = "imagenes-proyecto-hotel"; // Nombre de tu bucket

    // Método para subir un archivo
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        // Crear archivo temporal
        Path tempFile = Files.createTempFile(fileName, null);
        file.transferTo(tempFile.toFile());

        // Subir el archivo a Google Cloud Storage
        BlobId blobId = BlobId.of(bucketName, "habitacion-1/" + fileName);  // "habitacion-1/" es el directorio
        Blob blob = storage.create(
                BlobInfo.newBuilder(blobId).build(),
                Files.readAllBytes(tempFile)
        );

        // Eliminar el archivo temporal
        Files.delete(tempFile);

        // Retornar la URL pública del archivo subido
        return "https://storage.googleapis.com/" + bucketName + "/" + blob.getName();
    }

    // Método para descargar un archivo desde GCP
    public byte[] downloadFile(String fileName) throws IOException {
        BlobId blobId = BlobId.of(bucketName, "habitacion-1/" + fileName); // Asegúrate de que el nombre del archivo sea correcto
        Blob blob = storage.get(blobId);

        if (blob == null) {
            throw new IOException("El archivo no existe en el bucket: " + fileName);
        }

        // Retornar los bytes del archivo
        return blob.getContent();
    }
}