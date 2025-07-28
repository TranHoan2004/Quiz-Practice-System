package com.qps.infrastructure.persistence.setting;

import com.qps.domain.setting.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    @Query("""
            SELECT setting.id FROM SettingSubject WHERE subject.id = :id
            """)
    List<String> findBySubject(@Param("id") String id);

    @Query("""
            SELECT value FROM Setting WHERE settingType.id = :id
            """)
    List<String> findValueBySettingTypeId(@Param("id") String id);
}
