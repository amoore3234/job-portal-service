package com.portal.job_portal_service.repository;

import com.portal.job_portal_service.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import com.portal.job_portal_service.util.MockDataUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Test
    void testCreateUser() {

        // Arrange
        User newUser = MockDataUtil.getUserData();

        // Act
        User saveUser = userRepository.save(newUser);

        // Assert
        assertNotNull(saveUser.getId());

        userRepository.delete(saveUser);
    }

    @Test
    void testFindUserById() {

        // Arrange
        User newUser = MockDataUtil.getUserData();
        User createUser = userRepository.save(newUser);

        // Act
        User foundUser = userRepository.getReferenceById(createUser.getId());

        // Assert
        assertEquals(foundUser.getId(), newUser.getId());

        userRepository.delete(foundUser);
    }

    @Test
    void testFindAllUsers() {

        // Arrange
        User firstUser = MockDataUtil.getUserData();
        User secondUser = MockDataUtil.getUserData();

        List<User> users = List.of(firstUser, secondUser);
        userRepository.saveAll(users);

        // Act
        List<User> fetchUsers = userRepository.findAll();

        // Assert
        assertEquals(2, fetchUsers.size());

        userRepository.deleteAll();
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User newUser = MockDataUtil.getUserData();
        User saveUser = userRepository.save(newUser);

        // Act
        userRepository.delete(saveUser);
        User findNullUser = userRepository.findById(1L).orElse(null);

        // Assert
        assertNull(findNullUser);
    }

    @Test
    void testDeleteAllUsers() {

        // Arrange
        User firstUser = MockDataUtil.getUserData();
        User secondUser = MockDataUtil.getUserData();

        List<User> users = List.of(firstUser, secondUser);
        userRepository.saveAll(users);

        // Act
        userRepository.deleteAll();
        int size = userRepository.findAll().size();

        // Assert
        assertEquals(0, size);
    }
}
