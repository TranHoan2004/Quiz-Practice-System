package com.qps.infrastructure.persistence.lesson;

import com.qps.domain.lesson.model.LearningMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, String> {
    // LearningMaterialDAO.getByLessonId(String lessonId)
    LearningMaterial findByLessonId(String lessonId);
}
