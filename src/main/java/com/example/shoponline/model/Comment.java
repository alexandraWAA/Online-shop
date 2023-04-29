package com.example.shoponline.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private Ads ads;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

}
