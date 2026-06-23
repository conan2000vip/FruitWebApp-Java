package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

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

@RequestMapping("/fruits")
@Controller
public class FruitController {

	@Autowired
	private FruitService fruitService;

	// 画像保存先ディレクトリ
	private final String uploadDir = "src/main/resources/static/images";
	private final Path uploadPath = Paths.get(uploadDir);

	// 検索条件
	@GetMapping
	public String list(
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String region,
			Model model) {
		model.addAttribute("page", "fruits");
		model.addAttribute("fruits", fruitService.search(keyword, region));
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedRegion", region);
		return "fruit/list";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("fruit", new Fruit());
		return "fruit/form";
	}

	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<?> save(
			@Valid @ModelAttribute FruitDTO fruitDTO,
			BindingResult bindingResult,
			@RequestParam(required = false) MultipartFile imageFile,
			@RequestParam(required = false, name = "imageUrl") String imageUrl,
			@RequestParam(required = false, name = "currentImageUrl") String currentImageUrl)
			throws IOException {
		Map<String, String> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(
						error.getField(),
						error.getDefaultMessage());
			});
		}

		String finalImageUrl = imageUrl != null && !imageUrl.isBlank() ? imageUrl : currentImageUrl;

		// 画像ファイルが選択されていない場合は、currentImageUrl / imageUrlを使用できるようにする
		if ((imageFile == null || imageFile.isEmpty()) && (finalImageUrl == null || finalImageUrl.isBlank())) {
			errors.put("imageFile", "画像を選択してください。");
		}

		// バリデーションエラーがある場合は、エラーメッセージを返す
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		// 画像保存
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		if (imageFile != null && !imageFile.isEmpty()) {
			String fileName = System.currentTimeMillis() + "_"
					+ Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(
					imageFile.getInputStream(),
					filePath,
					StandardCopyOption.REPLACE_EXISTING);
			finalImageUrl = "/images/" + fileName;
		}

		// 重複チェック: 同じ名前の商品がすでに存在している場合は保存をブロック
		// Check duplicate within the same region only (allow same name in different regions)
		if (fruitService.existsByNameIgnoreCaseAndRegion(fruitDTO.getName(), fruitDTO.getRegion())) {
			errors.put("duplicate", "この果物名は既に登録されています。");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}

		// DB登録: DTO -> Entity に変換して保存
		Fruit fruit = new Fruit();
		BeanUtils.copyProperties(fruitDTO, fruit);
		fruit.setImageUrl(finalImageUrl);
		fruitService.save(fruit);

		// 一覧画面へ遷移
		return ResponseEntity.status(HttpStatus.FOUND)
				.header(HttpHeaders.LOCATION, "/fruits")
				.build();
	}

	@PostMapping("/update")
	@ResponseBody
	public ResponseEntity<?> update(
			@Valid @ModelAttribute FruitDTO fruitDTO,
			BindingResult bindingResult,
			@RequestParam(required = false) MultipartFile imageFile) throws IOException {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			return ResponseEntity.badRequest().body(errors);
		}

		// 更新の場合は、必ずDBから既存データを取得する
		Fruit fruit = fruitService.getById(fruitDTO.getId());
		if (fruit == null) {
			Map<String, String> errors = new HashMap<>();
			errors.put("id", "商品が見つかりません。");
			return ResponseEntity.badRequest().body(errors);
		}

		// Duplicate check: consider region too so same name in different region is allowed
		if (fruitService.existsByNameIgnoreCaseAndRegionAndIdNot(fruitDTO.getName(), fruitDTO.getRegion(), fruitDTO.getId())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("duplicate", "この果物名は同じ地域ですでに登録されています。");
			return ResponseEntity.badRequest().body(errors);
		}
		// 画像以外の情報だけ更新する
		fruit.setName(fruitDTO.getName());
		fruit.setRegion(fruitDTO.getRegion());
		fruit.setPrice(fruitDTO.getPrice());
		fruit.setQuantity(fruitDTO.getQuantity());
		fruit.setDescription(fruitDTO.getDescription());

		// 新しい画像が選択された場合だけ画像を更新する
		if (imageFile != null && !imageFile.isEmpty()) {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			String fileName = System.currentTimeMillis() + "_"
					+ Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(
					imageFile.getInputStream(),
					filePath,
					StandardCopyOption.REPLACE_EXISTING);
			fruit.setImageUrl("/images/" + fileName);
		}

		// 画像を選択していない場合は fruit.setImageUrl() を呼ばない
		// つまりDBにある古い画像URLをそのまま残す
		fruitService.save(fruit);
		return ResponseEntity.ok("OK");
	}

	@GetMapping("/delete/{id}")
	public String delete(
			@PathVariable Long id) {
		fruitService.delete(id);
		return "redirect:/fruits";
	}
}
