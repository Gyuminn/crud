package com.crud.service;

import com.crud.domain.PostEntity;
import com.crud.repository.PostRepository;
import com.crud.request.PostCreate;
import com.crud.request.PostEdit;
import com.crud.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        PostEntity postEntity = postRepository.findAll().get(0);
        assertEquals("제목입니다.", postEntity.getTitle());
        assertEquals("내용입니다.", postEntity.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        PostEntity requestPost = PostEntity.builder()
                .title("글1")
                .content("내용1")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("글1", response.getTitle());
        assertEquals("내용1", response.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // given
        List<PostEntity> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> PostEntity.builder().title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id");

        // when
        List<PostResponse> posts = postService.getList(pageable);

        // then
        assertEquals(10L, posts.size());
        assertEquals("제목 30", posts.get(0).getTitle());
        assertEquals("제목 26", posts.get(4).getTitle());
    }


    @Test
    @DisplayName("글 수정")
    void test4() {
        // given
        PostEntity requestPost = PostEntity.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .title("수정제목")
                .content("수정내용")
                .build();

        // when
        postService.edit(requestPost.getId(), postEdit);

        // then
        PostEntity changePost = postRepository.findById(requestPost.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + requestPost.getId()));

        Assertions.assertEquals("수정제목", changePost.getTitle());
        Assertions.assertEquals("수정내용", changePost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void test5() {
        // given
        PostEntity requestPost = PostEntity.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(requestPost);

        // when
        postService.delete(requestPost.getId());

        // then
        Assertions.assertEquals(0, postRepository.count());
    }

}