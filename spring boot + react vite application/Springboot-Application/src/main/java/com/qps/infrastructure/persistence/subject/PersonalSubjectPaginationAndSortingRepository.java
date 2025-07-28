package com.qps.infrastructure.persistence.subject;

import com.qps.domain.subject.model.PersonalSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalSubjectPaginationAndSortingRepository extends PagingAndSortingRepository<PersonalSubject, String> {
    // PersonalSubjectDAO.getPersonalSubjectsByAccount(String id)
    Page<PersonalSubject> findByAccountId(String accountId, Pageable pageable);

    // PersonalSubjectDAO.getPersonalSubjectsByAccountAndSubject(String accountId, String subjectId)
    Page<PersonalSubject> findByAccountIdAndSubjectId(String accountId, String subjectId, Pageable pageable);
}
