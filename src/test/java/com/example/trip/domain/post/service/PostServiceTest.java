package com.example.trip.domain.post.service;

import com.example.trip.domain.category.CategoryRepository;
import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.location.LocationRepository;
import com.example.trip.domain.member.location.domain.Location;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.*;
import com.example.trip.domain.tag.domain.Tag;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    LocationRepository locationRepository;

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

        @Nested
        class 작성자가_업데이트_요청하면 {

            static String username = "user";
            static String newTitle = "new title";
            static String newContent = "new content";
            static List<Long> newCategoryList;
            static List<Long> newLocationList;
            static List<Long> newImageList;
            static List<String> newTagList;

            @Test
            public void 정상적으로_수행된다() throws Exception {
                // given
                Location location1 = new Location(new BigDecimal(10), new BigDecimal(20), "서울 동작구 상도로 369", "123-4567", true);
                Location location2 = new Location(new BigDecimal(11), new BigDecimal(21), "서울 동작구 상도로 369", "123-4567", true);
                Location location3 = new Location(new BigDecimal(12), new BigDecimal(22), "서울 동작구 상도로 369", "123-4567", true);
                Location location4 = new Location(new BigDecimal(13), new BigDecimal(23), "서울 동작구 상도로 369", "123-4567", true);
                Location location5 = new Location(new BigDecimal(14), new BigDecimal(24), "서울 동작구 상도로 369", "123-4567", true);

                locationRepository.saveAll(List.of(location1, location2, location3, location4, location5));

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

                Member user = memberRepository.findByNickname(username);

                Long postId1 = postService.createPost(user.getId(), request1);

                newLocationList = List.of(2L, 3L);
                newCategoryList = List.of(category3.getId());
                newImageList = List.of(2L, 3L);
                newTagList = List.of("tag2", "tag3");

                // when
                Post post = postRepository.findById(postId1).orElseThrow(() -> new RuntimeException("존재하지 않는 엔티티입니다."));
//                System.out.println("post = " + post);

                UpdatePostRequest request = UpdatePostRequest.builder()
                        .id(postId1)
                        .title(newTitle)
                        .content(newContent)
                        .categoryList(newCategoryList)
                        .locationList(newLocationList)
                        .imageList(newImageList)
                        .tagList(newTagList)
                        .build();

                Member member = memberRepository.findByNickname(username);
                Long updatePostId = postService.updatePost(member.getId(), request);

                Post updatePost = postRepository.findById(updatePostId).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
//                for (PostCategory postCategory : expectPostCategoryList) {
//                    System.out.println("postCategory = " + postCategory);
//                }

                em.flush();
                em.clear();
                List<Tag> expectTagList = em.createQuery(
                                "select t from Tag t" +
                                        " where t.name in :newTagList",
                                Tag.class)
                        .setParameter("newTagList", newTagList)
                        .getResultList();

                List<PostCategory> expectPostCategoryList = em.createQuery(
                                "select pc from PostCategory pc" +
                                        " where pc.category.id in :newCategoryList",
                                PostCategory.class)
                        .setParameter("newCategoryList", newCategoryList)
                        .getResultList();

                List<Location> expectLocationList = em.createQuery(
                                "select l from Location l" +
                                        " where l.id in :newLocationList",
                                Location.class)
                        .setParameter("newLocationList", newLocationList)
                        .getResultList();

                List<Image> expectImageList = em.createQuery(
                                "select i from Image i" +
                                        " where i.id in :newImageList",
                                Image.class)
                        .setParameter("newImageList", newImageList)
                        .getResultList();

                Post post1 = postRepository.findById(updatePostId).orElseThrow();

                // then
                Assertions.assertThat(post1.getTitle()).isEqualTo(newTitle);
                Assertions.assertThat(post1.getContent()).isEqualTo(newContent);
                Assertions.assertThat(post1.getPostCategoryList()).containsAll(expectPostCategoryList);
                Assertions.assertThat(post1.getLocationList()).containsAll(expectLocationList);
                Assertions.assertThat(post1.getImageList()).containsAll(expectImageList);
                Assertions.assertThat(post1.getTagList()).containsAll(expectTagList);

//                System.out.println("updatePost = " + updatePost);
            }


        }

        @Nested
        class 작성자가_아닌_경우 {

            @Test
            public void 예외가_발생한다() throws Exception {
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

        Member user = memberRepository.findByNickname("user");

        Long postId1 = postService.createPost(user.getId(), request1);
        Long postId2 = postService.createPost(user.getId(), request2);

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