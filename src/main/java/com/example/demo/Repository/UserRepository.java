
package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.User;
import com.example.demo.Model.*;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.username = ?1")
	public User findByUsername(String username);
	

}