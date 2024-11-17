package com.example.demo.service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${google.credentials.json}")
    private String credentialsJson;

    @PostConstruct
    public void init() throws IOException {

        System.out.println("***********************************PROJECT_ID_GCP es: ");
        System.out.println(projectId);

        System.out.println("***********************************GOOGLE_CREDENTIALS_JSON DESDE EL SERVICE CTM! es: ");
        System.out.println(credentialsJson);


        System.out.println("***********************************GOOGLE_CREDENTIALS_JSON es: ");
        System.out.println(System.getenv("GOOGLE_CREDENTIALS_JSON"));


        
        if (credentialsJson == null || credentialsJson.isEmpty()) {
            throw new IllegalStateException("Faltan las credenciales de Google Cloud en la propiedad google.credentials.json");
        }

        System.out.println("ahora va por el inputstream");

        System.out.println("Longitud de credentialsJson: " + credentialsJson.length());
        System.out.println("Contenido de credentialsJson: " + credentialsJson);


        try {
            byte[] jsonBytes = credentialsJson.getBytes(StandardCharsets.UTF_8);
            System.out.println("Bytes del JSON: " + Arrays.toString(jsonBytes));
        
            try (InputStream credentialsStream = new ByteArrayInputStream(jsonBytes)) {
                this.storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();
                System.out.println("Servicio de almacenamiento inicializado correctamente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar las credenciales de Google Cloud Storage", e);
        }
        

        
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
