package com.abcdefg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcdefg.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User getByUid(String uid);
}
