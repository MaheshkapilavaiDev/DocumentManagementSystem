package com.dms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	//Optional<User> findByEmail(String string);

	//void save(User user);

	Optional<User> findByEmail(String email);

}
