package com.suygecu.teamly_service;


import com.suygecu.teamly_service.dto.outline.OutlineDocumentResponse;
import com.suygecu.teamly_service.outline.service.OutlineClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TestRestApi {

	@Autowired
	private OutlineClient outlineClient;

	private OutlineDocumentResponse outlineDocumentResponse;


	@Test
	void test() {

		OutlineDocumentResponse response = outlineClient.getDocumentInfo("fRKE1uTtes");
		String text = response.getData().getText() ;
		List<String> list = new ArrayList<>();
		list.add(text);


		System.out.println(list);



	}


}
