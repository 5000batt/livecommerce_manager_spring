package com.example.livecommerce_manager.broadcast;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BroadcastController {

	private ProductRepository productRepo;

	@Autowired
	public BroadcastController(ProductRepository productRepo) {
		this.productRepo = productRepo;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getProductDate() {
		return productRepo.findAll();
	}

}
