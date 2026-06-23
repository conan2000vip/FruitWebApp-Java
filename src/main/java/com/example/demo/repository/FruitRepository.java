package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Fruit;

public interface FruitRepository extends JpaRepository<Fruit, Long> {

	List<Fruit> findByNameContainingIgnoreCase(String keyword);

	List<Fruit> findByNameContainingIgnoreCaseAndRegion(String keyword, String region);

	List<Fruit> findByRegion(String region);

	// existence checks used by service/controller
	boolean existsByNameIgnoreCase(String name);
	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
	boolean existsByNameIgnoreCaseAndRegion(String name, String region);
	boolean existsByNameIgnoreCaseAndRegionAndIdNot(String name, String region, Long id);

	Optional<Fruit> findByNameIgnoreCase(String name);

}