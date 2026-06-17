/* SAVE : 商品登録・更新処理*/

//APIリクエスト前の前処理
// hidden項目から商品IDを取得
// IDが存在する場合：更新処理
// IDが空の場合：新規登録処理
document.getElementById("fruitNameError").textContent = "";
document.getElementById("fruitRegionError").textContent = "";
document.getElementById("fruitPriceError").textContent = "";
document.getElementById("fruitQuantityError").textContent = "";
document.getElementById("fruitImageError").textContent = "";

function saveFruit() {
  const id = document.getElementById("fruitId").value;

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
        .then(async (response) => {
          if (!response.ok) {

            const errors = await response.json();

            console.log("ERRORS =", errors);

            document.getElementById("fruitNameError").textContent =
              errors.name || "";

            document.getElementById("fruitRegionError").textContent =
              errors.region || "";

            document.getElementById("fruitPriceError").textContent =
              errors.price || "";

            document.getElementById("fruitQuantityError").textContent =
              errors.quantity || "";

            document.getElementById("fruitImageError").textContent =
              errors.imageFile || errors.imageUrl || "";

            throw new Error("Validation Error");
          }

          return response.text();
        })
        .then(() => {
          Swal.fire({
            icon: "success",
            title: id ? "商品情報を更新しました" : "商品を登録しました",
            confirmButtonText: "OK",
          }).then(() => {
            closeFruitForm();
            location.reload();
          });
        })
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
