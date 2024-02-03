package com.example.ddmdemo.respository;

import com.example.ddmdemo.model.Government;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovernmentRepository extends JpaRepository<Government, Integer> {
}
