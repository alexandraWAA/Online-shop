package com.example.shoponline.service.impl;

import com.example.shoponline.dto.RegisterReq;
import com.example.shoponline.enums.Role;
import com.example.shoponline.exception.UserNotFoundException;
import com.example.shoponline.mapper.UserMapper;
import com.example.shoponline.model.User;
import com.example.shoponline.repository.UserRepository;
import com.example.shoponline.service.AuthService;
import com.example.shoponline.service.ChecksMethods;
import com.example.shoponline.service.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final CustomUserDetailsService manager;
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;

  public AuthServiceImpl(CustomUserDetailsService manager, PasswordEncoder encoder, UserRepository userRepository) {
    this.manager = manager;
    this.encoder = encoder;
    this.userRepository = userRepository;
  }

  @Override
  public boolean login(String username, String password) {
    ChecksMethods.checkForLogin(password);
    if (!manager.userExists(ChecksMethods.checkForLogin(username))) {
      throw new UserNotFoundException();
    }
    UserDetails userDetails = manager.loadUserByUsername(username);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterReq registerReq) {
    if (manager.userExists(ChecksMethods.checkForChangeParameter(registerReq).getUsername())) {
      return false;
    }
    registerReq.setPassword(ChecksMethods.checkValidatePassword(registerReq.getPassword()));
    registerReq.setUsername(ChecksMethods.checkForEmail(registerReq.getUsername()));
    registerReq.setPhone(registerReq.getPhone());
    User user = UserMapper.fromRegister(registerReq);
    user.setRole(Role.USER);
    manager.createUser(UserMapper.toCustomUserDetails(user));
    return true;
  }

}
