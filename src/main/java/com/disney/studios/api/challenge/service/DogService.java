package com.disney.studios.api.challenge.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.disney.studios.api.challenge.exceptions.DisneyServiceException;
import com.disney.studios.api.challenge.model.Dog;
import com.disney.studios.api.challenge.model.UserVoteMap;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface DogService {

	public List<String> getPicturesByBreed(String breed);
	
	public Dog voteUp(String userName, String url) throws DisneyServiceException;
	
	public Dog voteDown(String userName, String url) throws DisneyServiceException;
	
	public Dog dogDetails(String url)  throws DisneyServiceException, JsonParseException, JsonMappingException, IOException;
	
	public int totalLikes(String url) throws DisneyServiceException;
	
	public Map<String, List<String>> getAllDogPictures();
	
}
