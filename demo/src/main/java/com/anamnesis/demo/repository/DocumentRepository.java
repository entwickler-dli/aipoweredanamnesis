package com.anamnesis.demo.repository;

import com.anamnesis.demo.dao.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    boolean existsByDocumentType(String type);

    Optional<Document> findByDocumentType(String type);

}
