package com.example.livecommerce_manager.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.livecommerce_manager.broadcast.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
