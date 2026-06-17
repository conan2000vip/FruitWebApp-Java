package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FruitDTO {

	private Long id;

	@NotBlank(message = "果物名は必須です。")
	@Size(max = 100, message = "果物名は100文字以内で入力してください。")
	@Pattern(regexp = "^[\\p{L}0-9\\s]+$", message = "果物名に特殊文字は使用できません。")
	private String name;

	@NotBlank(message = "地域を選択してください。")
	private String region;

	@NotNull(message = "価格を入力してください。")
	@DecimalMin(value = "0.01", message = "価格は0より大きい値を入力してください。")
	private Long price;

	@NotNull(message = "在庫量を入力してください。")
	@Min(value = 0, message = "在庫量は0以上を入力してください。")
	private Integer quantity;

	@Size(max = 500, message = "説明は500文字以内で入力してください。")
	private String description;

}
