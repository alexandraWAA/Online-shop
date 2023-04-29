package com.example.shoponline.service;

import com.example.shoponline.dto.CommentDTO;
import com.example.shoponline.dto.ResponseWrapperComment;
import com.example.shoponline.exception.CommentNotFoundException;
import com.example.shoponline.exception.UserNotFoundException;
import com.example.shoponline.mapper.CommentMapper;
import com.example.shoponline.model.Comment;
import com.example.shoponline.model.User;
import com.example.shoponline.repository.AdsRepository;
import com.example.shoponline.repository.CommentRepository;
import com.example.shoponline.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, AdsRepository adsRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    public CommentDTO addComment(Integer id, CommentDTO commentDTO, Authentication authentication){
        Comment comment = CommentMapper.fromDTO(ChecksMethods.checkForChangeParameter(commentDTO));
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName()).orElseThrow(UserNotFoundException::new);
        comment.setAuthor(user);
        comment.setAds(adsRepository.findById(id).orElseThrow(CommentNotFoundException::new));
        commentRepository.save(comment);
        return CommentMapper.toDTO(comment);
    }

    public void deleteAdsComment(Integer commentId, Integer adId){
            commentRepository.deleteByPkAndAdsPk(ChecksMethods.checkForChangeParameter(commentId),adId);
        }

    public CommentDTO updateComments(Integer commentId, Integer adId, CommentDTO commentDTO){
        Comment comment = commentRepository.findByPkAndAdsPk(commentId, adId)
                .orElseThrow(CommentNotFoundException::new);
        comment.setText(commentDTO.getText());
        return CommentMapper.toDTO(commentRepository.save(comment));
    }
    public ResponseWrapperComment getCommentsByAdsId(Integer adsId) {
        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setResult(commentRepository.findAllByAdsPk(adsId)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList()));
        responseWrapperComment.setCount(responseWrapperComment.getResult().size());
        return responseWrapperComment;
        }
    public Comment getCommentById(Integer id){
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

}
