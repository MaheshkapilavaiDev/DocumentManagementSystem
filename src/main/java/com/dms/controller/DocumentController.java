package com.dms.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dms.entity.Document;
import com.dms.repository.DocumentRepository;
import com.dms.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

	@Autowired
	private DocumentService documentService;
	@Autowired
	private DocumentRepository documentRepository;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {

		return ResponseEntity.ok(documentService.uploadDocument(file));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException {

		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		Path path = Paths.get(document.getFilePath());

		Resource resource = new UrlResource(path.toUri());

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(document.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getFileName())
				.body(resource);
	}

	@GetMapping
	public ResponseEntity<Page<Document>> getAllDocuments(

			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity.ok(documentRepository.findByDeletedFalse(pageable));
	}

	@GetMapping("/search")
	public ResponseEntity<Page<Document>> searchDocuments(

			@RequestParam String keyword,

			@RequestParam(defaultValue = "0") int page,

			@RequestParam(defaultValue = "5") int size) {

		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity.ok(

				documentRepository.findByFileNameContainingIgnoreCase(keyword, pageable));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDocument(@PathVariable Long id) {

		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		document.setDeleted(true);

		documentRepository.save(document);

		return ResponseEntity.ok("Document Soft Deleted");
	}

	@PutMapping("/{id}/restore")
	public ResponseEntity<String> restoreDocument(@PathVariable Long id) {

		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		document.setDeleted(false);

		documentRepository.save(document);

		return ResponseEntity.ok("Document Restored");
	}
}
