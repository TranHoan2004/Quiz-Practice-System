package com.qps.infrastructure.persistence.blog;

import com.qps.domain.blog.model.BlogMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMediaRepository extends JpaRepository<BlogMedia, String> {
    // BlogMediaDAO.deleteBlogMedia(UUID id)
    void deleteById(@NonNull String id);

    // BlogMediaDAO.deleteBlogMediaByBlogId(UUID blogId)
    void deleteByBlogId(String blogId);

    // BlogMediaDAO.getBlogMediaByBlogId(UUID blogId)
    List<BlogMedia> findAllByBlogId(String blogId);
}
