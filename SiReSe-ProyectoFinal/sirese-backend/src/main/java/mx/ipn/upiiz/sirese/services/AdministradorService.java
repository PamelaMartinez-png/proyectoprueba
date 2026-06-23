package mx.ipn.upiiz.sirese.services;

import mx.ipn.upiiz.sirese.entities.Administrador;
import mx.ipn.upiiz.sirese.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AdministradorService {

    @Autowired private AdministradorRepository adminRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    /**
     * Verifica credenciales. Soporta contraseñas en texto plano (legacy)
     * y hashes BCrypt. Si la contraseña almacenada no empieza con "$2"
     * se compara en texto plano para compatibilidad con datos de ejemplo.
     */
    public Map<String, Object> login(String usuario, String contrasena) {
        Optional<Administrador> opt = adminRepository.findByUsuario(usuario);
        if (opt.isEmpty()) {
            return Map.of("success", false, "mensaje", "Usuario no encontrado.");
        }

        Administrador admin = opt.get();
        boolean ok;

        if (admin.getContrasena().startsWith("$2")) {
            // Contraseña ya hasheada con BCrypt
            ok = passwordEncoder.matches(contrasena, admin.getContrasena());
        } else {
            // Contraseña en texto plano (datos iniciales / demo)
            ok = admin.getContrasena().equals(contrasena);
        }

        if (!ok) {
            return Map.of("success", false, "mensaje", "Contraseña incorrecta.");
        }

        return Map.of(
            "success", true,
            "nombre",  admin.getNombreCompleto(),
            "mensaje", "Sesión iniciada correctamente."
        );
    }

    /**
     * Cambia la contraseña de un administrador guardándola con hash BCrypt.
     */
    public boolean cambiarContrasena(String usuario, String nuevaContrasena) {
        Optional<Administrador> opt = adminRepository.findByUsuario(usuario);
        if (opt.isEmpty()) return false;
        Administrador admin = opt.get();
        admin.setContrasena(passwordEncoder.encode(nuevaContrasena));
        adminRepository.save(admin);
        return true;
    }
}
