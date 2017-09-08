package com.disney.studios;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.disney.studios.api.challenge.exceptions.DisneyServiceException;
import com.disney.studios.api.challenge.model.Dog;
import com.disney.studios.api.challenge.model.MyKey;
import com.disney.studios.api.challenge.model.UserVoteMap;
import com.disney.studios.api.challenge.service.DogServiceImpl;
import com.disney.studios.challenge.repositories.DogRepository;
import com.disney.studios.challenge.repositories.UserVoteMapRepo;

public class DogUnitTesting {

	@InjectMocks
	private DogServiceImpl dogService;

	@Mock
	private DogRepository dogRepository;
	
	@Mock
	private UserVoteMapRepo userVoteMapRepo;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	
	@Test
	public void testLikeCount() throws Exception {
		Dog mockDog = new Dog("http://i.imgur.com/xAGJ0Ry.png", "Pug", "Dog with breed Pug", 5, 2);
		Mockito.when(dogRepository.findOne("http://i.imgur.com/xAGJ0Ry.png")).thenReturn(mockDog);
		int count = dogService.totalLikes("http://i.imgur.com/xAGJ0Ry.png");
		Assert.assertEquals("count must be equal to  ",5 , count);
	}
	
	@Test(expected = DisneyServiceException.class)
	public void testLikeCountforNonExistingDog() throws Exception {
		Mockito.when(dogRepository.findOne("http://petsmart.com/dogs/1234.png")).thenReturn(null);
		dogService.totalLikes("http://petsmart.com/dogs/1234.png");
	}
	
	@Test
	public void testVoteFirstTimeByAUser() throws Exception {
		Dog mockDog = new Dog("http://i.imgur.com/xAGJ0Ry.png", "Pug", "Dog with breed Pug", 5, 2);
		String userID = "test_user_1";
		MyKey myKey = new MyKey(userID,"http://i.imgur.com/xAGJ0Ry.png");
		UserVoteMap user = new UserVoteMap(myKey);
		Mockito.when(userVoteMapRepo.findOne(Mockito.any(MyKey.class))).thenReturn(null);
		Mockito.when(userVoteMapRepo.save(user)).thenReturn(user);
		
		Mockito.when(dogRepository.findOne("http://i.imgur.com/xAGJ0Ry.png")).thenReturn(mockDog);
		Mockito.when(dogRepository.save(mockDog)).thenReturn(mockDog);
		
		Dog votedDog = dogService.voteUp("testuser1","http://i.imgur.com/xAGJ0Ry.png");
		
		Assert.assertEquals("the like count of the dog must be ",6 , votedDog.getVoteUp());
	}
	
	/**
	 * This test will return an exception as the user has already voted for the dog
	 * @throws Exception
	 */
	@Test(expected = DisneyServiceException.class)
	public void testVoteBySameUser() throws Exception {
		Dog mockDog = new Dog("http://i.imgur.com/xAGJ0Ry.png", "Pug", "Dog with breed Pug", 5, 2);
		String userID = "test_user_1";
		MyKey myKey = new MyKey(userID,"http://i.imgur.com/xAGJ0Ry.png");
		UserVoteMap user = new UserVoteMap(myKey);
		Mockito.when(userVoteMapRepo.findOne(Mockito.any(MyKey.class))).thenReturn(user);
		Mockito.when(userVoteMapRepo.save(user)).thenReturn(user);
		
		Mockito.when(dogRepository.findOne("http://i.imgur.com/xAGJ0Ry.png")).thenReturn(mockDog);
		Mockito.when(dogRepository.save(mockDog)).thenReturn(mockDog);
		Dog votedDog = dogService.voteUp("testuser1","http://i.imgur.com/xAGJ0Ry.png");
	}

	@Test
	public void testDetails() throws Exception {
		Dog mockDog = new Dog("http://i.imgur.com/xAGJ0Ry.png", "Pug", "Dog with breed Pug", 5, 2);
		Mockito.when(dogRepository.findByPictureurl("http://i.imgur.com/xAGJ0Ry.png")).thenReturn(mockDog);
		dogService.dogDetails("http://i.imgur.com/xAGJ0Ry.png");
		Assert.assertEquals("url must be ", "http://i.imgur.com/xAGJ0Ry.png",
				mockDog.getPictureurl());
	}

	@Test
	public void getByBreed() throws Exception {
		List<Dog> expectedDogs = new ArrayList<Dog>();
		expectedDogs.add(new Dog("http://i.imgur.com/ozJD7SC.png", "Pug", "Dog with breed Pug",  10, 2));
		expectedDogs.add(new Dog("http://i.imgur.com/E5vBM5Z.png", "Pug", "Dog with breed Pug",  0, 1));

		Mockito.when(dogRepository.findByBreed("Pug")).thenReturn(expectedDogs);
		List<String> pics = dogService.getPicturesByBreed("Pug");
		Assert.assertEquals("size must be ", 2, pics.size());
		Assert.assertEquals("the first elsemnt in the list must be ", "http://i.imgur.com/ozJD7SC.png",
				pics.get(0));
	}

	@Test
	public void nonExistingBreed() throws Exception {
		List<Dog> emptyList = new ArrayList<Dog>();
		Mockito.when(dogRepository.findByBreed("Poodle")).thenReturn(emptyList);
		List<String> pics = dogService.getPicturesByBreed("Poodle");
		Assert.assertEquals("the size of the list must be ", 0, pics.size());
	}


}
