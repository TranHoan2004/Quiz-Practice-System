package com.qps.infrastructure.persistence.course;

import com.qps.domain.course.model.PersonalCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PersonalCourseRepository extends JpaRepository<PersonalCourse, String> {
    // PersonalCourseDAO.deleteByCourseAndAccount(String accountID, String courseID)
    void deleteByAccountIdAndCourseId(String accountId, String courseId);

    // PersonalCourseDAO.getCountPersonalCourseByStatus(String startDate, String endDate, String status)
    int countByStatusAndEnrollDateBetween(String status, LocalDate enrollDateAfter, LocalDate enrollDateBefore);

    // PersonalCourseDAO.getCountPersonalCourse(String startDate, String endDate)
    int countByEnrollDateBetween(LocalDate enrollDateAfter, LocalDate enrollDateBefore);

    // PersonalCourseDAO.getCountNewPersonalCourseByNewAccount(String startDate, String endDate)
    @Query("""
            SELECT COUNT(*)
            FROM PersonalCourse ps
            WHERE ps.enrollDate BETWEEN :startDate AND :endDate
            AND ps.account.createdDate BETWEEN :startDate AND :endDate
            """)
    int countByNewAccount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
