function showImageThumbnail(fileInput) {
    const MAX_FILE_SIZE = 1048576; // 1 MB

    const file = fileInput.files[0];
    if (file.size > MAX_FILE_SIZE) {
        fileInput.value = "";
        showModalDialog("File too large", "Maximum file size is 1MB.");
        return;
    }

    const reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}
