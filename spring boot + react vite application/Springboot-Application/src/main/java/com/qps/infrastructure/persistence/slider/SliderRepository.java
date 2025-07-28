package com.qps.infrastructure.persistence.slider;

import com.qps.domain.slider.model.Slider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SliderRepository extends JpaRepository<Slider, Long> {
    List<Slider> findAllByTitleContaining(String keyword);

    // active = true, pageable for top index (Ex: Pageable.of(0, 3) = top 3)
    List<Slider> findByStatusTrue(Pageable pageable);

    List<Slider> findAllByStatusTrue(Pageable pageable);
}
