package com.ssafy.cob.domain.chat.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/uploads")
public class FileUploadController {
	
	@Autowired
	private HttpServletRequest request;


    // ✅ 실제 파일이 저장될 경로 (WebConfig에서 매핑된 경로와 일치해야 함)
	private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/uploads/";

	@PostMapping
	public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
	    try {
	        File dir = new File(UPLOAD_DIR);
	        if (!dir.exists()) dir.mkdirs();

	        String safeFileName = UUID.randomUUID() + "_" +
	            file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_");

	        File dest = new File(UPLOAD_DIR + safeFileName);
	        file.transferTo(dest);

	        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	        String fileUrl = serverUrl + "/uploads/download/" + safeFileName;
	        System.out.println(serverUrl);

	        return ResponseEntity.ok(Map.of("url", fileUrl));

	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
	    }
	}
	
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> download(@PathVariable String filename) throws IOException {
	    String decodedName = URLDecoder.decode(filename, "UTF-8");
	    File file = new File(UPLOAD_DIR + decodedName);

	    if (!file.exists()) {
	        return ResponseEntity.status(404).build(); // 파일 없을 경우 명확하게 처리
	    }

	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(decodedName, "UTF-8"))
	            .contentLength(file.length())
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(resource);
	}

	

}
