package com.example.livecommerce_manager.broadcast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.livecommerce_manager.broadcast.entity.Broadcast;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {
	Broadcast findByBroadcastTitle(String broadcastTitle);
}
