package com.qps.infrastructure.persistence.lesson;

import com.qps.domain.lesson.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonPaginationAndSortingRepository extends PagingAndSortingRepository<Lesson, String> {
    // LessonDAO.getLessonByCourseId(String id)
    Page<Lesson> findByCourseId(@Param("courseId") String courseId, Pageable pageable);

    // LessonDAO.getLessonsBySubjectId(String subjectId)
    @Query("""
            FROM Lesson l
            WHERE l.course.topic.subject.id = :subjectId
            ORDER BY l.name ASC
            """)
    Page<Lesson> findBySubjectId(@Param("subjectId") String subjectId, Pageable pageable);
}
