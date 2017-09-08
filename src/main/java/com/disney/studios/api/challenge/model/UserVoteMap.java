package com.disney.studios.api.challenge.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class UserVoteMap implements Serializable {
	
	@EmbeddedId
	MyKey myKey;

	/**
	 * @return the myKey
	 */
	public MyKey getMyKey() {
		return myKey;
	}

	/**
	 * @param myKey
	 *            the myKey to set
	 */
	public void setMyKey(MyKey myKey) {
		this.myKey = myKey;
	}

	public UserVoteMap(MyKey myKey) {
		super();
		this.myKey = myKey;
	}

	public UserVoteMap()
	{
		
	}
}
