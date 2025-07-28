package com.qps.infrastructure.persistence.course;

import com.qps.domain.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    // CourseDAO.getByTopic(String topicId)
    Course findByTopicId(String topicId);

    // getTotalRegistrationDto
}
