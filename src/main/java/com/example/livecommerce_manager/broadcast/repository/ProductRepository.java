package com.example.livecommerce_manager.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.livecommerce_manager.broadcast.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByBusinessNumber(String businessNumber);
}
