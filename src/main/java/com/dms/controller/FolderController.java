package com.dms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.entity.Folder;
import com.dms.service.FolderService;

@RestController
@RequestMapping("/folders")
public class FolderController {

	@Autowired
	private FolderService folderService;

	@PostMapping("/create")
	public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) {

		return ResponseEntity.ok(folderService.createFolder(folder.getName()));
	}
}
