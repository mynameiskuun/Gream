package com.project.gream.domain.post.repository;

import com.project.gream.domain.post.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
