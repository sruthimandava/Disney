package com.disney.studios.api.challenge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.disney.studios.api.challenge.exceptions.DisneyServiceException;
import com.disney.studios.api.challenge.model.Dog;
import com.disney.studios.api.challenge.model.MyKey;
import com.disney.studios.api.challenge.model.UserVoteMap;
import com.disney.studios.challenge.repositories.DogRepository;
import com.disney.studios.challenge.repositories.UserVoteMapRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DogServiceImpl implements DogService {

	@Autowired
	DogRepository dogRepository;

	@Autowired
	UserVoteMapRepo userVoteMapRepo;

	@Autowired
	ObjectMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<String> getPicturesByBreed(String breed) {
		Comparator<Dog> comparator = new Comparator<Dog>() {
			@Override
			public int compare(Dog dog1, Dog dog2) {
				return Integer.compare(dog2.getVoteUp(), dog1.getVoteUp());
			}
		};
		List<Dog> dogs = dogRepository.findByBreed(breed);
		List<String> picurl = new ArrayList<String>();
		Collections.sort(dogs, comparator);
		for (Dog dog : dogs) {
			String url = dog.getPictureurl();
			picurl.add(url);
		}
		return picurl;
	}

	public Dog voteUp(String userName, String url) throws DisneyServiceException {

		boolean isAlreadyVoted = isAlreadyVoted(userName, url);
		if (!isAlreadyVoted) {
			Dog dog = dogRepository.findOne(url);
			 logger.debug("details " + dog);
			if (dog == null) {
				DisneyServiceException dse = new DisneyServiceException("Dog with pic " + url + " not found");
				dse.setExceptionCode(201);
				throw dse;
			} else {
				int likeCount = dog.getVoteUp();
				dog.setVoteUp(likeCount + 1);
			}
			dog = dogRepository.save(dog);
			return dog;
		} else {
			DisneyServiceException dse = new DisneyServiceException(userName + " already voted for dog pic " + url);
			dse.setExceptionCode(500);
			throw dse;
		}

	}

	public Dog voteDown(String userName, String url) throws DisneyServiceException {
		Dog dog = dogRepository.findOne(url);
		if (dog == null) {
			DisneyServiceException dse = new DisneyServiceException("Dog with pic " + url + " not found");
			dse.setExceptionCode(201);
			throw dse;
		} else {
			int unlikeCount = dog.getVoteDown();
			dog.setVoteDown(unlikeCount + 1);
		}
		dog = dogRepository.save(dog);
		return dog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.disney.service.DogService#getDogDetails(java.lang.String)
	 */
	@Override
	public Dog dogDetails(String picurl) throws DisneyServiceException {
		Dog dog = dogRepository.findByPictureurl(picurl);
		 logger.debug(" details " + dog);
		if (dog == null) {
			throw new DisneyServiceException("Dog with pic " + picurl + " not found");
		} else {
			return dog;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.disney.service.DogService#getLikes(java.lang.String)
	 */
	@Override
	public int totalLikes(String url) throws DisneyServiceException {
		Dog dog = dogRepository.findOne(url);
		 logger.debug("details " + dog);
		if (dog == null) {
			throw new DisneyServiceException("Dog with pic " + url + " not found");
		} else {
			int likes = dog.getVoteUp();
			return likes;
		}
	}

	@Override
	public Map<String, List<String>> getAllDogPictures() {

		Comparator<Dog> likeCountComparator = getComp();
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> breeds = dogRepository.findDistinctBreeds();
		for (String breed : breeds) {
			List<Dog> dogs = dogRepository.findByBreed(breed);
			if (breeds.size() > 0) {
				Collections.sort(dogs, likeCountComparator);
			}
			List<String> imgs = new ArrayList<String>();
			for (Dog dog : dogs) {
				String dogpic = dog.getPictureurl();
				imgs.add(dogpic);
			}
			map.put(breed, imgs);
		}
		return map;
	}

	private Comparator<Dog> getComp() {
		Comparator<Dog> likeCountComparator = new Comparator<Dog>() {
			@Override
			public int compare(Dog d1, Dog d2) {
				return Integer.compare(d2.getVoteUp(), d1.getVoteDown());
			}
		};
		return likeCountComparator;
	}

	public boolean isAlreadyVoted(String userName, String url) {
		MyKey mk = new MyKey(userName, url);
		UserVoteMap user = null;
		user = userVoteMapRepo.findOne(mk);
		if (user == null) {
			user = new UserVoteMap(mk);
			userVoteMapRepo.save(user);
			return false;
		} else {
			return true;
		}
	}

}
