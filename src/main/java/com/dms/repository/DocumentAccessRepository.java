package com.dms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.entity.DocumentAccess;

@Repository
public interface DocumentAccessRepository
        extends JpaRepository<DocumentAccess, Long> {

	List<DocumentAccess> findByDocumentId(Long documentId);
}