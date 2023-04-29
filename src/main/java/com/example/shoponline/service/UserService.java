package com.example.shoponline.service;

import com.example.shoponline.dto.NewPassword;
import com.example.shoponline.dto.UserDTO;
import com.example.shoponline.exception.ImageNotFoundException;
import com.example.shoponline.exception.IncorrectArgumentException;
import com.example.shoponline.exception.UserNotFoundException;
import com.example.shoponline.mapper.UserMapper;
import com.example.shoponline.model.User;
import com.example.shoponline.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void updatePassword(NewPassword newPassword, Authentication authentication) {
        ChecksMethods.checkForChangeParameter(newPassword);
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectArgumentException();
        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    public UserDTO getUserByUsername(Authentication authentication){
        return UserMapper.toDTO(userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new));
    }

    public UserDTO updateUser(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId()).orElseThrow(UserNotFoundException::new);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        userRepository.save(user);
        return userDTO;
    }

    public boolean updateUserAvatar(Authentication authentication, MultipartFile avatar) throws IOException{

        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        user.setImage(ChecksMethods.checkForChangeParameter(avatar).getBytes());
        userRepository.save(user);
        return true;
    }
    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }
    public byte[] showAvatarOnId(Integer id) {
        return userRepository.findById(id).orElseThrow(ImageNotFoundException::new).getImage();
    }
}
