package com.dms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.entity.SharedDocument;

@Repository
public interface SharedDocumentRepository extends JpaRepository<SharedDocument, Long>{

}
