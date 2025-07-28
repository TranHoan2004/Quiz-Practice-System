package com.qps.infrastructure.persistence.quiz;

import com.qps.domain.quiz.model.PersonalQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalQuizRepository extends JpaRepository<PersonalQuiz, String> {
    // PersonalQuizDAO.checkPersonalQuiz(String quizId)
    boolean existsByQuizId(String quizId);

    // PersonalQuizDAO.deleteById(String id)
    void deleteById(@NonNull String id);
}
