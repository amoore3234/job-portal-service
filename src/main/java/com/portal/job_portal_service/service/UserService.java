package com.portal.job_portal_service.service;

import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User findUserById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  public void deleteUser(User user) {
    userRepository.delete(user);
  }

  public void deleteAllUsers() {
    userRepository.deleteAll();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      User existingUser = user.get();
      return org.springframework.security.core.userdetails.User.builder()
          .username(existingUser.getUsername())
          .password(existingUser.getUserPassword())
          .build();
    } else {
      throw new UsernameNotFoundException(username);
    }
  }
}
