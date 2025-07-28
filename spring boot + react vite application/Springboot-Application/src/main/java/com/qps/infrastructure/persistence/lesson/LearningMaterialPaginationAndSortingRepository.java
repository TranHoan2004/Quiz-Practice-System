package com.qps.infrastructure.persistence.lesson;

import com.qps.domain.lesson.model.LearningMaterial;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningMaterialPaginationAndSortingRepository extends PagingAndSortingRepository<LearningMaterial, String> {
}
