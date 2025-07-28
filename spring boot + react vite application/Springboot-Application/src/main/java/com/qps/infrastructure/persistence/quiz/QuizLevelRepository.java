package com.qps.infrastructure.persistence.quiz;

import com.qps.domain.quiz.model.QuizLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizLevelRepository extends JpaRepository<QuizLevel, String> {
    @Query("""
            SELECT name FROM QuizLevel WHERE id = :id
            """)
    String findNameById(@Param("id") String id);
}
