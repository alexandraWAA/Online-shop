package com.example.shoponline.controller;

import com.example.shoponline.dto.AdsDTO;
import com.example.shoponline.dto.CommentDTO;
import com.example.shoponline.enums.Role;
import com.example.shoponline.model.Ads;
import com.example.shoponline.model.Comment;
import com.example.shoponline.model.User;
import com.example.shoponline.repository.AdsRepository;
import com.example.shoponline.repository.CommentRepository;
import com.example.shoponline.repository.UserRepository;
import com.example.shoponline.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.transaction.Transactional;
import java.time.Instant;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
@Transactional
@Testcontainers
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CustomUserDetailsService manager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;

    private final User user = new User();

    private Authentication authentication;
    private final Ads ads = new Ads();
    private final Comment comment = new Comment();
    private final CommentDTO commentDTO = new CommentDTO();

    @BeforeEach
    void setUp() {
        user.setRole(Role.USER);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPhone("+71123456789");
        user.setUsername("test@test.test");
        user.setPassword("test1234");
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails userDetails = manager
                .loadUserByUsername(user.getUsername());

        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        ads.setTitle("Test ads");
        ads.setDescription("Test test");
        ads.setPrice(150);
        ads.setAuthor(user);
        adsRepository.save(ads);

        comment.setText("Test comment");
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setAuthor(user);
        commentRepository.save(comment);

        commentDTO.setText("Comment test");
    }

    @AfterEach
    void cleatUp() {
        commentRepository.delete(comment);
        adsRepository.delete(ads);
        userRepository.delete(user);
    }

    @Test
    @WithMockUser(username = "test@test.ru", password = "aqws123")
    public void testGetCommentsByAdsId() throws Exception {
        mockMvc.perform(get("/ads/{id}/comments", ads.getPk())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result[0].text").value(comment.getText()));
    }

    @Test
    public void testAddComment() throws Exception {
        MvcResult result = mockMvc.perform(post("/ads/{id}/comments", ads.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.text")
                        .value(commentDTO.getText()))
                .andExpect(jsonPath("$.authorFirstName")
                        .value(user.getFirstName()))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        AdsDTO createdAd = objectMapper.readValue(responseBody, AdsDTO.class);
        commentRepository.deleteById(createdAd.getPk());
    }
}