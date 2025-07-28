package com.qps.infrastructure.persistence.setting;

import com.qps.domain.setting.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingForAccountRepository extends JpaRepository<Setting, String> {
    // AccountDAO.getRoleIdByRoleName(String role)
    @Query("""
            SELECT id FROM Setting WHERE value = :value
            """)
    String findRoleIdByValue(@Param("value") String value);

    // AccountDAO.getRoleIdByRoleName(String role)
    @Query("""
            SELECT value FROM Setting WHERE id = :id
            """)
    String findRoleNameById(@Param("id") String id);
}
