package com.dms.dto;

import java.time.LocalDateTime;

public class DocumentResponseDTO {
	
	
	private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private LocalDateTime uploadedAt;

    private UserResponseDTO owner;

    private FolderResponseDTO folder;
    
    

	public DocumentResponseDTO() {
		super();
	}

	public DocumentResponseDTO(Long id, String fileName, String fileType, Long fileSize, LocalDateTime uploadedAt,
			UserResponseDTO owner, FolderResponseDTO folder) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.uploadedAt = uploadedAt;
		this.owner = owner;
		this.folder = folder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public UserResponseDTO getOwner() {
		return owner;
	}

	public void setOwner(UserResponseDTO owner) {
		this.owner = owner;
	}

	public FolderResponseDTO getFolder() {
		return folder;
	}

	public void setFolder(FolderResponseDTO folder) {
		this.folder = folder;
	}
    
    

}
