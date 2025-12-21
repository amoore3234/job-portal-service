package com.portal.job_portal_service.model;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_login")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String userEmail;
  private String userPassword;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private ZonedDateTime createdTimestamp;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private ZonedDateTime updatedTimestamp;
}
