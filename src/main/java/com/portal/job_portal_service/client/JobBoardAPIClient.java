package com.portal.job_portal_service.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.portal.job_portal_service.client.dto.JobPostingRequestDTO;
import com.portal.job_portal_service.client.dto.JobPostingResponseDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class JobBoardAPIClient {

  private final WebClient jobBoardAPIClient;

  public List<JobPostingResponseDTO> getPostings() {
    List<JobPostingResponseDTO> postings = new ArrayList<>();
    try {
      postings = jobBoardAPIClient.get()
          .uri("/postings")
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<List<JobPostingResponseDTO>>() {})
          .block();
    } catch (WebClientResponseException e) {
      log.error("There was an issue fetching job postings with status code: {}", e.getStatusCode());
    }
    if (postings != null && !postings.isEmpty()) {
      return postings;
    }
    return new ArrayList<>();
  }

  public List<JobPostingRequestDTO> addPostings() {
    List<JobPostingRequestDTO> addPostings = new ArrayList<>();
    try {
      addPostings = jobBoardAPIClient.post()
          .uri("/job_postings")
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<List<JobPostingRequestDTO>>() {})
          .block();
    } catch (WebClientResponseException e) {
      log.error("There was an issue adding job postings with status code: {}", e.getStatusCode());
    }
    if (addPostings != null && !addPostings.isEmpty()) {
      return addPostings;
    }
    return new ArrayList<>();
  }
}
