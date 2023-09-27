package com.example.trip.domain.post.service;

import com.example.trip.domain.category.CategoryRepository;
import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.CreatePostRequest;
import com.example.trip.domain.post.domain.Post;
import com.example.trip.domain.post.domain.PostDetailsDto;
import com.example.trip.domain.post.domain.ReadPostsDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Autowired
    CategoryRepository categoryRepository;

    @Nested
    class 게시물서비스에서_게시물을_저장한_뒤 {

        @Nested
        class 존재하는_아이디로_조회하면 {

            @Test
            public void 해당하는_엔티티가_조회된다() throws Exception {
                // given
                Category category1 = new Category("category1");
                Category category2 = new Category("category2");
                Category category3 = new Category("category3");

                categoryRepository.save(category1);
                categoryRepository.save(category2);
                categoryRepository.save(category3);

                CreatePostRequest request = CreatePostRequest.builder()
                        .title("test title")
                        .content("test content")
                        .locationList(List.of(1L, 2L, 3L, 4L, 5L))
                        .categoryList(List.of(category1.getId(), category2.getId(), category3.getId()))
                        .imageList(List.of(1L, 2L))
                        .tagList(List.of("tag1", "tag2"))
                        .build();

                Member user = memberRepository.findByNickname("user");

                // when
                Long postId = postService.createPost(user.getId(), request);
                Post findOne = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("NO ENTITY"));
                PostDetailsDto postDetails = postService.readPostDetails(postId);

                // then
                Assertions.assertThat(findOne.getId()).isEqualTo(postId);
                Assertions.assertThat(findOne.getId()).isEqualTo(postDetails.getId());
                Assertions.assertThat(findOne.getTitle()).isEqualTo(postDetails.getTitle());
                Assertions.assertThat(findOne.getContent()).isEqualTo(postDetails.getContent());

            }
        }

        @Nested
        class 존재하지않는_아이디로_조회하면 {

            @Test
            public void 예외가_발생한다() throws Exception {
                // given
                Category category1 = new Category("category1");
                Category category2 = new Category("category2");
                Category category3 = new Category("category3");

                categoryRepository.save(category1);
                categoryRepository.save(category2);
                categoryRepository.save(category3);

                CreatePostRequest request = CreatePostRequest.builder()
                        .title("test title")
                        .content("test content")
                        .locationList(List.of(1L, 2L, 3L, 4L, 5L))
                        .categoryList(List.of(category1.getId(), category2.getId(), category3.getId()))
                        .imageList(List.of(1L, 2L))
                        .tagList(List.of("tag1", "tag2"))
                        .build();

                Member user = memberRepository.findByNickname("user");

                // when
                Long postId = postService.createPost(user.getId(), request);
                PostDetailsDto postDetails = postService.readPostDetails(postId);

                // then
                Assertions.assertThatThrownBy(() -> postRepository.findById(postId + 2).orElseThrow(RuntimeException::new))
                        .isInstanceOf(RuntimeException.class);

            }
        }
    }

    @Test
    public void 게시물_리스트_조회_테스트() throws Exception {
        // given
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");
        Category category3 = new Category("category3");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        CreatePostRequest request1 = CreatePostRequest.builder()
                .title("test title1")
                .content("test content1")
                .locationList(List.of(1L, 2L, 3L, 4L, 5L))
                .categoryList(List.of(category1.getId(), category2.getId(), category3.getId()))
                .imageList(List.of(1L, 2L))
                .tagList(List.of("tag1", "tag2"))
                .build();

        CreatePostRequest request2 = CreatePostRequest.builder()
                .title("test title2")
                .content("test content2")
                .locationList(List.of())
                .categoryList(List.of(category1.getId(), category2.getId(), category3.getId()))
                .imageList(List.of())
                .tagList(List.of("tag3"))
                .build();

        Long postId1 = postService.createPost("user", request1);
        Long postId2 = postService.createPost("user", request2);

        int pageNumber = 0;
        int pageSize = 10;

        // when
        ReadPostsDto readPostsDto = postService.readPosts(pageNumber, pageSize);
        List<Post> posts = postRepository.findAll();

        // then
        Assertions.assertThat(readPostsDto.getCount()).isEqualTo(posts.size());
        Assertions.assertThat(readPostsDto.getPageNumber()).isEqualTo(pageNumber);
        Assertions.assertThat(posts).extracting(
                        Post::getId,
                        Post::getTitle)
                .contains(
                        Tuple.tuple(posts.get(0).getId(), posts.get(0).getTitle()),
                        Tuple.tuple(posts.get(1).getId(), posts.get(1).getTitle()));
    }

}