package com.qps.infrastructure.persistence.question;

import com.qps.domain.question.model.QuestionMedia;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionMediaPaginationAndSortingRepository extends PagingAndSortingRepository<QuestionMedia, String> {
}
