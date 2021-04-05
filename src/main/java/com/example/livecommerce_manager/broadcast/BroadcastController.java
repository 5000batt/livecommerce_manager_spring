package com.example.livecommerce_manager.broadcast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.livecommerce_manager.broadcast.entity.Broadcast;
import com.example.livecommerce_manager.broadcast.entity.Category;
import com.example.livecommerce_manager.broadcast.entity.Product;
import com.example.livecommerce_manager.broadcast.repository.BroadcastFileRepository;
import com.example.livecommerce_manager.broadcast.repository.BroadcastRepository;
import com.example.livecommerce_manager.broadcast.repository.CategoryRepository;
import com.example.livecommerce_manager.broadcast.repository.ProductRepository;
import com.example.livecommerce_manager.configuration.ApiConfiguration;

@RestController
public class BroadcastController {

	private ProductRepository productRepo;
	private BroadcastRepository broadcastRepo;
	private CategoryRepository categoryRepo;
	private BroadcastFileRepository broadcastFileRepo;
	private final Path FILE_PATH = Paths.get("broadcast_file");
	private BroadService service;

	@Autowired
	private ApiConfiguration apiConfig;

	@Autowired
	public BroadcastController(ProductRepository productRepo, BroadcastRepository broadcastRepo,
			CategoryRepository categoryRepo, BroadService service, BroadcastFileRepository broadcastFileRepo) {
		this.productRepo = productRepo;
		this.broadcastRepo = broadcastRepo;
		this.categoryRepo = categoryRepo;
		this.broadcastFileRepo = broadcastFileRepo;
		this.service = service;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public Product getProductData(@RequestParam("businessNumber") String businessNumber) {
		return productRepo.findByBusinessNumber(businessNumber);
	}

	@RequestMapping(value = "/broadcasts", method = RequestMethod.GET)
	public List<Broadcast> getBroadcastData() {
		List<Broadcast> list = broadcastRepo.findAll(Sort.by("id").descending());
		for (Broadcast broadcast : list) {
			for (BroadcastFile file : broadcast.getFiles()) {
				file.setDataUrl(apiConfig.getBasePath() + "/broadcast-files/" + file.getId());
			}
		}
		return list;
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
		modBroadcast.setPrice(broadcast.getPrice());
		modBroadcast.setCategory(broadcast.getCategory());
		modBroadcast.setChannelId(broadcast.getChannelId());
		modBroadcast.setProductName(broadcast.getProductName());
		modBroadcast.setImages(broadcast.getImages());
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

	@RequestMapping(value = "/broadcasts/{id}/broadcast-files", method = RequestMethod.POST)
	public BroadcastFile addBroadcastFile(@PathVariable("id") long id, @RequestPart("data") MultipartFile file,
			HttpServletResponse res) throws IOException {

		if (broadcastRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		System.out.println(file.getOriginalFilename());

		if (!Files.exists(FILE_PATH)) {
			Files.createDirectories(FILE_PATH);
		}
		FileCopyUtils.copy(file.getBytes(), new File(FILE_PATH.resolve(file.getOriginalFilename()).toString()));

		BroadcastFile broadcastFile = BroadcastFile.builder().broadcastId(id).fileName(file.getOriginalFilename())
				.contentType(file.getContentType()).build();
		broadcastFileRepo.save(broadcastFile);
		return broadcastFile;
	}

	@RequestMapping(value = "/register-broadcasts/{id}", method = RequestMethod.POST)
	public boolean registerBroadcast(@PathVariable("id") long id, @RequestBody Broadcast broadcast,
			HttpServletResponse res) {

		Broadcast regBroadcast = broadcastRepo.findById(id).orElse(null);

		if (regBroadcast == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}
		service.sendBroadcastData(broadcast);
		return true;
	}
}
