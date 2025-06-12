
let articleIdToDelete = null;
let targetElement = null;

const deleteModal = document.getElementById('confirmDeleteModal');
if (deleteModal) {
    deleteModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        articleIdToDelete = button.getAttribute('data-article-id');
        targetElement = button.closest('.article-block');
    });
}

$('#confirmDeleteBtn').click(function () {
    if (!articleIdToDelete) return;

    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');
    const url = contextPath + 'article/delete/' + articleIdToDelete;

    $.ajax({
        url: url,
        type: 'DELETE',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function () {
            $('#confirmDeleteModal').modal('hide');
            if (targetElement) {
                targetElement.remove();
            }
            showToastMessage('Article deleted successfully.');
        },
        error: function () {
            showToastMessage('Failed to delete article.');
        }
    });
});

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}
