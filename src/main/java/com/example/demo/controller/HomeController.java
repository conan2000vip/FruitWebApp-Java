package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.FruitService;
import com.example.demo.service.OrderService;

@Controller
public class HomeController {

	@Autowired
	private FruitService fruitService;

	@Autowired
	private OrderService orderService;

	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("page", "home");
		model.addAttribute("totalProducts", fruitService.countProducts());
		model.addAttribute("totalStock", fruitService.totalStock());
		model.addAttribute("lowStockCount", fruitService.countLowStock());
		model.addAttribute("todayOrders", orderService.countTodayOrders());
		model.addAttribute("lowStockFruits", fruitService.getLowStockFruits());
		model.addAttribute("newOrders", orderService.getNewOrders());

		return "fruit/home";
	}
}