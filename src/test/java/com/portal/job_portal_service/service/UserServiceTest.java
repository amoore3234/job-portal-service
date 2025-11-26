package com.portal.job_portal_service.service;

import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.repository.UserRepository;
import com.portal.job_portal_service.util.MockDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User user;

  @BeforeEach
  public void setUp() {
    user = MockDataUtil.getUserData();
  }

  @Test
  void testCreateNewUser() {

    // Arrange
    Long expectedId = 1L;
    user.setId(expectedId);

    when(userRepository.save(any())).thenReturn(user);

    // Act
    Long actualId = userService.createUser(user).getId();

    // Assert
    assertEquals(expectedId, actualId);
  }

  @Test
  void testFindUserById() {

    // Arrange
    User expected = user;
    expected.setId(1L);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(expected));

    // Act
    User actual = userService.findUserById(1L);

    // Assert
    assertEquals(expected, actual);
  }

  @Test
  void testFindAllUsers() {

    // Arrange
    User userOne = user;
    userOne.setId(1L);

    User userTwo = user;
    userTwo.setId(2L);

    List<User> expected = List.of(userOne, userTwo);

    when(userRepository.findAll()).thenReturn(expected);

    // Act
    List<User> actual = userService.findAllUsers();

    assertEquals(expected, actual);
  }

  @Test
  void testDeleteUser() {

    // Act
    userService.deleteUser(user);

    // Assert
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void testDeleteUsers() {

    // Act
    userService.deleteAllUsers();

    // Assert
    verify(userRepository, times(1)).deleteAll();
  }
}
