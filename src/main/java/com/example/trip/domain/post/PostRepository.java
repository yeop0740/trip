package com.example.trip.domain.post;

import com.example.trip.domain.member.domain.Member;
import com.example.trip.domain.member.dto.GetMyPostResponse;
import com.example.trip.domain.post.domain.Post;
import com.example.trip.domain.post.domain.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Pageable pageable);


    @Query(value = " select new com.example.trip.domain.member.dto.GetMyPostResponse( " +
            " p.id," +
            " p.title," +
            " (select count(i) from Interaction i inner join i.post p2 where i.type = 'LIKE' and p2 = p), " +
            " (select count(i) from Interaction i inner join i.post p2 where i.type = 'SCRAP' and p2 = p)," +
            " p.createdTime " +
            " ) from Post p where p.member = :member")
    List<GetMyPostResponse> findPostByMember(@Param("member") Member member);

    Slice<Post> findAllByPostCategoryListIn(List<PostCategory> categories, PageRequest page);

}
