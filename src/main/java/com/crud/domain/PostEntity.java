package com.crud.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PostEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Builder
    public PostEntity(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void edit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
