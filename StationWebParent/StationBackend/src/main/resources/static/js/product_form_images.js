let extraImageCount = 0;

$(document).ready(function() {
    // Main image upload
    $('#fileImage').change(function() {
        handleImageUpload(this, '#thumbnail');
    });

    // Add first extra image
    addExtraImageSection();

    // Event delegation for remove buttons
    $(document).on('click', '[data-section-id]', function() {
        const sectionId = $(this).data('section-id');

        if ($('#productImagesContainer .col').length <= 2) {
            alert('You must keep at least one extra image section.');
            return;
        }

        $(`#${sectionId}`).remove();
    });
});

function handleImageUpload(input, thumbnailSelector) {
    if (!input.files || !input.files[0]) return;

    const reader = new FileReader();
    reader.onload = function(e) {
        $(thumbnailSelector).attr('src', e.target.result);
    };
    reader.readAsDataURL(input.files[0]);
}

function addExtraImageSection() {
    extraImageCount++;
    const newSectionId = `extraImageSection${extraImageCount}`;
    const thumbnailId = `extraThumbnail${extraImageCount}`;
    const inputId = `extraImage${extraImageCount}`;

    const newSection = $(`
    <div class="col border m-3 p-2" id="${newSectionId}">
        <div><label>Extra Image #${extraImageCount}:</label></div>
        <div class="m-2">
            <img id="${thumbnailId}" alt="Extra image preview" class="img-fluid"
                 src="/images/image_thumbnail.png" />
        </div>
        <div>
            <input type="file" id="${inputId}" name="extraImages" accept="image/png,image/jpeg" />
            <button type="button" class="btn btn-sm btn-danger mt-2" data-section-id="${newSectionId}">
                Remove
            </button>
        </div>
    </div>`);

    $('#productImagesContainer').append(newSection);

    // Set up change handler for the new input
    $(`#${inputId}`).change(function() {
        handleImageUpload(this, `#${thumbnailId}`);
    });
}