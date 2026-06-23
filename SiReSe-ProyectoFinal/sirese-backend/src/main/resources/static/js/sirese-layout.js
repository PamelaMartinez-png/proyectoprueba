// sirese-layout.js
// Injects sidebar, navbar and session guard into every admin page

(function() {
  // Session guard
  if (!sessionStorage.getItem('adminLogged')) {
    window.location.href = 'login.html';
    return;
  }

  const adminNombre = sessionStorage.getItem('adminNombre') || 'Administrador';
  const currentPage = window.location.pathname.split('/').pop();

  function isActive(page) {
    return currentPage === page ? 'active' : '';
  }

  const sidebarHTML = `
  <div class="sidebar-backdrop" data-sidebar-close></div>
  <aside class="admin-sidebar" id="adminSidebar" aria-label="Navegación principal">
    <div class="sidebar-header">
      <a class="brand-mark" href="index.html" aria-label="SiReSe dashboard">
        <span class="brand-icon"><i class="bi bi-mortarboard-fill" aria-hidden="true"></i></span>
        <span class="brand-copy">
          <span class="brand-title">SiReSe</span>
          <span class="brand-subtitle">Panel Administrativo</span>
        </span>
      </a>
    </div>
    <nav class="sidebar-nav">
      <a class="nav-link ${isActive('index.html')}" href="index.html">
        <span class="nav-icon"><i class="bi bi-speedometer2"></i></span>
        <span class="nav-text">Inicio</span>
      </a>
      <a class="nav-link ${isActive('aspirantes.html')}" href="aspirantes.html">
        <span class="nav-icon"><i class="bi bi-people"></i></span>
        <span class="nav-text">Aspirantes</span>
      </a>
      <a class="nav-link ${isActive('carreras.html')}" href="carreras.html">
        <span class="nav-icon"><i class="bi bi-book"></i></span>
        <span class="nav-text">Carreras</span>
      </a>
      <a class="nav-link ${isActive('constancias.html')}" href="constancias.html">
        <span class="nav-icon"><i class="bi bi-file-earmark-text"></i></span>
        <span class="nav-text">Constancias</span>
      </a>
      <a class="nav-link ${isActive('administradores.html')}" href="administradores.html">
        <span class="nav-icon"><i class="bi bi-shield-lock"></i></span>
        <span class="nav-text">Administradores</span>
      </a>
    </nav>
    <div class="sidebar-user">
      <i class="bi bi-person-circle fs-3 me-2" style="color:var(--bs-primary)"></i>
      <div>
        <strong>${adminNombre}</strong>
        <small>Administrador</small>
      </div>
    </div>
    <div class="sidebar-footer">
      <a href="#" class="text-danger text-decoration-none small" onclick="cerrarSesion()">
        <i class="bi bi-box-arrow-left me-1"></i> Salir
      </a>
    </div>
  </aside>`;

  const navbarHTML = `
  <nav class="navbar admin-navbar navbar-expand bg-white">
    <div class="container-fluid px-3 px-lg-4">
      <button class="sidebar-toggle" type="button" data-sidebar-toggle aria-controls="adminSidebar" aria-expanded="true" aria-label="Toggle sidebar">
        <span></span><span></span><span></span>
      </button>
      <div class="ms-auto d-flex align-items-center gap-2">
        <button class="icon-button theme-toggle" type="button" data-theme-toggle aria-label="Cambiar tema" title="Cambiar tema">
          <i class="bi bi-moon-stars" data-theme-icon aria-hidden="true"></i>
        </button>
        <div class="dropdown">
          <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
            <i class="bi bi-person-circle me-1"></i> ${adminNombre}
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><h6 class="dropdown-header">Mi cuenta</h6></li>
            <li><a class="dropdown-item" href="#" onclick="mostrarNombreCompleto()"><i class="bi bi-info-circle me-2"></i>Acerca de</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item text-danger" href="#" onclick="cerrarSesion()"><i class="bi bi-box-arrow-left me-2"></i>Salir</a></li>
          </ul>
        </div>
      </div>
    </div>
  </nav>`;

  // Insert sidebar and navbar
  const shell = document.querySelector('.admin-shell');
  if (shell) {
    shell.insertAdjacentHTML('afterbegin', sidebarHTML);
    const main = shell.querySelector('.admin-main');
    if (main) main.insertAdjacentHTML('afterbegin', navbarHTML);
  }
})();

function cerrarSesion() {
  if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
    sessionStorage.clear();
    window.location.href = 'login.html';
  }
}

function mostrarNombreCompleto() {
  const nombre = sessionStorage.getItem('adminNombre') || 'Administrador';
  alert(' Usuario actual:\n\n' + nombre);
}
