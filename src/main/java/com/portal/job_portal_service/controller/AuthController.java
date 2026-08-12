package com.portal.job_portal_service.controller;

import com.portal.job_portal_service.model.LoginRequest;
import com.portal.job_portal_service.model.LoginResponse;
import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.service.JwtService;
import com.portal.job_portal_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "User authentication and registration")
public class AuthController {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserService userService;

  @PostMapping(value = "/register")
  @Operation(summary = "Create user registration")
    @ApiResponses(value = {
      @ApiResponse(
        responseCode = "204",
        description = "User created successfully",
        content = @Content(
          mediaType = "text/plain",
          examples = @ExampleObject(
            value = "User registered successfully"
          )
        )
      )
    })
  public ResponseEntity<?> createUser(@RequestBody User user) {
    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    user.setCreatedTimestamp(ZonedDateTime.now());
    userService.createUser(user);

    return new ResponseEntity<>("User registered successfully", HttpStatusCode.valueOf(200));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> userLogin(
      @RequestBody LoginRequest request
  ) {
      try {
        Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              request.getUsername(),
              request.getUserPassword()
          )
        );
        String storageDirectoryPath = "/app/uploads/";
        boolean hasUploadedResume = userService.checkUserResumeExists(request.getUsername(), storageDirectoryPath);
        boolean isFirstTimeUser = !hasUploadedResume;
        String token = jwtService.generateToken(auth.getName());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setConfirmFirstTimeLogin(isFirstTimeUser);

        return ResponseEntity.ok(response);
      } catch (BadCredentialsException e) {
        log.error("Authentication failed: " + e.getMessage());
        return ResponseEntity.status(401).build();
      } catch (Exception e) {
        log.error("An unexpected error occurred: " + e.getMessage());
          return ResponseEntity.status(500).build();
      }
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns user details")
    @ApiResponses(value = {
      @ApiResponse(
        responseCode = "200",
        description = "User details we returned successfully.",
        content = @Content(
          mediaType = "application/json"
        )
      )
    })
    public ResponseEntity<User> findUserByUsername(@RequestParam("username") String username) {
      try {
        Optional<User> response = userService.findByUsername(username);
        User user = response.get();
        User profileName = new User();
        profileName.setFirstName(user.getFirstName());
        profileName.setLastName(user.getLastName());

        return ResponseEntity.ok(profileName);
      } catch (Exception e) {
        log.error("An unexpected error occurred: " + e.getMessage());
        return ResponseEntity.status(500).build();
      }
    }
}
