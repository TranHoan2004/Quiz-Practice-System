package com.qps.infrastructure.persistence.quiz;

import com.qps.application.dto.response.QuizResp;
import com.qps.domain.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    // QuizDAO.getQuizDTOById(String id)
    @Query("""
            SELECT new com.qps.application.dto.response.QuizResp(
                   q.id, q.duration, q.status, q.passRate,
                   q.updatedDate, q.numberOfQuestion, q.description,
                   q.title, s.name, qt.name)
            FROM Quiz q, Subject s, QuizType qt
            JOIN q.subject.id
            JOIN q.type.id
            WHERE q.id = :id
            """)
    QuizResp findByIdReturnQuizResp(@Param("id") String id);
}
