package com.example.livecommerce_manager.recording;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RecordingController {
	
	private RecordingRepository recordingRepo;
	
	@Autowired
	public RecordingController(RecordingRepository recordingRepo) {
		this.recordingRepo = recordingRepo;
	}
	
	@RequestMapping(value = "/recordings", method = RequestMethod.POST)
	public Recording addRecording(@RequestBody Recording recording) {

		recordingRepo.save(recording);
		return recording;
	}
	
	@RequestMapping(value = "/recordings", method = RequestMethod.GET)
	public List<Recording> getRecordingData() {
		return recordingRepo.findAll();
	}
}
