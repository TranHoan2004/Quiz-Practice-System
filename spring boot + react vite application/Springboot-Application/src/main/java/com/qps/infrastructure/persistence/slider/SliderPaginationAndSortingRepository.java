package com.qps.infrastructure.persistence.slider;

import com.qps.domain.slider.model.Slider;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SliderPaginationAndSortingRepository extends PagingAndSortingRepository<Slider, Long> {
}
