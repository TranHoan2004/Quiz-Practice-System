package com.qps.infrastructure.persistence.option;

import com.qps.domain.option.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, String> {
    // OptionDAO.getOptionsByQuestionId(String questionId)
    List<Option> findByQuestionId(String questionId);
}
