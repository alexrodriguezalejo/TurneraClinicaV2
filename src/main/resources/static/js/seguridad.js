// js/seguridad.js

async function verificarAcceso(rolEsperado) {
  const token = localStorage.getItem("token");
  if (!token) {
    window.location.href = "/html/login.html";
    return null;
  }

  try {
    const response = await fetch("/api/auth/me", {
      headers: { "Authorization": "Bearer " + token }
    });

    if (!response.ok) {
      localStorage.removeItem("token");
      window.location.href = "/html/login.html";
      return null;
    }

    const usuario = await response.json();

    if (rolEsperado && usuario.rol !== rolEsperado) {
      alert("Acceso denegado");
      window.location.href = "/";
      return null;
    }

    return usuario;
  } catch (error) {
    console.error("Error al verificar acceso:", error);
    localStorage.removeItem("token");
    window.location.href = "/html/login.html";
  }
}

function cerrarSesion() {
  localStorage.removeItem("token");
  window.location.href = "/";
}
