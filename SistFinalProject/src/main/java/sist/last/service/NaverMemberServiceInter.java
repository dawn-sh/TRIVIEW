package sist.last.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface NaverMemberServiceInter {

	public String getAccessToken(String code, String state) throws JsonProcessingException;
		
		
	
}
