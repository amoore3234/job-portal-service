package com.portal.job_portal_service.controller;

import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/portal")
public class JobPortalController {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @PostMapping(value = "/register")
  public ResponseEntity<?> createUser(User user) {
    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    user.setCreatedTimestamp(ZonedDateTime.now());
    userService.createUser(user);

    return new ResponseEntity<>("User registered successfully", HttpStatusCode.valueOf(200));
  }
}
