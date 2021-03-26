package com.example.livecommerce_manager.broadcast;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast, Long> {
	Broadcast findByBroadcastTitle(String broadcastTitle);
}
