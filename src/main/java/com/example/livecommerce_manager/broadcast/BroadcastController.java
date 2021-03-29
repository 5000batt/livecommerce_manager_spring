package com.example.livecommerce_manager.broadcast;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BroadcastController {

	private ProductRepository productRepo;
	private BroadcastRepository broadcastRepo;

	@Autowired
	public BroadcastController(ProductRepository productRepo, BroadcastRepository broadcastRepo) {
		this.productRepo = productRepo;
		this.broadcastRepo = broadcastRepo;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getProductData() {
		return productRepo.findAll();
	}

	@RequestMapping(value = "/broadcasts", method = RequestMethod.GET)
	public List<Broadcast> getBroadcastData() {
		return broadcastRepo.findAll();
	}

	@RequestMapping(value = "/broadcasts", method = RequestMethod.POST)
	public Broadcast addBroadcast(@RequestBody Broadcast broadcast) {
		broadcastRepo.save(broadcast);
		return broadcast;
	}

	@RequestMapping(value = "/broadcasts/{id}", method = RequestMethod.DELETE)
	public boolean removeBroadcast(@PathVariable("id") long id, HttpServletResponse res) {

		Broadcast broadcast = broadcastRepo.findById(id).orElse(null);

		if (broadcast == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		broadcastRepo.deleteById(id);

		return true;
	}

}
