package com.project.gream.domain.item.repository;

import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImgRepository extends JpaRepository<Img, Long> {

    @Query(value = "select img from Img img where img.item.id = :id and img.review.id = null")
    List<Img> findItemImgs(@Param("id") Long itemId);

}
