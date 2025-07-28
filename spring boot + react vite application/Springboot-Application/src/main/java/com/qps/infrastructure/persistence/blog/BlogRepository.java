package com.qps.infrastructure.persistence.blog;

import com.qps.domain.blog.model.Blog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, String> {
    // BlogDAO.getHottestBlogs(int limit)
    List<Blog> findAllByOrderByViewsDesc(Pageable pageable);

    // BlogDAO.getLatestBlogs(int limit)
    List<Blog> findAllByOrderByCreatedDateDesc(Pageable pageable);

    // BlogDAO.getCategoryNameById(UUID id)
    @Query("""
            SELECT s.value
            FROM Blog b, Setting s
            WHERE b.category.id = s.id
            AND s.settingType.name = "Blog Category"
            AND b.category.id = :id
            """)
    List<Blog> findCategoryNameOfBlog(@Param("id") String id);
}
