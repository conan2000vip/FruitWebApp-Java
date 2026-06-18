package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Fruit {

	// primary key 自動生成
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment Mysql

	private Long id;
	private String name;
	private String region;
	private Long price;
	private Integer quantity;
	private String description;
	private String imageUrl;

}
