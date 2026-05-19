package com.dms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dms.entity.Document;
import com.dms.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	private  DocumentRepository documentRepository;


	public String uploadDocument(MultipartFile file) throws IOException {

		// create uploads folder if not exists
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {

			Files.createDirectories(uploadPath);
		}

		// unique file name
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		// full path
		Path filePath = uploadPath.resolve(fileName);

		// save file in local folder
		Files.copy(file.getInputStream(), filePath);

		// save metadata in database
		Document document = new Document();

		document.setFileName(file.getOriginalFilename());

		document.setFileType(file.getContentType());

		document.setFileSize(file.getSize());

		document.setFilePath(filePath.toString());

		document.setUploadedAt(LocalDateTime.now());

		document.setDeleted(false);

		documentRepository.save(document);

		return "File Uploaded Successfully";
	}
}