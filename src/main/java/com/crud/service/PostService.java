package com.crud.service;

import com.crud.domain.PostEntity;
import com.crud.repository.PostRepository;
import com.crud.request.PostCreate;
import com.crud.request.PostEdit;
import com.crud.response.PostResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public void write(PostCreate postCreate) {

        PostEntity postEntity = PostEntity.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(postEntity);
    }

    public PostResponse get(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(postEntity.getId())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .build();
    }

    public List<PostResponse> getList(Pageable pageable) {

        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        postEntity.edit(postEdit.getTitle(), postEdit.getContent());
        return new PostResponse(postEntity);

    }

    @Transactional
    public void delete(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        postRepository.delete(postEntity);

    }
}