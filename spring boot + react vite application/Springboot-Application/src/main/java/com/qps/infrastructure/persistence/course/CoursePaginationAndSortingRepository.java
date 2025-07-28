package com.qps.infrastructure.persistence.course;

import com.qps.domain.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePaginationAndSortingRepository extends PagingAndSortingRepository<Course, String> {
// pagingRegistrationDto

    @Query("""
            FROM Course c
            WHERE c.topic.subject.id = :id
            """)
    Page<Course> findAllBySubject(@Param("id") String id, Pageable pageable);
}
