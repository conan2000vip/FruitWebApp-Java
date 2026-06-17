package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;
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

		// 画像ファイルが選択されていない場合のエラーメッセージを追加
		if (imageFile == null || imageFile.isEmpty()) {
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

		// 画像ファイルが選択されている場合のみ保存処理を行う
		MultipartFile file = Objects.requireNonNull(imageFile);
		String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
		Path filePath = uploadPath.resolve(fileName);
		Files.copy(
			file.getInputStream(),
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
	public String editForm(
			@PathVariable Long id,
			Model model) {
		Fruit fruit = fruitService.getById(id);
		if (fruit == null) {
			return "redirect:/fruits";
		}
		model.addAttribute("fruit", fruit);
		return "fruit/form";
	}

	@PostMapping("/update")
@ResponseBody
public ResponseEntity<?> update(
        @Valid @ModelAttribute FruitDTO fruitDTO,
        BindingResult bindingResult,
        @RequestParam(required = false) MultipartFile imageFile
) throws IOException {

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

        String fileName = Paths.get(imageFile.getOriginalFilename())
                .getFileName()
                .toString();

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(
                imageFile.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        fruit.setImageUrl("/images/" + fileName);
    }

    // 画像を選択していない場合は fruit.setImageUrl() を呼ばない
    // つまりDBにある古い画像URLをそのまま残す

    fruitService.save(fruit);

    return ResponseEntity.ok("OK");
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
