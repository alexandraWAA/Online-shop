package com.example.shoponline.controller;

import com.example.shoponline.dto.LoginReq;
import com.example.shoponline.dto.RegisterReq;
import com.example.shoponline.enums.Role;
import com.example.shoponline.model.User;
import com.example.shoponline.repository.UserRepository;
import com.example.shoponline.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvcAuth;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    private RegisterReq registerReq = new RegisterReq();
    private LoginReq loginReq;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("1@mail.ru");
        user.setFirstName("firstTest");
        user.setLastName("lastTest");
        user.setPhone("+79990002233");
        user.setPassword(encoder.encode("1234qwer"));
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);


        registerReq.setUsername("2@mail.ru");
        registerReq.setPassword("1234qwer");
        registerReq.setFirstName("testFirst");
        registerReq.setLastName("testLast");
        registerReq.setPhone("+79990001122");
        registerReq.setRole(Role.USER);

        loginReq = new LoginReq();
        loginReq.setPassword("1234qwer");
        loginReq.setUsername(user.getUsername());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithAnonymousUser
    void login() throws Exception {
        mockMvcAuth.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk());
    }
}