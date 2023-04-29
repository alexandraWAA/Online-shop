package com.example.shoponline.controller;
import com.example.shoponline.dto.NewPassword;
import com.example.shoponline.dto.UserDTO;
import com.example.shoponline.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Обновление пароля", tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NewPassword.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword,
                                         Authentication authentication) {
        userService.updatePassword(newPassword, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получить информацию об авторизованном пользователе", tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @GetMapping("/me")
    public ResponseEntity<?> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserByUsername(authentication));
    }

    @Operation(
            summary = "Обновить информацию об авторизованном пользователе", tags = "Пользователи",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))}),
                    @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorised", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(userDTO));
    }

    @Operation(
            summary = "Обновить аватар авторизованного пользователя", tags = "Пользователи",
            responses = {
                    @ApiResponse(description = "Файл изображения для обновления аватара пользователя",
                    content = {@Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary"))}),
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserAvatar(Authentication authentication,
                                              @RequestPart("image") MultipartFile avatarFile) throws IOException {
       userService.updateUserAvatar(authentication, avatarFile);
        return ResponseEntity.ok().build();
    }
    @Operation(hidden = true)
    @GetMapping(value = "/me/image/{id}", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public byte[] showAvatarOnId(@PathVariable("id") Integer id) {
        return userService.showAvatarOnId(id);
    }
}