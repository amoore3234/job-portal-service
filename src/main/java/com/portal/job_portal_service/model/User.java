package com.portal.job_portal_service.model;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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

  @Nonnull
  @Column
  private String firstName;

  @Nonnull
  @Column
  private String lastName;

  @Nonnull
  @Column
  private String userEmail;

  @Nonnull
  @Column
  private String userPassword;

  @Nonnull
  @Column
  private ZonedDateTime createdTimestamp;

  @Nullable
  @Column
  private ZonedDateTime updatedTimestamp;
}
