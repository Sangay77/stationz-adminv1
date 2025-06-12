
let detailCount = 1;

function addNextDetailSection() {
  detailCount++;
  
  const htmlDetailSection = `
    <div class="card shadow-sm p-4 mb-3 detail-section">
      <div class="row g-3 align-items-center">
        <div class="col-md-5">
          <label class="form-label fw-semibold">Name</label>
          <input type="text" class="form-control detail-name" name="detailNames" maxlength="255" placeholder="Enter name" required />
        </div>

        <div class="col-md-5">
          <label class="form-label fw-semibold">Value</label>
          <input type="text" class="form-control detail-value" name="detailValues" maxlength="255" placeholder="Enter value" required />
        </div>
        
        <div class="col-md-2 d-flex align-items-end">
          <button type="button" class="btn btn-outline-danger remove-btn">
            <i class="fas fa-times"></i> Remove
          </button>
        </div>
      </div>
    </div>
  `;

  $("#divProductDetails .detail-section:last").after(htmlDetailSection);
  updateRemoveButtons();
}

function updateRemoveButtons() {
  const sections = $(".detail-section");
  
  // Hide remove button on the first section
  sections.first().find(".remove-btn").hide();
  
  // Show remove buttons on all other sections
  sections.not(":first").find(".remove-btn").show();
  
  // Set click handlers for remove buttons
  $(".remove-btn").off("click").on("click", function() {
    $(this).closest(".detail-section").remove();
    updateRemoveButtons();
  });
}
// Initialize on page load
$(document).ready(function() {
  updateRemoveButtons();
});
