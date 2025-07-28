package com.qps.infrastructure.persistence.pricepackage;

import com.qps.domain.pricepackage.model.PricePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePackageRepository extends JpaRepository<PricePackage, String> {
}
