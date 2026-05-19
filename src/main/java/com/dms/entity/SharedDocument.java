package com.dms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class SharedDocument {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Document document;

    @ManyToOne
    private User sharedWith;

    private LocalDateTime sharedAt;
    

	public SharedDocument() {
	}

	public SharedDocument(Long id, Document document, User sharedWith, LocalDateTime sharedAt) {
		super();
		this.id = id;
		this.document = document;
		this.sharedWith = sharedWith;
		this.sharedAt = sharedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public User getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(User sharedWith) {
		this.sharedWith = sharedWith;
	}

	public LocalDateTime getSharedAt() {
		return sharedAt;
	}

	public void setSharedAt(LocalDateTime sharedAt) {
		this.sharedAt = sharedAt;
	}
    
    

}
