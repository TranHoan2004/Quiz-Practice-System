package com.qps.infrastructure.persistence.quiz;

import com.qps.domain.quiz.model.PersonalQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalQuizPaginationAndSortingRepository extends PagingAndSortingRepository<PersonalQuiz, String> {
    // PersonalQuizDAO.getAllByAccount(String id)
    Page<PersonalQuiz> findByAccountId(Pageable pageable, String accountId);
}
