package com.portal.job_portal_service.client.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingResponseDTO {
  private Long id;
  @JsonProperty("job_title")
  private String jobTitle;
  @JsonProperty("job_url")
  private String jobUrl;
  @JsonProperty("company_name")
  private String companyName;
  @JsonProperty("company_logo")
  private String companayLogo;
  @JsonProperty("company_address")
  private String companyAddress;
  @JsonProperty("company_salary")
  private String companySalary;
  @JsonProperty("company_metadata")
  private List<String> companyMetadata;
  @JsonProperty("date_posted")
  private String datePosted;
}
