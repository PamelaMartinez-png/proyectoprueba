package mx.ipn.upiiz.sirese.controllers;

import mx.ipn.upiiz.sirese.entities.Administrador;
import mx.ipn.upiiz.sirese.repositories.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired private AdministradorRepository adminRepository;
    @Autowired private BCryptPasswordEncoder    passwordEncoder;
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Map<String, Object>> resultado = adminRepository.findAll().stream()
            .map(a -> Map.<String, Object>of(
                "id",            a.getId(),
                "usuario",       a.getUsuario(),
                "nombreCompleto", a.getNombreCompleto(),
                "email",         a.getEmail()
            )).toList();
        return ResponseEntity.ok(resultado);
    }

    // GET /api/administradores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return adminRepository.findById(id)
            .map(a -> ResponseEntity.ok(Map.<String, Object>of(
                "id",             a.getId(),
                "usuario",        a.getUsuario(),
                "nombreCompleto", a.getNombreCompleto(),
                "email",          a.getEmail()
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/administradores — alta de nuevo administrador
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, String> body) {
        String usuario = body.get("usuario");
        String email   = body.get("email");

        if (usuario == null || usuario.isBlank())
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "mensaje", "El usuario es obligatorio."));

        if (adminRepository.findByUsuario(usuario).isPresent())
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "mensaje", "El usuario ya existe."));

        Administrador a = new Administrador();
        a.setUsuario(usuario);
        a.setNombreCompleto(body.getOrDefault("nombreCompleto", usuario));
        a.setEmail(email != null ? email : "");
        a.setContrasena(passwordEncoder.encode(
            body.getOrDefault("contrasena", "admin123")));

        adminRepository.save(a);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "mensaje", "Administrador creado correctamente.",
            "id",      a.getId()
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Map<String, String> body) {
        Optional<Administrador> opt = adminRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Administrador a = opt.get();

        // Si el nuevo usuario ya lo tiene otro admin → error
        String nuevoUsuario = body.get("usuario");
        if (nuevoUsuario != null && !nuevoUsuario.equalsIgnoreCase(a.getUsuario())) {
            if (adminRepository.findByUsuario(nuevoUsuario).isPresent())
                return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "mensaje", "Ese usuario ya está en uso."));
            a.setUsuario(nuevoUsuario);
        }

        if (body.get("nombreCompleto") != null) a.setNombreCompleto(body.get("nombreCompleto"));
        if (body.get("email")          != null) a.setEmail(body.get("email"));


        if (body.get("contrasena") != null && !body.get("contrasena").isBlank())
            a.setContrasena(passwordEncoder.encode(body.get("contrasena")));

        adminRepository.save(a);
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Administrador actualizado."));
    }

    // DELETE /api/administradores/{id} — baja
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!adminRepository.existsById(id)) return ResponseEntity.notFound().build();
        // Evitar borrar al último admin
        if (adminRepository.count() <= 1)
            return ResponseEntity.badRequest()
                .body(Map.of("success", false,
                    "mensaje", "No puedes eliminar el único administrador del sistema."));
        adminRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Administrador eliminado."));
    }
}
