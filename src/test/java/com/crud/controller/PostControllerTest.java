package com.crud.controller;

import com.crud.domain.PostEntity;
import com.crud.repository.PostRepository;
import com.crud.request.PostCreate;
import com.crud.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                // json 검증방법
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        PostEntity postEntity = postRepository.findAll().get(0);
        assertEquals("제목입니다.", postEntity.getTitle());
        assertEquals("내용입니다.", postEntity.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        PostEntity postEntity = PostEntity.builder()
                .title("12345678912345")
                .content("내용1")
                .build();
        postRepository.save(postEntity);

        // expected
        mockMvc.perform(get("/posts/{postId}", postEntity.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postEntity.getId()))
                .andExpect(jsonPath("$.title").value("1234567891"))
                .andExpect(jsonPath("$.content").value("내용1"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test5() throws Exception {
        // given
        List<PostEntity> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> PostEntity.builder().title("제목 " + i)
                        .content("내용 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&sort=id,desc")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목 30"))
                .andExpect(jsonPath("$[0].content").value("내용 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정")
    void test6() throws Exception {
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

        // expected
        mockMvc.perform(patch("/posts/{postId}", requestPost.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test7() throws Exception {
        // given
        PostEntity requestPost = PostEntity.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(requestPost);

        // expected
        mockMvc.perform(delete("/posts/{postId}", requestPost.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}