package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.BlackWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackWordRepository extends JpaRepository<BlackWord, Long> {
}
