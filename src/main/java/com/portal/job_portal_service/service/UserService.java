package com.portal.job_portal_service.service;

import com.portal.job_portal_service.model.User;
import com.portal.job_portal_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("null")
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

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public boolean checkUserResumeExists(String username, String directoryPath) {
    File folder = new File(directoryPath);

    // Check if the directory exists
    if (!folder.exists() || !folder.isDirectory()) {
        return false;
    }

    File[] listOfFiles = folder.listFiles();
    if (listOfFiles == null) return false;

    // Iterate through files looking for a naming signature prefix or suffix matching the user
    for (File file : listOfFiles) {
      if (file.isFile() && file.getName().toLowerCase().contains(username.toLowerCase())) {
        System.out.println("-> Filesystem Match Located: " + file.getName());
        return true;
      }
    }

    return false;
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
