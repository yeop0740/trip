package com.example.trip.domain.interaction.service;

import com.example.trip.domain.interaction.InteractionRepository;
import com.example.trip.domain.interaction.domain.InteractionRequest;
import com.example.trip.domain.interaction.domain.Interaction;
import com.example.trip.domain.member.MemberRepository;
import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.post.PostRepository;
import com.example.trip.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InteractionService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final InteractionRepository interactionRepository;

    public void createInteraction(Long memberId, InteractionRequest request) throws RuntimeException {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        Post post = postRepository.findById(request.getPostId()).orElseThrow(RuntimeException::new);
        if (interactionRepository.findByTypeAndMemberAndPost(request.getType(), member, post).isPresent()) {
            throw new RuntimeException();
        }
        Interaction interaction = new Interaction(request.getType(), member, post);
        interactionRepository.save(interaction);
    }

    public void deleteInteraction(Long memberId, InteractionRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        Post post = postRepository.findById(request.getPostId()).orElseThrow(RuntimeException::new);
        Interaction interaction = interactionRepository.findByTypeAndMemberAndPost(request.getType(), member, post).orElseThrow(RuntimeException::new);
        member.delete(interaction);
        post.delete(interaction);
        interactionRepository.delete(interaction);
    }

}
