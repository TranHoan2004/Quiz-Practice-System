package com.qps.infrastructure.persistence.subject;

import com.qps.domain.subject.model.PersonalSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalSubjectRepository extends JpaRepository<PersonalSubject, String> {
    // PersonalSubjectDAO.existBySubjectIdAndAccountId(String accountId, String subjectId)
    boolean existsBySubjectIdAndAccountId(String subjectId, String accountId);
}
