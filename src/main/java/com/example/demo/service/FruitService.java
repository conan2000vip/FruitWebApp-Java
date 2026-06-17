package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Fruit;

public interface FruitService {

	// 全果物取得
	List<Fruit> getAll();

	// 探す
	List<Fruit> searchByName(String keyword);

	// 果物IDによる果物取得
	Fruit getById(Long id);

	// DBに追加する、作成
	Fruit save(Fruit fruit);

	// 果物更新
	Fruit update(Long id, Fruit fruit);

	// 果物削除
	void delete(Long id);

	// 果物は存在するかどうか
	boolean exists(Long id);

	// 数量
	long count();
}
