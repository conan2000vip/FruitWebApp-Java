package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Fruit;

public interface FruitService {
	List<Fruit> getAll();

	List<Fruit> getLowStockFruits();

	List<Fruit> search(String keyword, String region);

	Fruit getById(Long id);

	Fruit save(Fruit fruit);

	Fruit update(Long id, Fruit fruit);

	boolean existsByNameIgnoreCase(String name);

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

	boolean existsByNameIgnoreCaseAndRegion(String name, String region);

	boolean existsByNameIgnoreCaseAndRegionAndIdNot(String name, String region, Long id);

	void delete(Long id);

	boolean exists(Long id);

	long count();

	int countProducts();

	int totalStock();

	int countLowStock();

}