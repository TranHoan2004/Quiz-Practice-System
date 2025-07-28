package com.qps.infrastructure.persistence.question;

import com.qps.domain.question.model.QuestionMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionMediaRepository extends JpaRepository<QuestionMedia, String> {

}
