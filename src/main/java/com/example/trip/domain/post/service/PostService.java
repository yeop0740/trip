package com.example.trip.domain.post.service;

import com.example.trip.domain.category.CategoryRepository;
import com.example.trip.domain.category.domain.Category;
import com.example.trip.domain.image.ImageManager;
import com.example.trip.domain.image.ImageRepository;
import com.example.trip.domain.image.domain.Image;
import com.example.trip.domain.image.domain.ImageDto;
import com.example.trip.domain.location.domain.LocationPath;
import com.example.trip.domain.location.repository.LocationPathRepository;
import com.example.trip.domain.location.repository.LocationRepository;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.*;
import com.example.trip.domain.post.repository.PostCategoryRepository;
import com.example.trip.domain.post.repository.PostJpqlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    private final LocationPathRepository locationPathRepository;
    private final ImageRepository imageRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final PostJpqlRepository postJpqlRepository;

    private final ImageManager imageManager;

    public Long createPost(Long userId, CreatePostRequest request) {
        Member member = memberRepository.findById(userId).orElseThrow(RuntimeException::new);
        LocationPath locationPath = locationPathRepository.findById(request.getLocationPathId()).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
        List<Image> imageList = imageRepository.findAllById(request.getImageList());
        List<PostCategory> postCategoryList = categoryRepository.findAllById(request.getCategoryList()).stream()
                .map(PostCategory::new)
                .toList();

        Post post = Post.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .postCategoryList(postCategoryList)
                .locationPath(locationPath)
                .imageList(imageList)
                .build();

        postRepository.save(post);

        return post.getId();
    }

    public PostDetailsDto readPostDetails(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
        return PostDetailsDto.of(post, getImageDtoList(post));
    }

    public ReadPostsDto readPosts(int pageNumber, int pageSize) {
        Sort sort = Sort.by("createdTime").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> postList = postRepository.findAll(pageRequest);
        List<ReadPostDto> postDtoList = postList.stream()
                .map(post -> ReadPostDto.of(post, getImageDtoList(post)))
                .toList();
        return ReadPostsDto.of(postList, postDtoList);
    }

    public Long updatePost(Long userId, UpdatePostRequest request) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
        Post post = postRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
        LocationPath locationPath = locationPathRepository.findById(request.getLocationPathId()).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
        List<Image> imageList = imageRepository.findAllById(request.getImageList());
        List<PostCategory> postCategoryList = categoryRepository.findAllById(
                        request.getCategoryList()).stream()
                .map(PostCategory::new)
                .toList();
//        postCategoryRepository.save()


        if (!isValid(post, member)) {
            throw new RuntimeException("허용되지 않는 요청입니다.");
        }

        post.change(request.getTitle(), request.getContent(), postCategoryList, locationPath, imageList);

        return post.getId();
    }

    public void deletePost(Long userId, Long postId) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));

        if (!isValid(post, member)) {
            throw new RuntimeException("허용되지 않는 요청입니다.");
        }
        postRepository.delete(post);
    }

    private boolean isValid(Post post, Member member) {
        return post.getMember().equals(member);
    }

    public ReadPostsDto readPostsByCategory(Long categoryId, int pageNumber, int pageSize) {
        PageRequest page = PageRequest.of(pageNumber, pageSize);
        Category category = categoryRepository.findById(categoryId).orElseThrow(RuntimeException::new);
        List<PostCategory> categories = postCategoryRepository.findByCategory(category);
        Slice<Post> postList = postRepository.findAllByPostCategoryListIn(categories, page);
        List<ReadPostDto> postDtoList = postList.map(post -> ReadPostDto.of(post, getImageDtoList(post)))
                .toList();
        return ReadPostsDto.of(postList, postDtoList);
    }

    public ReadPostsDto readPostsBySearch(SearchType search) {
        Stream<Post> postList = postJpqlRepository.findBySearch(search);
        List<ReadPostDto> postDtoList = postList.map(post -> ReadPostDto.of(post, getImageDtoList(post)))
                .toList();
        return ReadPostsDto.of(postDtoList, search);
    }

    public List<ImageDto> getImageDtoList(Post post) {
        return post.getImageList().stream()
                .map(image -> ImageDto.of(image.getId(), imageManager.createSignedUrlForString(image.getImageKey())))
                .toList();
    }

}
