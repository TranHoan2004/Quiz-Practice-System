package com.qps.infrastructure.persistence.setting;

import com.qps.application.dto.response.BlogResp;
import com.qps.domain.setting.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingForBlogRepository extends JpaRepository<Setting, String> {
    // BlogDAO.getCategories(String category)
    @Query("""
            SELECT DISTINCT s.id, s.value
            FROM Setting s
            WHERE s.settingType.name = :name
            """)
    List<BlogResp> findAllCategories(@Param("name") String category);
}
