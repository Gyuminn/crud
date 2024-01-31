package com.crud.mock;

import com.crud.domain.PostEntity;
import com.crud.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MockDataTest {
    @Autowired
    private PostRepository postRepository;

    // 더미데이터 삽입
    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 300).forEach(i -> {
            PostEntity postEntity = PostEntity.builder()
                    .title("제목..............." + i)
                    .content("내용........................................................................................." + i)
                    .build();
            System.out.println(postRepository.save(postEntity));
        });
    }
}
