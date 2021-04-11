package com.example.livecommerce_manager.broadcast.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BroadcastFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String fileName;
	private String contentType;
	private long broadcastId;

//	public String getDataUrl() {
//		return "https://a552eubt4h.execute-api.ap-northeast-2.amazonaws.com/v1/livecommerce-manager"
//				+ "/broadcast-files/" + this.id;
//	}

	private String dataUrl;
}
