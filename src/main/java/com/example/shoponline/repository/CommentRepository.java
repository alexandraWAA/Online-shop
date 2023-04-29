package com.example.shoponline.repository;

import com.example.shoponline.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByPkAndAdsPk(Integer pk, Integer adsPk);

    List<Comment> findAllByAdsPk (Integer pk);

    void deleteByPkAndAdsPk(Integer pk, Integer adsPk);
}