package com.example.trip.domain.interaction.service;

import com.example.trip.domain.interaction.InteractionRepository;
import com.example.trip.domain.interaction.domain.CreateInteractionRequest;
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

import static org.junit.jupiter.api.Assertions.*;

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
            CreateInteractionRequest request = new CreateInteractionRequest(postId, InteractionType.LIKE);

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
            CreateInteractionRequest request = new CreateInteractionRequest(postId + 1, InteractionType.LIKE);

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

            CreateInteractionRequest request1 = new CreateInteractionRequest(postId, InteractionType.LIKE);
            CreateInteractionRequest request2 = new CreateInteractionRequest(postId, InteractionType.LIKE);

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

            CreateInteractionRequest request1 = new CreateInteractionRequest(postId, InteractionType.LIKE);
            CreateInteractionRequest request2 = new CreateInteractionRequest(postId, InteractionType.SCRAP);

            interactionService.createInteraction(memberId, request1);
            interactionService.createInteraction(memberId, request2);

            // then
            Assertions.assertThat(interactionRepository.findAll())
                    .extracting("type", "member", "post")
                            .contains(Tuple.tuple(InteractionType.LIKE, member, post),
                                    Tuple.tuple(InteractionType.SCRAP, member, post));
        }
    }

}