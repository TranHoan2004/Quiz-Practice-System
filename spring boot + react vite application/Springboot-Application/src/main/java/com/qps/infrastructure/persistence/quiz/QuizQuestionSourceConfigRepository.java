package com.qps.infrastructure.persistence.quiz;

import com.qps.domain.quiz.model.QuizQuestionSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionSourceConfigRepository extends JpaRepository<QuizQuestionSourceConfig, String> {
    @Query("""
            SELECT sourceType FROM QuizQuestionSourceConfig WHERE id = :id
            """)
    String findSourceTypeById(@Param("id") String id);
}
