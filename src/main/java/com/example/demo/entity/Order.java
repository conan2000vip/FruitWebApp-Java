package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment Mysql

	private Long id;

	private Long userId;
	private String orderName;
	private Long orderPrice;
	private Integer orderQuantity;
	private String address;
	private String orderDate;
	private String orderStatus;
}
