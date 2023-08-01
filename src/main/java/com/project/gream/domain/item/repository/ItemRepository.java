package com.project.gream.domain.item.repository;

import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

//    Page<Item> findByGender(Gender gender, Pageable pageable);
    Page<Item> findByGender(Gender gender, Pageable pabeagle);
    Page<Item> findAllByIdIn(List<Long> id, Pageable pageable);
    List<Item> getByGender(Gender gender);
//    Page<Item> findAllByCategoryOrderByCreatedTime(Category category, Pageable pageable);
    List<Item> findAllByCategoryOrderByCreatedTime(Category category);
}
