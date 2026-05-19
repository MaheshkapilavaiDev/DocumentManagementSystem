package com.dms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Page<Document> findByDeletedFalse(Pageable pageable);

    Page<Document> findByFileNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
    
    List<Document> findByFolderIdAndDeletedFalse(Long folderId);
}