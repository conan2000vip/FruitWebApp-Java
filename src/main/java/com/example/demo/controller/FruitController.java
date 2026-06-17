package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Fruit;
import com.example.demo.service.FruitService;

import jakarta.validation.Valid;

@RequestMapping("/fruits")
@Controller
public class FruitController {

	@Autowired
	private FruitService fruitService;

	// 画像保存先ディレクトリ
	private final String uploadDir = "src/main/resources/static/images";

	/**
	 * 商品一覧画面表示
	 *
	 * 処理概要：
	 * ① キーワードによる商品検索
	 * ② 一覧画面へ表示
	 */
	@GetMapping
	public String list(
			@RequestParam(required = false) String keyword,
			Model model) {
		model.addAttribute(
				"fruits",
				fruitService.searchByName(keyword));
		return "fruit/list";
	}

	/**
	 * 商品登録画面表示
	 *
	 * 処理概要：
	 * ① 空のFruitオブジェクトを作成
	 * ② 登録画面へ表示
	 */
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("fruit", new Fruit());
		return "fruit/form";
	}

	/**
	 * 商品登録処理
	 *
	 * 処理概要：
	 * ① 入力情報取得
	 * ② 画像アップロード(任意)
	 * ③ DB登録
	 * ④ 一覧画面へリダイレクト
	 */
	@PostMapping("/save")
	public String save(
			@Valid @ModelAttribute Fruit fruit,
			BindingResult bindingResult,
			@RequestParam(required = false) MultipartFile imageFile,
			Model model) throws IOException {

		// バリデーションエラー
		if (bindingResult.hasErrors()) {
			return "fruit/form";
		}

		// 画像アップロード処理
		if (imageFile != null && !imageFile.isEmpty()) {
			String fileName = System.currentTimeMillis()
					+ "_"
					+ imageFile.getOriginalFilename();
			Path uploadPath = Paths.get(uploadDir);

			// 保存先フォルダが存在しない場合は作成
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			// 画像保存
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(
					imageFile.getInputStream(),
					filePath,
					StandardCopyOption.REPLACE_EXISTING);
			fruit.setImageUrl(
					"/images/" + fileName);
		}

		// DB登録
		fruitService.save(fruit);

		// 一覧画面へ遷移
		return "redirect:/fruits";
	}

	/**
	 * 商品編集画面表示
	 *
	 * 処理概要：
	 * ① IDから商品取得
	 * ② 編集画面へ表示
	 */
	@GetMapping("/edit/{id}")
	public String edit(
			@PathVariable Long id,
			Model model) {
		Fruit fruit = fruitService.getById(id);
		model.addAttribute(
				"fruit",
				fruit);
		return "fruit-form";
	}

	/**
	 * 商品更新処理
	 *
	 * 処理概要：
	 * ① 入力情報取得
	 * ② 画像アップロード(任意)
	 * ③ DB更新
	 * ④ 一覧画面へリダイレクト
	 */
	@PostMapping("/update")
	public String update(
			@ModelAttribute Fruit fruit,
			@RequestParam(required = false) MultipartFile imageFile)
			throws IOException {

		// DBから既存データ取得
		Fruit oldFruit = fruitService.getById(fruit.getId());

		// 新しい画像が選択された場合
		if (imageFile != null && !imageFile.isEmpty()) {
			String fileName = System.currentTimeMillis()
					+ "_"
					+ imageFile.getOriginalFilename();
			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(
					imageFile.getInputStream(),
					filePath,
					StandardCopyOption.REPLACE_EXISTING);
			fruit.setImageUrl("/images/" + fileName);
		} else {
			// 画像未変更の場合は既存画像を保持
			fruit.setImageUrl(
					oldFruit.getImageUrl());
		}
		fruitService.update(
				fruit.getId(),
				fruit);
		return "redirect:/fruits";
	}

	/**
	 * 商品削除処理
	 *
	 * 処理概要：
	 * ① 商品ID取得
	 * ② DB削除
	 * ③ 一覧画面へリダイレクト
	 */
	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Long id) {
		fruitService.delete(id);
		return "redirect:/fruits";
	}
}