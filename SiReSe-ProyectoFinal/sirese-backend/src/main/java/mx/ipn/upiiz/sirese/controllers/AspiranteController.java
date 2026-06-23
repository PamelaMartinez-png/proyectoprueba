package mx.ipn.upiiz.sirese.controllers;

import mx.ipn.upiiz.sirese.services.AspiranteService;
import mx.ipn.upiiz.sirese.services.ConstanciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aspirantes")
@CrossOrigin(origins = "*")
public class AspiranteController {

    @Autowired private AspiranteService aspiranteService;
    @Autowired private ConstanciaService constanciaService;

    // GET /api/aspirantes
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(aspiranteService.listarTodos());
    }

    // GET /api/aspirantes/recientes
    @GetMapping("/recientes")
    public ResponseEntity<?> recientes() {
        return ResponseEntity.ok(aspiranteService.listarRecientes());
    }

    // GET /api/aspirantes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return aspiranteService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/aspirantes/check-email?email=...
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(Map.of("exists", aspiranteService.existeEmail(email)));
    }

    // POST /api/aspirantes/registrar  —  Registro público y desde panel admin
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> body) {
        Map<String, Object> resultado = aspiranteService.registrar(body);
        return ResponseEntity.ok(resultado);
    }

    // PUT /api/aspirantes/{id}  —  Modificación
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Map<String, Object> body) {
        Map<String, Object> resultado = aspiranteService.actualizar(id, body);
        if (Boolean.FALSE.equals(resultado.get("success"))
                && "Aspirante no encontrado.".equals(resultado.get("mensaje"))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    // DELETE /api/aspirantes/{id}  —  Baja
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!aspiranteService.eliminar(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Aspirante eliminado correctamente."));
    }

    // GET /api/aspirantes/{id}/constancia.pdf  —  Genera PDF y registra la emisión
    @GetMapping("/{id}/constancia.pdf")
    public ResponseEntity<byte[]> generarConstancia(@PathVariable Long id) {
        byte[] pdf = constanciaService.generarYRegistrar(id);
        if (pdf == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=constancia_" + id + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}
