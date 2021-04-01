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
	private CategoryRepository categoryRepo;

	@Autowired
	public BroadcastController(ProductRepository productRepo, BroadcastRepository broadcastRepo,
			CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.broadcastRepo = broadcastRepo;
		this.categoryRepo = categoryRepo;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getProductData() {
		return productRepo.findAll();
	}

	@RequestMapping(value = "/broadcasts", method = RequestMethod.GET)
	public List<Broadcast> getBroadcastData() {
		return broadcastRepo.findAll();
	}

	@RequestMapping(value = "/categorys", method = RequestMethod.GET)
	public List<Category> getCategoryData() {
		return categoryRepo.findAll();
	}

	@RequestMapping(value = "/broadcasts", method = RequestMethod.POST)
	public Broadcast addBroadcast(@RequestBody Broadcast broadcast) {
		broadcastRepo.save(broadcast);
		return broadcast;
	}

	@RequestMapping(value = "/broadcasts/{id}", method = RequestMethod.PATCH)
	public Broadcast modifyBroadcast(@PathVariable("id") long id, @RequestBody Broadcast broadcast,
			HttpServletResponse res) {

		Broadcast modBroadcast = broadcastRepo.findById(id).orElse(null);

		if (modBroadcast == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		modBroadcast.setBroadcastTitle(broadcast.getBroadcastTitle());
		modBroadcast.setUnitPrice(broadcast.getUnitPrice());
		modBroadcast.setCategory(broadcast.getCategory());
		modBroadcast.setChannelId(broadcast.getChannelId());
		modBroadcast.setProductName(broadcast.getProductName());
		modBroadcast.setProductName(broadcast.getProductName());
//		System.out.println(modBroadcast);

		broadcastRepo.save(modBroadcast);

		return modBroadcast;
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
