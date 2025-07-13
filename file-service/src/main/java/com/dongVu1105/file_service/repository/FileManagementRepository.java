package com.dongVu1105.file_service.repository;

import com.dongVu1105.file_service.entity.FileManagement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Optional;

@Repository
public interface FileManagementRepository extends MongoRepository<FileManagement, String> {
    Optional<FileManagement> findById (String id);
}
