package com.example.demo.repository;

import com.example.demo.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CodeRepository  extends JpaRepository<Code, Integer>{

}
