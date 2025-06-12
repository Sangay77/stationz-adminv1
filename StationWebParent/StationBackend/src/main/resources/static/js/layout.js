document.addEventListener('DOMContentLoaded', () => {
  const sidebarToggle = document.getElementById('sidebarToggle');
  const sidebar = document.getElementById('sidebar');

  if (sidebarToggle && sidebar) {
    sidebarToggle.addEventListener('click', (e) => {
      e.stopPropagation();
      sidebar.classList.toggle('open');
    });

    // Close sidebar when clicking outside on small screens
    document.addEventListener('click', (event) => {
      if (window.innerWidth < 992) {
        if (!sidebar.contains(event.target) && !sidebarToggle.contains(event.target)) {
          sidebar.classList.remove('open');
        }
      }
    });
  }
});
