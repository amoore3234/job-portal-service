package com.portal.job_portal_service.controller;

import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "registration")
public class JobPortalController {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  @PostMapping(value = "/register")
  @Operation(summary = "Create user registration")
    @ApiResponses(value = {
      @ApiResponse(
        responseCode = "200",
        description = "User created successfully",
        content = @Content(
          mediaType = "text/plain",
          examples = @ExampleObject(
            value = "User registered successfully"
          )
        )
      )
    })
  public ResponseEntity<?> createUser(User user) {
    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    user.setCreatedTimestamp(ZonedDateTime.now());
    userService.createUser(user);

    return new ResponseEntity<>("User registered successfully", HttpStatusCode.valueOf(200));
  }
}
