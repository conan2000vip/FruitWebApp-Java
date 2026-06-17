/* SAVE : 商品登録・更新処理*/

//APIリクエスト前の前処理
// hidden項目から商品IDを取得
// IDが存在する場合：更新処理
// IDが空の場合：新規登録処理
function saveFruit() {
  const id = document.getElementById("fruitId").value;
  const fruitName = document.getElementById("fruitName");
  const region = document.querySelector('input[name="region"]:checked');
  const fruitPrice = document.getElementById("fruitPrice");
  const fruitQuantity = document.getElementById("fruitQuantity");
  const fruitImage = document.getElementById("fruitImage");

  const regionError = document.getElementById("fruitRegionError");
  const fruitNameError = document.getElementById("fruitNameError");
  const fruitPriceError = document.getElementById("fruitPriceError");
  const fruitQuantityError = document.getElementById("fruitQuantityError");
  const fruitImageError = document.getElementById("fruitImageError");

  // エラーメッセージとエラー状態のリセット
  fruitImage.classList.remove("input-error");
  fruitName.classList.remove("input-error");
  fruitPrice.classList.remove("input-error");
  fruitQuantity.classList.remove("input-error");
  fruitImage.classList.remove("input-error");

  fruitRegionError.textContent = "";
  fruitNameError.textContent = "";
  fruitPriceError.textContent = "";
  fruitQuantityError.textContent = "";
  fruitImageError.textContent = "";

let hasError = false;

if (!region) {
  regionError.textContent = "地域を選択してください";
  hasError = true;
} else {
  regionError.textContent = "";
}

if (!fruitName.value.trim()) {
    fruitName.classList.add("input-error");
    fruitNameError.textContent = "果物名を入力してください";
    hasError = true;
}

if (!fruitPrice.value.trim()) {
    fruitPrice.classList.add("input-error");
    fruitPriceError.textContent = "価格を入力してください";
    hasError = true;
}

if (!fruitQuantity.value.trim()) {
    fruitQuantity.classList.add("input-error");
    fruitQuantityError.textContent = "数量を入力してください";
    hasError = true;
}

if (!fruitImage.files || fruitImage.files.length === 0) {
    fruitImage.classList.add("input-error");
    fruitImageError.textContent = "画像を選択してください";
    hasError = true;
}
// エラーがある場合は処理を中断
if (hasError) return;


//   // 必須項目の入力チェック
//   const fruitName = document.getElementById("fruitName");
//   const fruitNameError = document.getElementById("fruitNameError");

//   fruitName.classList.remove("input-error");
//   fruitNameError.textContent = "";

//   if (!fruitName.value.trim()) {
//     fruitName.classList.add("input-error");
//     fruitNameError.textContent = "果物名を入力してください";
//     return;
//   }

//   // 地域の選択チェック
//   const fruitRegionError = document.getElementById("fruitRegionError");
//   regionError.textContent = "";

//   if (!selectedRegion) {
//     regionError.textContent = "地域を選択してください";
//     return;
//   } 

//   // 価格の入力チェック
//   const fruitPrice = document.getElementById("fruitPrice");
//   const fruitPriceError = document.getElementById("fruitPriceError");
//   fruitPrice.classList.remove("input-error");
//   fruitPriceError.textContent = "";

//   if (!fruitPrice.value.trim()) {
//     fruitPrice.classList.add("input-error");
//     fruitPriceError.textContent = "数字を入力してください";
//     return;
// }

// if (Number(fruitPrice.value) < 0) {
//     fruitPrice.classList.add("input-error");
//     fruitPriceError.textContent = " 価格は0以上の数値で入力してください";
//     return;
// }

//   // 数量の入力チェック
//   const fruitQuantity = document.getElementById("fruitQuantity");
//   const fruitQuantityError = document.getElementById("fruitQuantityError");
//   fruitQuantity.classList.remove("input-error");
//   fruitQuantityError.textContent = "";

//   if (!fruitQuantity.value.trim()) {
//     fruitQuantity.classList.add("input-error");
//     fruitQuantityError.textContent = "数字を入力してください";
//     return;
//   }

//   if (Number(fruitQuantity.value) < 0) {
//     fruitQuantity.classList.add("input-error");
//     fruitQuantityError.textContent = "数量は0以上の数値で入力してください";
//     return;
//   }

//   // 画像ファイルの入力チェック
//   const fruitImage = document.getElementById("fruitImage");
//   const fruitImageError = document.getElementById("fruitImageError");
//   fruitImage.classList.remove("input-error");
//   fruitImageError.textContent = "";

//   if (fruitImage.files.length > 0) {
//     const file = fruitImage.files[0];
//     const validTypes = ["image/jpeg", "image/png", "image/gif"];
//     if (!validTypes.includes(file.type)) {
//       fruitImage.classList.add("input-error");
//       fruitImageError.textContent =
//         "画像ファイルはJPEG、PNG、GIFのいずれかを選択してください";
//       return;
//     }
//   } else if (!id) {
//     // 新規登録の場合は画像ファイルの選択を必須にする
//     fruitImage.classList.add("input-error");
//     fruitImageError.textContent = "画像ファイルを選択してください";
//     return;
//   }

  // フォームデータ作成
  const formData = new FormData();

  formData.append("id", id);
  formData.append("name", document.getElementById("fruitName").value);
  const selectedRegion = document.querySelector('input[name="region"]:checked');
  formData.append("region", selectedRegion ? selectedRegion.value : "");
  formData.append("price", document.getElementById("fruitPrice").value);
  formData.append("quantity", document.getElementById("fruitQuantity").value);
  formData.append(
    "description",
    document.getElementById("fruitDescription").value,
  );
  // 画像URLは更新時のみ送信
  if (id) {
    formData.append("imageUrl", document.getElementById("previewImg").src);
  }
  // 画像ファイルが選択されている場合はフォームデータに追加
  const imageFile = document.getElementById("fruitImage").files[0];
  if (imageFile) {
    formData.append("imageFile", imageFile);
  }

  // APIエンドポイントの決定, IDが存在する場合は更新、存在しない場合は新規登録
  const url = id ? "/fruits/update" : "/fruits/save";
  // アクションテキストの決定
  const actionText = id ? "更新" : "登録";
  // 確認ダイアログの表示
  Swal.fire({
    icon: "question",
    title: `商品を${actionText}しますか？`,
    showCancelButton: true,
    confirmButtonText: "OK",
    cancelButtonText: "キャンセル",
  }).then((result) => {
    // ユーザーがOKをクリックした場合にAPIリクエストを実行
    if (result.isConfirmed) {
      fetch(url, {
        method: "POST",
        body: formData,
      })
        // レスポンスのチェック
        .then((response) => {
          if (!response.ok) {
            throw new Error("サーバーエラーが発生しました");
          }
          // レスポンスをテキストとして取得
          return response.text();
        })

        // 成功時の処理
        .then(() => {
          Swal.fire({
            icon: "success",
            title: id ? "商品情報を更新しました" : "商品を登録しました",
            confirmButtonText: "OK",
          }).then(() => {
            // フォームを閉じる
            closeFruitForm();

            // 商品一覧をリロードして最新の情報を表示
            location.reload();
          });
        })

        // エラー時の処理
        .catch((error) => {
          console.error(error);
          Swal.fire({
            icon: "error",
            title: "処理失敗",
            text: error.message,
          });
        });
    }
  });
}

/* OPEN/CLOSE : モーダル表示・非表示処理*/
function openAddForm() {
  // モーダルタイトルを「商品登録」に設定
  document.getElementById("modalTitle").textContent = "商品登録";
  // フォームをリセットして、IDを空にする
  document.getElementById("fruitForm").reset();

  document.getElementById("fruitId").value = "";
  // プレビュー画像を非表示にする
  document.getElementById("previewImg").style.display = "none";
  // モーダルを表示する
  document.getElementById("fruitFormModal").style.display = "flex";
}

// 編集ボタンがクリックされたときの処理
function openEditForm(btn) {
  // モーダルタイトルを「商品編集」に設定
  document.getElementById("modalTitle").textContent = "商品編集";
  // フォームの各項目にデータ属性から値をセットする
  document.getElementById("fruitId").value = btn.dataset.id;
  document.getElementById("fruitName").value = btn.dataset.name;
  document.getElementById("fruitPrice").value = btn.dataset.price;
  document.getElementById("fruitQuantity").value = btn.dataset.quantity;
  document.getElementById("fruitDescription").value = btn.dataset.description;
  // 地域のラジオボタンを選択する
  document.querySelectorAll('input[name="region"]').forEach((r) => {
    r.checked = r.value === btn.dataset.region;
  });
  // プレビュー画像の表示
  const img = document.getElementById("previewImg");
  // データ属性に画像URLがある場合は表示、ない場合は非表示
  if (btn.dataset.imageurl) {
    img.src = btn.dataset.imageurl;
    img.style.display = "block";
  } else {
    img.style.display = "none";
  }
  // モーダルを表示する
  document.getElementById("fruitFormModal").style.display = "flex";
}
// モーダルを閉じる処理
function closeFruitForm() {
  document.getElementById("fruitFormModal").style.display = "none";
}
// モーダルの外側をクリックしたときにモーダルを閉じる処理
window.addEventListener("click", function (e) {
  const modal = document.getElementById("fruitFormModal");
  if (e.target === modal) {
    closeFruitForm();
  }
});

/* DELETE : 商品削除処理*/
function openConfirmDialog(id) {
  document.getElementById("confirmDialog").style.display = "flex";
  document.getElementById("confirmYes").dataset.id = id;
}
// 確認ダイアログを閉じる処理
function closeConfirmDialog() {
  document.getElementById("confirmDialog").style.display = "none";
}
// 確認ダイアログの「はい」ボタンがクリックされたときの処理
document.getElementById("confirmYes").addEventListener("click", function () {
  window.location.href = "/fruits/delete/" + this.dataset.id;
});
// 確認ダイアログの「いいえ」ボタンがクリックされたときの処理
document.getElementById("confirmNo").addEventListener("click", function () {
  closeConfirmDialog();
});
// 商品削除ボタンがクリックされたときの処理
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("delete-btn")) {
    openConfirmDialog(e.target.dataset.id);
  }
});
