package mx.ipn.upiiz.sirese.services;

import mx.ipn.upiiz.sirese.entities.Carrera;
import mx.ipn.upiiz.sirese.repositories.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CarreraService {

    @Autowired private CarreraRepository carreraRepository;

    public List<Carrera> listarTodas() {
        return carreraRepository.findAll();
    }

    public Optional<Carrera> buscarPorId(Long id) {
        return carreraRepository.findById(id);
    }

    public Carrera crear(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public Map<String, Object> actualizar(Long id, Carrera datos) {
        Optional<Carrera> opt = carreraRepository.findById(id);
        if (opt.isEmpty()) {
            return Map.of("success", false, "mensaje", "Carrera no encontrada.");
        }
        Carrera c = opt.get();
        c.setNombre(datos.getNombre());
        c.setSemestres(datos.getSemestres());
        c.setObservaciones(datos.getObservaciones());
        carreraRepository.save(c);
        return Map.of("success", true, "mensaje", "Carrera actualizada correctamente.");
    }

    public boolean eliminar(Long id) {
        if (!carreraRepository.existsById(id)) return false;
        carreraRepository.deleteById(id);
        return true;
    }

    public long contar() {
        return carreraRepository.count();
    }
}
