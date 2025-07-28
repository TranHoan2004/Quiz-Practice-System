package com.qps.infrastructure.persistence.subject;

import com.qps.domain.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("""
            SELECT s.value
            FROM SettingSubject ss, Setting s, SettingType stt
            WHERE ss.setting.id = s.id AND s.settingType.id = stt.id
            AND ss.id = :id AND stt.name = 'Blog Category'
            """)
    String findCategoryBySubjectId(@Param("id") Long subjectId);

    @Query("""
            SELECT DISTINCT s.value
            FROM SettingSubject ss, Setting s, SettingType stt
            WHERE ss.setting.id = s.id AND stt.id = s.id
            AND stt.name IN ('Domain', 'Group')
            """)
    List<String> findAllCategories();
}