package com.example.livecommerce_manager.broadcast;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.livecommerce_manager.broadcast.entity.Broadcast;
import com.example.livecommerce_manager.broadcast.entity.Product;
import com.example.livecommerce_manager.broadcast.repository.ProductRepository;

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

	@RabbitListener(bindings = {
			@QueueBinding(exchange = @Exchange(name = "amq.topic", type = "topic"), value = @Queue(value = "mdm.product.1"), key = {
					"mdm.product" }), })
	public void receiveProduct1(Product product) {
		System.out.println("--product.1 --");
		System.out.println(product);

		Product broadProduct = Product.builder().businessNumber(product.getBusinessNumber()).price(product.getPrice())
				.name(product.getName()).images(product.getImages()).build();

		System.out.println(broadProduct);
		repo.save(broadProduct);

	}

	public void sendBroadcastData(Broadcast broadcast) {
		System.out.println("---- BROADCAST LOG ----");
		System.out.println(broadcast);

		rabbit.convertAndSend("commerce.broadcast", broadcast);
	}
}
