package com.example.trip.domain.interaction.service;

import com.example.trip.domain.interaction.InteractionRepository;
import com.example.trip.domain.interaction.domain.InteractionRequest;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.Post;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class InteractionServiceTest {
    
    @Autowired
    InteractionService interactionService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    InteractionRepository interactionRepository;

    @Nested
    class 인터랙션을_생성할_때 {
        
        @Test
        public void 존재하지_않는_멤버가_요청할때_예외가_발생한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();
            InteractionRequest request = new InteractionRequest(postId, InteractionType.LIKE);

            // then
            Assertions.assertThat(memberRepository.findById(memberId + 1)).isEqualTo(Optional.empty());
            Assertions.assertThatThrownBy(() -> interactionService.createInteraction(memberId + 1, request))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        public void 존재하지_않는_게시물에_대해_요청할때_예외가_발생한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();
            InteractionRequest request = new InteractionRequest(postId + 1, InteractionType.LIKE);

            // then
            Assertions.assertThat(postRepository.findById(postId + 1)).isEqualTo(Optional.empty());
            Assertions.assertThatThrownBy(() -> interactionService.createInteraction(memberId, request))
                    .isInstanceOf(RuntimeException.class);
        }
        
        @Test
        public void 이미_인터랙션이_존재하는_경우_예외가_발생한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();

            InteractionRequest request1 = new InteractionRequest(postId, InteractionType.LIKE);
            InteractionRequest request2 = new InteractionRequest(postId, InteractionType.LIKE);

            interactionService.createInteraction(memberId, request1);

            // then
            Assertions.assertThatThrownBy(() -> interactionService.createInteraction(memberId, request2))
                    .isInstanceOf(RuntimeException.class);
        }
        
        @Test
        public void 정상적으로_인터렉션을_추가한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();

            InteractionRequest request1 = new InteractionRequest(postId, InteractionType.LIKE);
            InteractionRequest request2 = new InteractionRequest(postId, InteractionType.SCRAP);

            interactionService.createInteraction(memberId, request1);
            interactionService.createInteraction(memberId, request2);

            // then
            Assertions.assertThat(interactionRepository.findAll())
                    .extracting("type", "member", "post")
                            .contains(Tuple.tuple(InteractionType.LIKE, member, post),
                                    Tuple.tuple(InteractionType.SCRAP, member, post));
        }
    }

    @Nested
    class 인터랙션을_삭제할_때 {

        @Test
        public void 존재하지_않는_멤버가_요청할때_예외가_발생한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();
            InteractionRequest request = new InteractionRequest(postId, InteractionType.LIKE);
            interactionService.createInteraction(memberId, request);

            // then
            Assertions.assertThat(memberRepository.findById(memberId + 1)).isEqualTo(Optional.empty());
            Assertions.assertThatThrownBy(() -> interactionService.deleteInteraction(memberId + 1, request))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        public void 존재하지_않는_게시물에_대해_요청할때_예외가_발생한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();
            InteractionRequest request = new InteractionRequest(postId, InteractionType.LIKE);
            interactionService.createInteraction(memberId, request);
            InteractionRequest deleteRequest = new InteractionRequest(postId + 1, InteractionType.LIKE);

            // then
            Assertions.assertThat(postRepository.findById(postId + 1)).isEqualTo(Optional.empty());
            Assertions.assertThatThrownBy(() -> interactionService.deleteInteraction(memberId, deleteRequest))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        public void 인터랙션이_존재하지않는_경우_예외가_발생한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();

            InteractionRequest likeRequest = new InteractionRequest(postId, InteractionType.LIKE);
            InteractionRequest scrapRequest = new InteractionRequest(postId, InteractionType.SCRAP);

            interactionService.createInteraction(memberId, likeRequest);

            // then
            Assertions.assertThat(interactionRepository.findByTypeAndMemberAndPost(InteractionType.SCRAP, member, post))
                    .isEqualTo(Optional.empty());
            Assertions.assertThatThrownBy(() -> interactionService.deleteInteraction(memberId, scrapRequest))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        public void 정상적으로_인터렉션을_삭제한다() throws Exception {
            // given
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);

            Long memberId = member.getId();
            Long postId = post.getId();

            InteractionRequest request1 = new InteractionRequest(postId, InteractionType.LIKE);
            InteractionRequest request2 = new InteractionRequest(postId, InteractionType.SCRAP);

            interactionService.createInteraction(memberId, request1);
            interactionService.createInteraction(memberId, request2);

            Long likeInteractionId = interactionRepository.findByTypeAndMemberAndPost(InteractionType.LIKE, member, post).orElseThrow(RuntimeException::new).getId();
            Long scrapInteractionId = interactionRepository.findByTypeAndMemberAndPost(InteractionType.SCRAP, member, post).orElseThrow(RuntimeException::new).getId();

            interactionService.deleteInteraction(memberId, request1);
            interactionService.deleteInteraction(memberId, request2);

            // then
            Assertions.assertThat(interactionRepository.findById(likeInteractionId)).isEqualTo(Optional.empty());
            Assertions.assertThat(interactionRepository.findById(scrapInteractionId)).isEqualTo(Optional.empty());
        }
    }

}