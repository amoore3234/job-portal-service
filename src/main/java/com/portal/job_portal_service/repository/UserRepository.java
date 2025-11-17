package com.portal.job_portal_service.repository;

import com.portal.job_portal_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
