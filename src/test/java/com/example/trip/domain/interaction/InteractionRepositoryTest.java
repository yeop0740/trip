package com.example.trip.domain.interaction;

import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.interaction.domain.InteractionType;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.Post;
import jakarta.persistence.EntityManager;
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
class InteractionRepositoryTest {

    @Autowired
    InteractionRepository interactionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManager em;

    @Nested
    class 인터랙션_생성_테스트 {

        @Test
        public void 좋아요와_스크랩_타입_생성() throws Exception {
            // give
            Member member = new Member("david", "david", "1234", "www.naver.com");
            memberRepository.save(member);
            Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
            postRepository.save(post);
            Interaction likeInteraction = new Interaction(InteractionType.LIKE, member, post);
            interactionRepository.save(likeInteraction);
            Long likeExpectedId = likeInteraction.getId();

            Interaction scrapInteraction = new Interaction(InteractionType.SCRAP, member, post);
            interactionRepository.save(scrapInteraction);
            Long scrapExpectedId = scrapInteraction.getId();

            // when
            em.flush();
            em.clear();

            // then
            Assertions.assertThat(interactionRepository.findAll()).extracting("type", "member", "post")
                    .contains(
                            Tuple.tuple(InteractionType.LIKE, member, post),
                            Tuple.tuple(InteractionType.SCRAP, member, post)
                    );
        }
    }

    @Test
    public void 인터랙션_삭제_테스트() throws Exception {
        // given
        Member member = new Member("david", "david", "1234", "www.naver.com");
        memberRepository.save(member);
        Post post = new Post("title", "content", member, List.of(), List.of(), List.of(), List.of());
        postRepository.save(post);
        Interaction likeInteraction = new Interaction(InteractionType.LIKE, member, post);
        interactionRepository.save(likeInteraction);
        Long likeExpectedId = likeInteraction.getId();

        // when
        interactionRepository.delete(likeInteraction);

        // then
        Assertions.assertThat(interactionRepository.findById(likeExpectedId)).isEqualTo(Optional.empty());
    }


}