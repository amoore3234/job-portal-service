package com.portal.job_portal_service.model;

import java.time.ZonedDateTime;

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
  private ZonedDateTime createdTimestamp;
  private ZonedDateTime updatedTimestamp;
}
