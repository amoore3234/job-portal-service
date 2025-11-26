package com.portal.job_portal_service.controller;

import com.portal.job_portal_service.client.dto.JobPostingResponse;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;
import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.service.JobPortalService;
import com.portal.job_portal_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/service/portal")
@Tag(name = "job-portal")
public class JobPortalController {

  private final UserService userService;

  @Autowired
  private final JobPortalService jobPostingService;

  private final PasswordEncoder passwordEncoder;

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
  public ResponseEntity<?> createUser(User user) {
    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    user.setCreatedTimestamp(ZonedDateTime.now());
    userService.createUser(user);

    return new ResponseEntity<>("User registered successfully", HttpStatusCode.valueOf(200));
  }

  @GetMapping(value = "/jobPostings", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Returns a list of job postings from the job board.")
    @ApiResponses(value = {
      @ApiResponse(
        responseCode = "200",
        description = "Job postings were returned successfully",
        content = @Content(
          mediaType = "application/json",
          examples = @ExampleObject(
            value = "[\r\n" + //
                            "  {\r\n" + //
                            "    \"job_title\": \"string\",\r\n" + //
                            "    \"job_url\": \"string\",\r\n" + //
                            "    \"company_name\": \"string\",\r\n" + //
                            "    \"company_logo\": \"string\",\r\n" + //
                            "    \"company_address\": \"string\",\r\n" + //
                            "    \"company_salary\": \"string\",\r\n" + //
                            "    \"company_metadata\": [\r\n" + //
                            "      \"string\"\r\n" + //
                            "    ],\r\n" + //
                            "    \"date_posted\": \"string\"\r\n" + //
                            "  }\r\n" + //
                            "]"
          )
        )
      )
    })
  public ResponseEntity<JobPostingResponse> getPostings() {
    try {
      List<JobPostingResponseDTO> postings = jobPostingService.getJobPostings();
      JobPostingResponse response = new JobPostingResponse();
      response.setPostings(postings);
      return ResponseEntity.ok(response);
    } catch(WebClientResponseException e) {
      if (e.getStatusCode().is4xxClientError() || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        log.error("The client is unauthorized to make the request", e);
        throw new ResponseStatusException(e.getStatusCode());
      } else {
        log.error("There was an issue with the server when making a request to the client", e);
        throw new ResponseStatusException(e.getStatusCode(), "Something went wrong with the server");
      }
    }
  }

  @PostMapping(value = "/addJobPostings")
  @Operation(summary = "Add a list of job postings from the job board site.")
    @ApiResponses(value = {
      @ApiResponse(
        responseCode = "204",
        description = "Job postings were saved successfully"
      )
    })
  public ResponseEntity<Void> addJobPostings() {
    try {
      jobPostingService.addJobPostings();
      return ResponseEntity.noContent().build();
    } catch(WebClientResponseException e) {
      if (e.getStatusCode().is4xxClientError() || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        log.error("The client is unauthorized to make the request", e);
        throw new ResponseStatusException(e.getStatusCode());
      } else {
        log.error("There was an issue with the server when making a request to the client", e);
        throw new ResponseStatusException(e.getStatusCode(), "Something went wrong with the server");
      }
    }
  }
}
