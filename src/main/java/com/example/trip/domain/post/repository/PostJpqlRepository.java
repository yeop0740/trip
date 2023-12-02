package com.example.trip.domain.post.repository;

import com.example.trip.domain.post.domain.Post;
import com.example.trip.domain.post.domain.SearchType;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class PostJpqlRepository {

    private final EntityManager em;


    public Stream<Post> findBySearch(SearchType search) {
        StringBuilder jpqlBuilder = new StringBuilder();
        jpqlBuilder.append("select p")
                .append(" from Post p");

        String jpql = createWhereClause(jpqlBuilder, search);

        return em.createQuery(jpql, Post.class)
                .setFirstResult(search.getPageNumber() * search.getPageSize())
                .setMaxResults(search.getPageSize() + 1)
                .getResultStream();
    }

    private String createWhereClause(StringBuilder jpqlBuilder, SearchType search) {

        if (search.getCategoryId() != null) {
            jpqlBuilder.append(" left outer join p.postCategoryList pc")
                    .append(" left outer join pc.category c")
                    .append(" where")
                    .append(" pc.category.id = ")
                    .append(search.getCategoryId());
            if (search.getName() != null || search.getTitle() != null) {
                jpqlBuilder.append(" and");
            }
        }

        if ((search.getTitle() != null || search.getName() != null) && search.getCategoryId() == null) {
            jpqlBuilder.append(" where");
        }

        if (search.getName() != null) {
            jpqlBuilder.append(" p.member.nickname like ")
                    .append("'%")
                    .append(search.getName())
                    .append("%'");
            if (search.getTitle() != null) {
                jpqlBuilder.append(" and");
            }
        }

        if (search.getTitle() != null) {
            jpqlBuilder.append(" p.title like ")
                    .append("'%")
                    .append(search.getTitle())
                    .append("%'");
        }

        jpqlBuilder.append(" order by p.id desc");

        return jpqlBuilder.toString();
    }

}
