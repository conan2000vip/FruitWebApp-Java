package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.FruitDTO;
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
	private final Path uploadPath = Paths.get(uploadDir);

	/**
	 * 商品一覧画面表示
	 *
	 * 処理概要： ① キーワードによる商品検索 ② 一覧画面へ表示
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
	 * 処理概要： ① 空のFruitオブジェクトを作成 ② 登録画面へ表示
	 */
	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("fruit", new Fruit());
		return "fruit/form";
	}

	/**
	 * 商品登録処理
	 *
	 * 処理概要： ① 入力情報取得 ② 画像アップロード(任意) ③ DB登録 ④ 一覧画面へリダイレクト
	 */
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<?> save(
			@Valid @ModelAttribute FruitDTO fruitDTO,
			BindingResult bindingResult,
			@RequestParam(required = false) MultipartFile imageFile)
			throws IOException {

		Map<String, String> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(
						error.getField(),
						error.getDefaultMessage());
			});
		}

		// kiểm tra ảnh
		if (imageFile == null || imageFile.isEmpty()) {
			errors.put("imageFile", "画像を選択してください。");
		}

		// nếu có bất kỳ lỗi nào
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		// 画像保存
		String fileName = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(
				imageFile.getInputStream(),
				filePath,
				StandardCopyOption.REPLACE_EXISTING);
		String imageUrl = "/images/" + fileName;

		// DB登録: DTO -> Entity に変換して保存
		Fruit fruit = new Fruit();
		BeanUtils.copyProperties(fruitDTO, fruit);
		fruit.setImageUrl(imageUrl);
		fruitService.save(fruit);

		// 一覧画面へ遷移
		return ResponseEntity.status(HttpStatus.FOUND)
				.header(HttpHeaders.LOCATION, "/fruits")
				.build();
	}

	/**
	 * 商品編集画面表示
	 *
	 * 処理概要： ① IDから商品取得 ② 編集画面へ表示
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

	@PostMapping("/update")
	public String update(
			@Valid @ModelAttribute FruitDTO fruitDTO,
			BindingResult bindingResult,
			@RequestParam(required = false) MultipartFile imageFile,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			model.addAttribute("fruit", fruitDTO);
			return "fruit-form";
		}

		String imageUrl = null;
		if (imageFile != null && !imageFile.isEmpty()) {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String fileName = Paths.get(imageFile.getOriginalFilename())
					.getFileName().toString();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(imageFile.getInputStream(), filePath,
					StandardCopyOption.REPLACE_EXISTING);
			imageUrl = "/images/" + fileName;
		}

		Fruit fruit = new Fruit();
		BeanUtils.copyProperties(fruitDTO, fruit);
		if (imageUrl != null) {
			fruit.setImageUrl(imageUrl);
		}
		fruitService.save(fruit);
		return "redirect:/fruits";
	}

	/**
	 * 商品削除処理
	 *
	 * 処理概要： ① 商品ID取得 ② DB削除 ③ 一覧画面へリダイレクト
	 */
	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Long id) {
		fruitService.delete(id);
		return "redirect:/fruits";
	}
}
