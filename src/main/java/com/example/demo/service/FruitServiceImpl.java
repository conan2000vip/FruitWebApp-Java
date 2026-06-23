package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Fruit;
import com.example.demo.repository.FruitRepository;

@Service
public class FruitServiceImpl implements FruitService {

	private static final int LOW_STOCK_THRESHOLD = 5;

	@Autowired
	private FruitRepository fruitRepository;

	@Override
	// 全てのフルーツを取得
	public List<Fruit> getAll() {
		return fruitRepository.findAll();
	}

	@Override
	// 名前でフルーツを検索
	public List<Fruit> search(String keyword, String region) {

		boolean hasKeyword = keyword != null && !keyword.isBlank();
		boolean hasRegion = region != null && !region.isBlank();

		if (hasKeyword && hasRegion) {
			return fruitRepository.findByNameContainingIgnoreCaseAndRegion(keyword, region);
		}

		if (hasKeyword) {
			return fruitRepository.findByNameContainingIgnoreCase(keyword);
		}

		if (hasRegion) {
			return fruitRepository.findByRegion(region);
		}

		return fruitRepository.findAll();
	}

	@Override
	public Fruit getById(Long id) {
		return fruitRepository.findById(id).orElse(null);
	}

	@Override
	public Fruit save(Fruit fruit) {
		return fruitRepository.save(fruit);
	}

	@Override
	public Fruit update(Long id, Fruit fruit) {
		Fruit oldFruit = fruitRepository
				.findById(id)
				.orElseThrow(() -> new RuntimeException(
						"Fruit not found"));

		oldFruit.setName(fruit.getName());
		oldFruit.setDescription(fruit.getDescription());
		oldFruit.setPrice(fruit.getPrice());
		oldFruit.setQuantity(fruit.getQuantity());
		oldFruit.setImageUrl(fruit.getImageUrl());

		return fruitRepository.save(oldFruit);
	}

	@Override
	public boolean existsByNameIgnoreCase(String name) {
		return fruitRepository.existsByNameIgnoreCase(name);
	}

	@Override
	public boolean existsByNameIgnoreCaseAndIdNot(String name, Long id) {
		return fruitRepository.existsByNameIgnoreCaseAndIdNot(name, id);
	}

	@Override
	public boolean existsByNameIgnoreCaseAndRegion(String name, String region) {
		return fruitRepository.existsByNameIgnoreCaseAndRegion(name, region);
	}

	@Override
	public boolean existsByNameIgnoreCaseAndRegionAndIdNot(String name, String region, Long id) {
		return fruitRepository.existsByNameIgnoreCaseAndRegionAndIdNot(name, region, id);
	}

	@Override
	public void delete(Long id) {
		fruitRepository.deleteById(id);
	}

	@Override
	public boolean exists(Long id) {
		return fruitRepository.existsById(id);
	}

	@Override
	public long count() {
		return fruitRepository.count();
	}

	@Override
	public List<Fruit> getLowStockFruits() {
		return fruitRepository.findAll().stream()
				.filter(fruit -> fruit.getQuantity() != null && fruit.getQuantity() > 0
						&& fruit.getQuantity() <= LOW_STOCK_THRESHOLD)
				.toList();
	}

	@Override
	public int countProducts() {
		return (int) fruitRepository.count();
	}

	@Override
	public int totalStock() {
		return fruitRepository.findAll().stream()
				.mapToInt(fruit -> {
					Integer quantity = fruit.getQuantity();
					return quantity != null ? quantity : 0;
				})
				.sum();
	}

	@Override
	public int countLowStock() {
		return (int) fruitRepository.findAll().stream()
				.filter(fruit -> {
					Integer quantity = fruit.getQuantity();
					return quantity != null && quantity > 0 && quantity <= LOW_STOCK_THRESHOLD;
				})
				.count();
	}

}