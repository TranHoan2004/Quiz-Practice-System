package com.qps.infrastructure.persistence.lesson;

import com.qps.domain.lesson.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
    // LessonDAO.countByCourseId(String courseId)
    int countByCourseId(String courseId);

    // LessonDAO.getOrderOfLesson(String lessonId)
    // Tach thanh 2 phuong thuc, dung vong for de kiem tra vi tri cua no
    Lesson findByCourseIdAndId(String courseId, String id);
}
