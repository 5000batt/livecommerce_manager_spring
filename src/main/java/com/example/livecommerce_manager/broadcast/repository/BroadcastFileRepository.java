package com.example.livecommerce_manager.broadcast.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.livecommerce_manager.broadcast.BroadcastFile;

@Repository
public interface BroadcastFileRepository extends JpaRepository<BroadcastFile, Long>{
	List<BroadcastFile> findByBroadcastId(long boradcastId);
}
