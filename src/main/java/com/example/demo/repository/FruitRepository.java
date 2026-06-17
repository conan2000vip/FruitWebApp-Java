package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Fruit;

public interface FruitRepository extends JpaRepository<Fruit, Long> {

	List<Fruit> findByNameContainingIgnoreCase(String keyword);

	List<Fruit> findByNameContaining(String keyword);
}
