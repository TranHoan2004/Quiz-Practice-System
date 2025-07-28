package com.qps.infrastructure.persistence.course;

import com.qps.application.dto.response.StatusPersonalCourseResp;
import com.qps.domain.course.model.PersonalCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalCoursePaginationAndSortingRepository extends PagingAndSortingRepository<PersonalCourse, String> {
    // PersonalCourseDAO.getAllByAccount(String id)
    Page<PersonalCourse> findByAccountId(String accountId, Pageable pageable);

    // PersonalCourseDAO.getAllByCourse(String id)
    Page<PersonalCourse> findByCourseId(String courseId, Pageable pageable);

    // PersonalCourseDAO.getTopCoursePurchases(int limit)
    @Query("""
            SELECT ps, COUNT(*) AS total_purchases
            FROM PersonalCourse ps
            WHERE ps.status = 'PAID'
            GROUP BY ps.course.id
            ORDER BY total_purchases DESC
            """)
    Page<PersonalCourse> findTopCoursePurchases(Pageable pageable);

    // PersonalCourseDAO.getStatus()
    @Query("SELECT DISTINCT status FROM PersonalCourse WHERE status IS NOT NULL")
    Page<StatusPersonalCourseResp> findStatusNotNullPersonalCourseResp(Pageable pageable);
}
