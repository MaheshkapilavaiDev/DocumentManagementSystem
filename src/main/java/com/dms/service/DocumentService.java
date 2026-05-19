package com.dms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dms.dto.DocumentResponseDTO;
import com.dms.dto.FolderResponseDTO;
import com.dms.dto.UserResponseDTO;
import com.dms.entity.Document;
import com.dms.entity.Folder;
import com.dms.entity.User;
import com.dms.repository.DocumentRepository;
import com.dms.repository.FolderRepository;
import com.dms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FolderRepository folderRepository;

	public String uploadDocument(MultipartFile file, Long folderId) throws IOException {

		// GET LOGGED-IN USER

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {

			Files.createDirectories(uploadPath);
		}

		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

		Path filePath = uploadPath.resolve(fileName);

		Files.copy(file.getInputStream(), filePath);

		Folder folder = null;

		if (folderId != null) {

			folder = folderRepository.findById(folderId).orElseThrow(() -> new RuntimeException("Folder not found"));
		}

		Document document1 = new Document();

		document1.setFileName(file.getOriginalFilename());

		document1.setFileType(file.getContentType());

		document1.setFileSize(file.getSize());

		document1.setFilePath(filePath.toString());

		document1.setUploadedAt(LocalDateTime.now());

		document1.setDeleted(false);

		document1.setOwner(user);

		document1.setFolder(folder);

		documentRepository.save(document1);

		Document document = new Document();

		document.setFileName(file.getOriginalFilename());

		document.setFileType(file.getContentType());

		document.setFileSize(file.getSize());

		document.setFilePath(filePath.toString());

		document.setUploadedAt(LocalDateTime.now());

		document.setDeleted(false);

		document.setOwner(user);

		documentRepository.save(document);

		return "File Uploaded Successfully";
	}

	public Page<DocumentResponseDTO> searchDocuments(String keyword, int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return documentRepository.findByFileNameContainingIgnoreCase(keyword, pageable).map(this::mapToDTO);
	}
	

	public Page<DocumentResponseDTO> getAllDocuments(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		return documentRepository.findByDeletedFalse(pageable).map(this::mapToDTO);
	}
	

	public List<DocumentResponseDTO> getDocumentsByFolder(Long folderId) {

		List<Document> documents = documentRepository.findByFolderIdAndDeletedFalse(folderId);

		return documents.stream().map(this::mapToDTO).toList();
	}
	

	private DocumentResponseDTO mapToDTO(Document document) {

		UserResponseDTO userDTO = new UserResponseDTO(document.getOwner().getId(), document.getOwner().getName(),
				document.getOwner().getEmail());

		FolderResponseDTO folderDTO = null;

		if (document.getFolder() != null) {

			folderDTO = new FolderResponseDTO(document.getFolder().getId(), document.getFolder().getName());
		}

		return new DocumentResponseDTO(

				document.getId(),

				document.getFileName(),

				document.getFileType(),

				document.getFileSize(),

				document.getUploadedAt(),

				userDTO,

				folderDTO);
	}
}