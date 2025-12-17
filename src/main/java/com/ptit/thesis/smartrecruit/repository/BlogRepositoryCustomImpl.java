package com.ptit.thesis.smartrecruit.repository;

import com.ptit.thesis.smartrecruit.dto.common.BlogFilterDTO;
import com.ptit.thesis.smartrecruit.dto.response.AdminBlogResponse;
import com.ptit.thesis.smartrecruit.entity.Blog;
import com.ptit.thesis.smartrecruit.entity.QBlog;
import com.ptit.thesis.smartrecruit.entity.QUser;
import com.ptit.thesis.smartrecruit.enums.BlogStatus;
import com.ptit.thesis.smartrecruit.mapper.BlogMapper;
import com.ptit.thesis.smartrecruit.utils.StringUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BlogRepositoryCustomImpl implements BlogRepositoryCustom {
    JPAQueryFactory jpaQueryFactory;
    BlogMapper blogMapper;

    @Override
    public Page<AdminBlogResponse> getBlogsForAdmin(BlogFilterDTO filter, Pageable pageable) {
        QBlog blog = QBlog.blog;
        QUser user = QUser.user;

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(blog.status.ne(BlogStatus.DRAFT));

        if (StringUtil.hasText(filter.getKeyword())) {
            conditions.and(blog.title.containsIgnoreCase(filter.getKeyword()));
        }

        if (filter.getStatus() != null && filter.getStatus() != BlogStatus.DRAFT) {
            conditions.and(blog.status.eq(filter.getStatus()));
        }

        var query = this.jpaQueryFactory
            .selectFrom(blog)
            .leftJoin(blog.author, user)
            .where(conditions)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset()
            );

        List<Blog> blogs = query.fetch();
        List<AdminBlogResponse> lst = this.blogMapper.toListAdminBlogRespone(blogs);

        Long total = Optional.ofNullable(
            jpaQueryFactory
                .select(blog.id.count())
                .from(blog)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(lst, pageable, total);
    }
}
