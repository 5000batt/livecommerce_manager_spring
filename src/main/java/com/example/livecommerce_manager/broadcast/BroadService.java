package com.example.livecommerce_manager.broadcast;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BroadService {

	private ProductRepository repo;
	private RabbitTemplate rabbit;

	@Autowired
	public BroadService(ProductRepository repo, RabbitTemplate rabbit) {
		this.repo = repo;
		this.rabbit = rabbit;
	}

//	@RabbitListener(queues = "store.manager.product")
//	public void getProductData(Product product) {
//		System.out.println("---- PRODUCT LOG ----");
//		System.out.println(product);
//
//		repo.save(product);
//	}

	public void sendBroadcastData(Broadcast broadcast) {
		System.out.println("---- BROADCAST LOG ----");
		System.out.println(broadcast);

		try {
			rabbit.convertAndSend("live.manager.broadcast", broadcast);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
