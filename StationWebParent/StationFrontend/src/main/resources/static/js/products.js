// Initialize rich text editor
function initRichTextEditor() {
   $("#fullDescription").richText();
   $("#shortDescription").richText();
}

// Handle image preview
function setupImagePreview() {
    $('#fileImage').change(function(event) {
        const file = event.target.files[0];
        if (file) {
            if (file.size > 2 * 1024 * 1024) {
                alert('Image size should be less than 2MB');
                this.value = '';
                return;
            }
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#thumbnail').attr('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
    });
}

// Initialize collapsible sections
function initCollapsibleSections() {
    const defaultCollapsed = ['dimensions', 'status'];
    defaultCollapsed.forEach(id => {
        const element = document.getElementById(id);
        if (element) {
            new bootstrap.Collapse(element, {toggle: false});
            const header = element.previousElementSibling;
            if (header) {
                header.classList.add('collapsed');
            }
        }
    });
}
// Document ready handler
$(document).ready(function() {
    initRichTextEditor();
    setupImagePreview();
    initCollapsibleSections();
    setupFormSubmission();

    // Additional safety check for editor content
    $(window).on('beforeunload', function() {
        $('#fullDescription').val($('#richTextEditor').html());
    });
});