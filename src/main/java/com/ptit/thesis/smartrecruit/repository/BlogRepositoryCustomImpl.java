package com.ptit.thesis.smartrecruit.repository;

import com.ptit.thesis.smartrecruit.dto.response.AdminBlogResponse;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.entity.QBlog;
import com.ptit.thesis.smartrecruit.entity.QRole;
import com.ptit.thesis.smartrecruit.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BlogRepositoryCustomImpl implements BlogRepositoryCustom {
    JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminBlogResponse> getBlogsForAdmin(Pageable pageable) {
        QBlog blog = QBlog.blog;
        QUser user = QUser.user;

        var query = this.jpaQueryFactory.select(
                Projections.constructor(
                    AdminBlogResponse.class,
                    blog.id,
                    blog.title,
                    blog.status,
                    blog.createdAt,
                    blog.thumbnail,
                    blog.author
                )
            )
            .from(blog)
            .leftJoin(blog.author, user)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset()
            );

        List<AdminBlogResponse> data = query.fetch();

        Long total = jpaQueryFactory
            .select(blog.id.count())
            .from(blog)
            .fetchOne();

        return new PageImpl<>(data, pageable, total);
    }
}
