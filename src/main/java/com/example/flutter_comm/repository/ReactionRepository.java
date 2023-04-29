package com.example.flutter_comm.repository;

import com.example.flutter_comm.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Reaction findFirstByName(String name);

}
