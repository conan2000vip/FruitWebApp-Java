/* ==============================
   COMMON : 共通処理
============================== */

function getElement(id) {
    return document.getElementById(id);
}

function clearErrors() {
    const errorIds = [
        "fruitNameError",
        "fruitRegionError",
        "fruitPriceError",
        "fruitQuantityError",
        "fruitImageError",
    ];

    errorIds.forEach((id) => {
        const el = getElement(id);
        if (el) {
            el.textContent = "";
        }
    });
}

function setPreviewImage(imageUrl) {
    const previewImg = getElement("previewImg");

    if (!previewImg) return;

    if (imageUrl) {
        // DBには images/xoai.jpg のような相対パスを保存する
        // 表示するときだけ /images/xoai.jpg にする
        previewImg.src = imageUrl.startsWith("/") ? imageUrl : "/" + imageUrl;
        previewImg.style.display = "block";
    } else {
        previewImg.removeAttribute("src");
        previewImg.style.display = "none";
    }
}

function getSelectedRegion() {
    const selectedRegion = document.querySelector('input[name="region"]:checked');
    return selectedRegion ? selectedRegion.value : "";
}

function setSelectedRegion(region) {
    document.querySelectorAll('input[name="region"]').forEach((radio) => {
        radio.checked = radio.value === region;
    });
}

/* ==============================
   SAVE : 商品登録・更新処理
============================== */

function saveFruit() {
    clearErrors();

    const id = getElement("fruitId").value;
    const currentImageUrl = getElement("currentImageUrl")
        ? getElement("currentImageUrl").value
        : "";

    const imageFile = getElement("fruitImage").files[0];

    const formData = new FormData();

    // IDがある場合だけ送る。空文字のIDを送るとBackend側でエラーになることがある
    if (id) {
        formData.append("id", id);
    }

    formData.append("name", getElement("fruitName").value);
    formData.append("region", getSelectedRegion());
    formData.append("price", getElement("fruitPrice").value);
    formData.append("quantity", getElement("fruitQuantity").value);
    formData.append("description", getElement("fruitDescription").value);

    // 重要：
    // 更新時に新しい画像を選ばない場合でも、古い画像URLを送る
    // previewImg.src ではなく、hiddenの currentImageUrl を使う
    formData.append("imageUrl", currentImageUrl);

    // 新しい画像が選択された場合だけ送信する
    if (imageFile) {
        formData.append("imageFile", imageFile);
    }

    const url = id ? "/fruits/update" : "/fruits/save";
    const actionText = id ? "更新" : "登録";

    Swal.fire({
        icon: "question",
        title: `商品を${actionText}しますか？`,
        showCancelButton: true,
        confirmButtonText: "OK",
        cancelButtonText: "キャンセル",
    }).then((result) => {
        if (!result.isConfirmed) return;

        fetch(url, {
            method: "POST",
            body: formData,
        })
            .then(async (response) => {
                if (!response.ok) {
                    const errors = await response.json();

                    console.log("ERRORS =", errors);

                    getElement("fruitNameError").textContent = errors.name || "";
                    getElement("fruitRegionError").textContent = errors.region || "";
                    getElement("fruitPriceError").textContent = errors.price || "";
                    getElement("fruitQuantityError").textContent = errors.quantity || "";
                    getElement("fruitImageError").textContent =
                        errors.imageFile || errors.imageUrl || "";

                    // validation errorの場合は下のcatchで余計なエラーアラートを出さない
                    throw { type: "validation" };
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

                if (error && error.type === "validation") {
                    return;
                }

                Swal.fire({
                    icon: "error",
                    title: "処理失敗",
                    text: "処理中にエラーが発生しました。",
                });
            });
    });
}

/* ==============================
   OPEN/CLOSE : モーダル表示・非表示処理
============================== */

function openAddForm() {
    clearErrors();

    getElement("modalTitle").textContent = "商品登録";

    getElement("fruitForm").reset();

    getElement("fruitId").value = "";

    if (getElement("currentImageUrl")) {
        getElement("currentImageUrl").value = "";
    }

    getElement("fruitImage").value = "";

    setPreviewImage("");

    getElement("fruitFormModal").style.display = "flex";
}

function openEditForm(btn) {
    clearErrors();

    getElement("modalTitle").textContent = "商品編集";

    getElement("fruitId").value = btn.dataset.id || "";
    getElement("fruitName").value = btn.dataset.name || "";
    getElement("fruitPrice").value = btn.dataset.price || "";
    getElement("fruitQuantity").value = btn.dataset.quantity || "";
    getElement("fruitDescription").value = btn.dataset.description || "";

    setSelectedRegion(btn.dataset.region || "");

    // data-image-url でも data-imageurl でも取れるようにしておく
    const imageUrl = btn.dataset.imageUrl || btn.dataset.imageurl || "";

    // 古い画像URLをhiddenに保存する
    if (getElement("currentImageUrl")) {
        getElement("currentImageUrl").value = imageUrl;
    }

    // file input は必ず空にする
    // file inputには既存画像をセットできないため
    getElement("fruitImage").value = "";

    setPreviewImage(imageUrl);

    getElement("fruitFormModal").style.display = "flex";
}

function closeFruitForm() {
    getElement("fruitFormModal").style.display = "none";
}

/* ==============================
   IMAGE PREVIEW : 画像プレビュー
============================== */

const fruitImageInput = getElement("fruitImage");

if (fruitImageInput) {
    fruitImageInput.addEventListener("change", function () {
        const file = this.files[0];
        const currentImageUrl = getElement("currentImageUrl")
            ? getElement("currentImageUrl").value
            : "";

        if (file) {
            const previewImg = getElement("previewImg");
            previewImg.src = URL.createObjectURL(file);
            previewImg.style.display = "block";
        } else {
            setPreviewImage(currentImageUrl);
        }
    });
}

/* ==============================
   MODAL OUTSIDE CLICK : 外側クリックで閉じる
============================== */

window.addEventListener("click", function (e) {
    const modal = getElement("fruitFormModal");

    if (e.target === modal) {
        closeFruitForm();
    }
});

/* ==============================
   DELETE : 商品削除処理
============================== */

function openConfirmDialog(id) {
    getElement("confirmDialog").style.display = "flex";
    getElement("confirmYes").dataset.id = id;
}

function closeConfirmDialog() {
    getElement("confirmDialog").style.display = "none";
}

const confirmYes = getElement("confirmYes");
const confirmNo = getElement("confirmNo");

if (confirmYes) {
    confirmYes.addEventListener("click", function () {
        window.location.href = "/fruits/delete/" + this.dataset.id;
    });
}

if (confirmNo) {
    confirmNo.addEventListener("click", function () {
        closeConfirmDialog();
    });
}

document.addEventListener("click", function (e) {
    if (e.target.classList.contains("delete-btn")) {
        openConfirmDialog(e.target.dataset.id);
    }
});
