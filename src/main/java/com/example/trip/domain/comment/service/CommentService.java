package com.example.trip.domain.comment.service;

import com.example.trip.domain.comment.domain.Comment;
import com.example.trip.domain.comment.domain.CreateCommentRequest;
import com.example.trip.domain.comment.repository.CommentRepository;
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
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public Long createComment(Member member, CreateCommentRequest request) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(RuntimeException::new);
        Post post = postRepository.findById(request.getPostId()).orElseThrow(RuntimeException::new);
        Comment comment = new Comment(request.getContent(), findMember, post);
        commentRepository.save(comment);
        return post.getId();
    }

    public void deleteComment(Member member, Long commentId) {
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(RuntimeException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);
        if (!comment.getMember().equals(findMember)) {
            throw new RuntimeException();
        }
        comment.setDeleted();
    }

}
