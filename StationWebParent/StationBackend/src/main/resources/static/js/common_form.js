
    $(document).ready(function () {
        $("#fileImage").change(function () {
            if (!checkFileSize(this)) {
                return;
            } else {
                showImageThumbnail(this);
            }
        });
    });

    // Check file size
    function checkFileSize(fileInput) {
        const fileSize = fileInput.files[0].size;

        // If file is too large, show error
        if (fileSize > MAX_FILE_SIZE) {
            fileInput.setCustomValidity(`File must be less than ${(MAX_FILE_SIZE / 1024)} KB.`);
            fileInput.reportValidity();
            return false;
        } else {
            fileInput.setCustomValidity(""); // Clear previous error messages
            return true;
        }
    }

    // Show image thumbnail
    function showImageThumbnail(fileInput) {
        const file = fileInput.files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            // Update the thumbnail image src attribute
            $("#thumbnail").attr("src", e.target.result);
        };

        // Read the file and display it
        reader.readAsDataURL(file);
    }
