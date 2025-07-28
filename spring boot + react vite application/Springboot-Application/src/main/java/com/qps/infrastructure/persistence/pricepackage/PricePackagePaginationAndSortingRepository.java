package com.qps.infrastructure.persistence.pricepackage;

import com.qps.domain.pricepackage.model.PricePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePackagePaginationAndSortingRepository extends PagingAndSortingRepository<PricePackage, String> {
    // PricePackageDAO.getByCourse(String id)
    Page<PricePackage> findByCourseId(String courseId, Pageable pageable);
}
