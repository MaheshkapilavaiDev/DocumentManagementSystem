package com.dms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dms.entity.Folder;
import com.dms.entity.User;
import com.dms.repository.FolderRepository;
import com.dms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FolderService {

	@Autowired
	private FolderRepository folderRepository;

	@Autowired

	private UserRepository userRepository;

	public Folder createFolder(String folderName) {

		// get logged-in user

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		// create folder

		Folder folder = new Folder();

		folder.setName(folderName);

		folder.setOwner(user);

		return folderRepository.save(folder);
	}
}
