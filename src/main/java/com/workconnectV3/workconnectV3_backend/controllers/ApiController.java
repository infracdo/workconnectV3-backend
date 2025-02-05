package com.workconnectV3.workconnectV3_backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*") // change to front end domain
@RestController
@RequestMapping(path = "/api/wc")
public class ApiController {

	@GetMapping("/hi")
    public ResponseEntity<String> saySomething() {
		System.out.println("accessed api /");
        return ResponseEntity.ok("hi stranger! -wc backend");
    }  

    @GetMapping("/hello")
	@PreAuthorize("hasAnyAuthority('user')") 
	public String index(@AuthenticationPrincipal Jwt jwt) {
		System.out.println("accessed api /hello");
		return String.format("Hello, user %s! -wc backend", jwt.getClaimAsString("preferred_username"));
	}

	@GetMapping("/admin")
	@PreAuthorize("hasAuthority('test-workconnect-admin')") 
	public String admin(@AuthenticationPrincipal Jwt jwt) {
		System.out.println("accessed api /admin");
		return String.format("Hello, admin %s! -wc backend", jwt.getClaimAsString("preferred_username"));
	}
}
