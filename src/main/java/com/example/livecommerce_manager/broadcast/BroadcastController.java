package com.example.livecommerce_manager.broadcast;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.livecommerce_manager.broadcast.entity.Broadcast;
import com.example.livecommerce_manager.broadcast.entity.BroadcastFile;
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

	// 상품조회
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public List<Product> getProductData() {
		return productRepo.findAll();
	}

	// 방송조회
	@RequestMapping(value = "/broadcasts", method = RequestMethod.GET)
	public List<Broadcast> getBroadcastData() {
		List<Broadcast> list = broadcastRepo.findAll(Sort.by("id").descending());
		for (Broadcast broadcast : list) {
			for (BroadcastFile file : broadcast.getFiles()) {
				file.setDataUrl(apiConfig.getBasePath() + "/broadcast-files/" + file.getId());
//				broadcastFileRepo.save(file);
			}
		}

		return list;
	}

	// 카테고리조회
	@RequestMapping(value = "/categorys", method = RequestMethod.GET)
	public List<Category> getCategoryData() {
		return categoryRepo.findAll();
	}

	// 방송추가
	@RequestMapping(value = "/broadcasts", method = RequestMethod.POST)
	public Broadcast addBroadcast(@RequestBody Broadcast broadcast) {

		broadcastRepo.save(broadcast);
		return broadcast;
	}

	// 방송수정
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

	// 방송삭제
	@RequestMapping(value = "/broadcasts/{id}", method = RequestMethod.DELETE)
	public boolean removeBroadcast(@PathVariable("id") long id, HttpServletResponse res) {

		Broadcast broadcast = broadcastRepo.findById(id).orElse(null);

		if (broadcast == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		List<BroadcastFile> files = broadcastFileRepo.findByBroadcastId(id);
		for (BroadcastFile file : files) {
			broadcastFileRepo.delete(file);
		}

		broadcastRepo.deleteById(id);

		return true;
	}

	// 파일 추가
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

	// 파일 목록 삭제
	@RequestMapping(value = "/broadcasts/{id}/broadcast-files", method = RequestMethod.DELETE)
	public boolean removeBroadcastFiles(@PathVariable("id") long id, HttpServletResponse res) {

		if (broadcastRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		List<BroadcastFile> broadcastFiles = broadcastFileRepo.findByBroadcastId(id);
		for (BroadcastFile broadcastFile : broadcastFiles) {
			broadcastFileRepo.delete(broadcastFile);
			File file = new File(broadcastFile.getFileName());
			if (file.exists()) {
				file.delete();
			}
		}
		return true;
	}

	// 파일 목록 조회
	@RequestMapping(value = "/broadcasts/{id}/broadcast-files", method = RequestMethod.GET)
	public List<BroadcastFile> getBroadcastFiles(@PathVariable("id") long id, HttpServletResponse res) {

		if (broadcastRepo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		List<BroadcastFile> broadcastFiles = broadcastFileRepo.findByBroadcastId(id);

		return broadcastFiles;
	}

	@RequestMapping(value = "/broadcast-files/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getBroadFile(@PathVariable("id") long id, HttpServletResponse res)
			throws IOException {
		BroadcastFile broadcastFile = broadcastFileRepo.findById(id).orElse(null);

		if (broadcastFile == null) {
			return ResponseEntity.notFound().build();
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", broadcastFile.getContentType() + ";charset=UTF-8");
		responseHeaders.set("Content-Disposition",
				"inline; filename=" + URLEncoder.encode(broadcastFile.getFileName(), "UTF-8"));

		return ResponseEntity.ok().headers(responseHeaders)
				.body(Files.readAllBytes(FILE_PATH.resolve(broadcastFile.getFileName())));

	}

	@RequestMapping(value = "/register-broadcasts/{id}", method = RequestMethod.POST)
	public boolean registerBroadcast(@PathVariable("id") long id, @RequestBody Broadcast broadcast,
			HttpServletResponse res) {

		if (broadcast == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}
		service.sendBroadcastData(broadcast);
		return true;
	}
}
