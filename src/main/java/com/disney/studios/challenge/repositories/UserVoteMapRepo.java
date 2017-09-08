package com.disney.studios.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disney.studios.api.challenge.model.MyKey;
import com.disney.studios.api.challenge.model.UserVoteMap;

public interface UserVoteMapRepo extends JpaRepository<UserVoteMap, MyKey>{
}
