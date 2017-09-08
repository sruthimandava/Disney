package com.disney.studios.api.challenge.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table
public class Dog implements Serializable{

	@Id
	private String pictureurl;

	private String breed;

	private String details;
	
	private int voteUp;

	private int voteDown;

	/**
	 * 
	 */
	public Dog() {

	}

	public Dog(String pictureurl, String breed, String details, int voteUp, int voteDown) {
		super();
		this.pictureurl = pictureurl;
		this.breed = breed;
		this.details = details;
		this.voteUp = voteUp;
		this.voteDown = voteDown;
	}



	/**
	 * @return the pictureurl
	 */
	public String getPictureurl() {
		return pictureurl;
	}

	/**
	 * @param pictureurl the pictureurl to set
	 */
	public void setPictureurl(String pictureurl) {
		this.pictureurl = pictureurl;
	}

	/**
	 * @return the breed
	 */
	public String getBreed() {
		return breed;
	}

	/**
	 * @param breed the breed to set
	 */
	public void setBreed(String breed) {
		this.breed = breed;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the voteUp
	 */
	public int getVoteUp() {
		return voteUp;
	}

	/**
	 * @param voteUp the voteUp to set
	 */
	public void setVoteUp(int voteUp) {
		this.voteUp = voteUp;
	}

	/**
	 * @return the voteDown
	 */
	public int getVoteDown() {
		return voteDown;
	}

	/**
	 * @param voteDown the voteDown to set
	 */
	public void setVoteDown(int voteDown) {
		this.voteDown = voteDown;
	}

	@Override
	public String toString() {
		return "Dog [pictureurl=" + pictureurl + ", breed=" + breed + ", details=" + details + ", voteUp=" + voteUp
				+ ", voteDown=" + voteDown + "]";
	}

		
}
