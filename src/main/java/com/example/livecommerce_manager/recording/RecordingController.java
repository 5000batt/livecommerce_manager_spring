package com.example.livecommerce_manager.recording;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

	// 녹화추가
	@RequestMapping(value = "/recordings", method = RequestMethod.POST)
	public Recording addRecording(@RequestBody Recording recording) {

		recordingRepo.save(recording);
		return recording;
	}

	// 녹화조회
	@RequestMapping(value = "/recordings", method = RequestMethod.GET)
	public List<Recording> getRecordingData() {
		return recordingRepo.findAll();
	}

	// 녹화삭제
	@RequestMapping(value = "/recordings/{id}", method = RequestMethod.DELETE)
	public boolean removeRecording(@PathVariable("id") long id, HttpServletResponse res) {

		Recording recording = recordingRepo.findById(id).orElse(null);

		if (recording == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}

		recordingRepo.deleteById(id);

		return true;

	}
}
