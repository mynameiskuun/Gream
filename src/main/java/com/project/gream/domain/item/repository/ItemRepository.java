package com.project.gream.domain.item.repository;

import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByGender(Gender gender);
    List<Item> findAllByCategoryOrderByCreatedTime(Category category);
}
