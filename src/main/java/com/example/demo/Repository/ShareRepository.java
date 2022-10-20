
package com.example.demo.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Model.*;

import java.util.List;

@Repository
public interface ShareRepository extends PagingAndSortingRepository<Shares,Integer > {


    Shares findById(User user);

    Object findAllById(User user);

    List<Shares> findAll();
}