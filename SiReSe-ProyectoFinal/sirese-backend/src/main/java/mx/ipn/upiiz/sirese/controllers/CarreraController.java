package mx.ipn.upiiz.sirese.controllers;

import mx.ipn.upiiz.sirese.entities.Carrera;
import mx.ipn.upiiz.sirese.services.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carreras")
@CrossOrigin(origins = "*")
public class CarreraController {

    @Autowired private CarreraService carreraService;

    // GET /api/carreras
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(carreraService.listarTodas());
    }

    // GET /api/carreras/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        return carreraService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/carreras  —  Alta
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Carrera carrera) {
        Carrera nueva = carreraService.crear(carrera);
        return ResponseEntity.ok(Map.of("success", true, "id", nueva.getId(),
            "mensaje", "Carrera creada correctamente."));
    }

    // PUT /api/carreras/{id}  —  Modificación
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody Carrera datos) {
        Map<String, Object> resultado = carreraService.actualizar(id, datos);
        if (Boolean.FALSE.equals(resultado.get("success"))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    // DELETE /api/carreras/{id}  —  Baja
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!carreraService.eliminar(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Carrera eliminada correctamente."));
    }
}
