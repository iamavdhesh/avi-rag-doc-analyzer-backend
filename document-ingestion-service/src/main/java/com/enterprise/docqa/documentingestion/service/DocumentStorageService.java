package com.enterprise.docqa.documentingestion.service;

import com.enterprise.docqa.documentingestion.model.DocumentEntity;
import com.enterprise.docqa.documentingestion.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Service
public class DocumentStorageService {

    private final Path storageRoot;
    private final DocumentRepository documentRepository;

    public DocumentStorageService(@Value("${storage.path}") String storagePath, DocumentRepository documentRepository) {
        this.storageRoot = Path.of(storagePath);
        this.documentRepository = documentRepository;
        try {
            Files.createDirectories(storageRoot);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create storage directory", e);
        }
    }

    public DocumentEntity storeFile(MultipartFile file) {
        var filename = StringUtils.cleanPath(file.getOriginalFilename());
        var storedFile = storageRoot.resolve("documents").resolve(Instant.now().toEpochMilli() + "_" + filename);
        try {
            Files.createDirectories(storedFile.getParent());
            file.transferTo(storedFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store document file", e);
        }

        var entity = new DocumentEntity(filename, "UPLOADED", storedFile.toString(), file.getContentType(), file.getSize(), Instant.now());
        return documentRepository.save(entity);
    }
}
