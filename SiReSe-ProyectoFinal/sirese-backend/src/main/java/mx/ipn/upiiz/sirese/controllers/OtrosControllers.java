package mx.ipn.upiiz.sirese.controllers;

import mx.ipn.upiiz.sirese.entities.Constancia;
import mx.ipn.upiiz.sirese.services.AdministradorService;
import mx.ipn.upiiz.sirese.services.AspiranteService;
import mx.ipn.upiiz.sirese.services.CarreraService;
import mx.ipn.upiiz.sirese.services.ConstanciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// ============================================================
//  Auth Controller
// ============================================================
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
class AuthController {

    @Autowired private AdministradorService administradorService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String usuario    = body.get("usuario");
        String contrasena = body.get("contrasena");
        Map<String, Object> resultado = administradorService.login(usuario, contrasena);
        return ResponseEntity.ok(resultado);
    }
}

// ============================================================
//  Correo Controller
// ============================================================
@RestController
@RequestMapping("/api/correo")
@CrossOrigin(origins = "*")
class CorreoController {

    @Autowired private AspiranteService aspiranteService;

    // POST /api/correo/individual
    @PostMapping("/individual")
    public ResponseEntity<?> enviarIndividual(@RequestBody Map<String, Object> body) {
        Long   aspiranteId = Long.valueOf(body.get("aspiranteId").toString());
        String asunto      = (String) body.get("asunto");
        String mensaje     = (String) body.get("mensaje");

        boolean ok = aspiranteService.enviarCorreoIndividual(aspiranteId, asunto, mensaje);
        if (!ok) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Correo enviado."));
    }

    // POST /api/correo/masivo
    @PostMapping("/masivo")
    public ResponseEntity<?> enviarMasivo(@RequestBody Map<String, String> body) {
        String asunto  = body.get("asunto");
        String mensaje = body.get("mensaje");
        int enviados   = aspiranteService.enviarCorreoMasivo(asunto, mensaje);
        return ResponseEntity.ok(Map.of("success", true, "total", enviados,
            "mensaje", "Correo enviado a " + enviados + " aspirantes."));
    }
}

// ============================================================
//  Constancias Controller
// ============================================================
@RestController
@RequestMapping("/api/constancias")
@CrossOrigin(origins = "*")
class ConstanciasController {

    @Autowired private ConstanciaService constanciaService;

    /**
     * GET /api/constancias — Historial de constancias emitidas.
     * Devuelve un arreglo con {id, aspirante, carrera, fecha} para el frontend.
     */
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Constancia> lista = constanciaService.listarTodas();

        List<Map<String, Object>> resultado = lista.stream().map(c -> Map.<String, Object>of(
            "id",          c.getId(),
            "aspiranteId", c.getAspirante().getId(),
            "aspirante",   c.getAspirante().getNombre(),
            "carrera",     c.getAspirante().getCarrera().getNombre(),
            "fecha",       c.getFechaEmision().toString()
        )).toList();

        return ResponseEntity.ok(resultado);
    }
}

// ============================================================
//  Dashboard Controller
// ============================================================
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
class DashboardController {

    @Autowired private AspiranteService  aspiranteService;
    @Autowired private CarreraService    carreraService;
    @Autowired private ConstanciaService constanciaService;

    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(Map.of(
            "aspirantes",  aspiranteService.contarTodos(),
            "carreras",    carreraService.contar(),
            "constancias", constanciaService.contarTodas(),
            "correos",     aspiranteService.contarTodos() * 2
        ));
    }
}
