package com.disney.studios.api.challenge.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.disney.studios.api.challenge.biz.Converter;
import com.disney.studios.api.challenge.exceptions.DisneyServiceException;
import com.disney.studios.api.challenge.model.Dog;
import com.disney.studios.api.challenge.service.DogService;

@RestController
@RequestMapping("/api-challenge")
public class DogController {

	@Autowired
	DogService dogService;

	private final Logger logger = LoggerFactory.getLogger(DogController.class);

	/**
	 * gives list of all the dog pictures
	 * 
	 * @param response
	 * @return - returns all the dog pictures
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Map<String, List<String>> get(HttpServletResponse response) {
		Map<String, List<String>> alldogpics = dogService.getAllDogPictures();
		return alldogpics;
	}

	/**
	 * gives list of all the dog pictures based on the breed
	 * 
	 * @param response
	 * @param breed
	 *            - name of the breed
	 * @return - returns list of all the dog pictures based on the breed
	 * @throws IOException
	 */
	@RequestMapping(value = "/{breed}", method = RequestMethod.GET)
	public Map<String, List<String>> getPicturesByBreed(HttpServletResponse response,
			@PathVariable("breed") String breed) throws IOException {
		Map<String, List<String>> picurlbybreed = new HashMap<String, List<String>>();
		List<String> pictures = null;
		try {
			pictures = dogService.getPicturesByBreed(breed);
			picurlbybreed.put(breed, pictures);
			logger.debug("count for " + breed + " " + pictures.size());
		} catch (Exception e) {
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, "internal server error occured");
		}
		return picurlbybreed;
	}

	@RequestMapping(value = "/vote-up", method = RequestMethod.POST)
	public Dog voteup(HttpServletResponse response, @RequestParam("url") String url,
			@RequestHeader("username") String userName) throws Exception {
		Dog dog = null;
		try {
			dog = dogService.voteUp(userName, url);
		} catch (DisneyServiceException de) {
			logger.error("error while getting count " + de.getMessage());
			response.sendError(de.getExceptionCode(), de.getMessage());
		} catch (Exception e) {
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, "internal server error occured");
		}
		return dog;

	}

	@RequestMapping(value = "/vote-down", method = RequestMethod.POST)
	public Dog votedown(HttpServletResponse response, @RequestParam("url") String url,
			@RequestHeader("username") String userName) throws IOException {
		Dog dog = null;
		try {
			dog = dogService.voteDown(userName, url);
		} catch (DisneyServiceException de) {
			response.sendError(response.SC_NOT_FOUND, de.getMessage());
		} catch (Exception e) {
			logger.error("error while getting unlike count " + e.getMessage());
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, "internal server error occured");
		}
		return dog;
	}

	@RequestMapping(value = "/dog-details", method = RequestMethod.GET)
	public Dog details(HttpServletResponse response, @RequestParam("url") String url) throws IOException {
		Dog details = null;
		try {
			details = dogService.dogDetails(url);
			logger.debug(" details : " + details);
		} catch (DisneyServiceException de) {
			logger.error("error  while fetching dog details " + de.getMessage());
			response.sendError(response.SC_NOT_FOUND, de.getMessage());
		} catch (Exception e) {
			logger.error("error while fetching dog details " + e.getMessage());
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, "internal server error occured");
		}
		return details;
	}

	@RequestMapping(value = "/total-likes", method = RequestMethod.GET)
	public String likeCount(HttpServletResponse response, @RequestParam("url") String url) throws IOException {
		String resp = null;
		try {
			int likeCount = dogService.totalLikes(url);
			logger.debug("like-count " + likeCount);
			Map<String, String> map = new HashMap<String, String>();
			map.put("like-count", String.valueOf(likeCount));
			resp = Converter.getJSONString(map);
		} catch (DisneyServiceException de) {
			response.sendError(response.SC_NOT_FOUND,de.getMessage());
		} catch (Exception e) {
			response.sendError(response.SC_INTERNAL_SERVER_ERROR, "internal server error occured");
		}
		return resp;
	}

}