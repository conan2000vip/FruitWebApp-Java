package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Fruit;

public interface FruitService {
	List<Fruit> getAll();

	List<Fruit> search(String keyword, String region);

	Fruit getById(Long id);

	Fruit save(Fruit fruit);

	Fruit update(Long id, Fruit fruit);

	void delete(Long id);

	boolean exists(Long id);

	long count();

}