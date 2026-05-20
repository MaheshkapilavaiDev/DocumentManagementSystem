package com.dms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dms.dto.DocumentResponseDTO;
import com.dms.dto.FolderResponseDTO;
import com.dms.dto.UserResponseDTO;
import com.dms.entity.Document;
import com.dms.entity.DocumentAccess;
import com.dms.entity.Folder;
import com.dms.entity.SharedDocument;
import com.dms.entity.User;
import com.dms.repository.DocumentAccessRepository;
import com.dms.repository.DocumentRepository;
import com.dms.repository.FolderRepository;
import com.dms.repository.SharedDocumentRepository;
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

	@Autowired
	private SharedDocumentRepository sharedDocumentRepo;
	
	@Autowired
	private DocumentAccessRepository documentAccessRepo;

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

		
		Document document = new Document();

		document.setFileName(file.getOriginalFilename());

		document.setFileType(file.getContentType());

		document.setFileSize(file.getSize());

		document.setFilePath(filePath.toString());

		document.setUploadedAt(LocalDateTime.now());

		document.setDeleted(false);

		document.setOwner(user);
		
		document.setFolder(folder);


		documentRepository.save(document);
		
		saveAccessHistory(
		        user,
		        document,
		        "UPLOAD");

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

	public String shareDocument(Long documentId, Long userId) {

		Document document = documentRepository.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document nit Found"));

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Document not Found"));

		SharedDocument sharedDocument = new SharedDocument();
		sharedDocument.setDocument(document);
		sharedDocument.setSharedWith(user);
		sharedDocument.setSharedAt(LocalDateTime.now());

		sharedDocumentRepo.save(sharedDocument);
		
		saveAccessHistory(
		        user,
		        document,
		        "SHARE");

		return "Document Shared Successfully";
	}

	public ResponseEntity<Resource> downloadDocument(Long id) throws IOException {

		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));
		
		Authentication authentication =
		        SecurityContextHolder
		                .getContext()
		                .getAuthentication();

		String email =
		        authentication.getName();

		User user =
		        userRepository.findByEmail(email)
		        .orElseThrow(() ->
		                new RuntimeException(
		                        "User not found"));
		
		saveAccessHistory(
	            user,
	            document,
	            "DOWNLOAD");

		Path path = Paths.get(document.getFilePath());

		Resource resource = new UrlResource(path.toUri());

		return ResponseEntity.ok()

				.contentType(MediaType.parseMediaType(document.getFileType()))

				.header(HttpHeaders.CONTENT_DISPOSITION,

						"attachment; filename=\"" + document.getFileName() + "\"")

				.body(resource);
	}
	
	public List<DocumentAccess>
	getDocumentHistory(Long documentId) {

	    return documentAccessRepo
	            .findByDocumentId(documentId);
	}
	
	private void saveAccessHistory(
	        User user,
	        Document document,
	        String action) {

	    DocumentAccess access =
	            new DocumentAccess();

	    access.setUser(user);

	    access.setDocument(document);

	    access.setAction(action);

	    access.setAccessedAt(
	            LocalDateTime.now());

	    documentAccessRepo.save(access);
	}
	
	public String deleteDocument(Long id) {

	    // get logged-in user

	    Authentication authentication =
	            SecurityContextHolder
	                    .getContext()
	                    .getAuthentication();

	    String email =
	            authentication.getName();

	    User user =
	            userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "User not found"));


	    Document document =
	            documentRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "Document not found"));


	    document.setDeleted(true);

	    documentRepository.save(document);


	    saveAccessHistory(
	            user,
	            document,
	            "DELETE");

	    return "Document Deleted Successfully";
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