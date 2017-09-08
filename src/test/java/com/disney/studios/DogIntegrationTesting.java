package com.disney.studios;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.disney.studios.api.challenge.model.Dog;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@IntegrationTest("server.port:0")
public class DogIntegrationTesting {
	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${local.server.port}")
	private int port;

	@Test
	public void getDogs() throws Exception {
		final HttpEntity<String> requestEntity = new HttpEntity<String>(new HttpHeaders());
		Set<String> set = Stream.of("Yorkie", "Labrador","Pug","Retriever").collect(Collectors.toSet());
		try {
			Object[] obj = new Object[] {};
			final ResponseEntity<Map> resp = restTemplate.exchange("/api-challenge/", HttpMethod.GET, requestEntity, Map.class,	obj);
			final Map<String, String> entries = resp.getBody();
			final Set<String> breeds = entries.keySet();
			for(String breed: breeds)
			{
				Assert.assertTrue("must contain ",set.contains(breed));
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	@Test
	public void testVoteSameUser() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "test-user-2");
		final HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		String picurl = "http://i.imgur.com/Rdbawjx.png";
		try {
			Object[] obj = new Object[] {};
			ResponseEntity<Dog> entity = restTemplate.exchange("/api-challenge/vote-up?url="+picurl, HttpMethod.POST, requestEntity, Dog.class,
					obj);
			Dog dog = entity.getBody();
			Assert.assertEquals("the count at this point must be ",1,dog.getVoteUp());
			entity = restTemplate.exchange("/api-challenge/vote-up?url="+picurl, HttpMethod.POST, requestEntity, Dog.class,
					obj);
			Assert.assertEquals("they http error code must be 500 ",HttpStatus.INTERNAL_SERVER_ERROR,entity.getStatusCode());
			
			
			ResponseEntity<String> ret = restTemplate.exchange(
					"/api-challenge/total-likes?url="+picurl, HttpMethod.GET, requestEntity, String.class,
					obj);
		    String count = ret.getBody();
			JSONParser parser = new JSONParser();
			JSONObject j = (JSONObject) parser.parse(count);
			String ct = (String) j.get("like-count");
			Assert.assertEquals("the count must be ","1",ct);
			
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testVote() throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.add("username", "test-user-1");
		final HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		try {
			Object[] obj = new Object[] {};
			ResponseEntity<Dog> entity = restTemplate.exchange("/api-challenge/vote-up?url=http://i.imgur.com/wR38uBx.png", HttpMethod.POST, requestEntity, Dog.class,
					obj);
			Dog dog = entity.getBody();
			Assert.assertEquals("the count at this point must be ",1,dog.getVoteUp());
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	

}
