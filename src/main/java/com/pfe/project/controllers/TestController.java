package com.pfe.project.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		System.out.println("public");
		return "Public Content.";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('CALL_CENTER') or hasRole('CC') or hasRole('MARKETING')")
	public String userAccess() {
	System.out.println("ana CALL CENTER");
	return "La liste des clients :";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('CC')")
	public String moderatorAccess() {
		System.out.println("ana CC");
		return "CC Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('MARKETING')")
	public String adminAccess() {
		System.out.println("ana marketing");
		return "Marketing Board.";
	}


	@GetMapping("/chart")
	public String portalReactHandler() {

		return "forward:./index.html";
	}
}
