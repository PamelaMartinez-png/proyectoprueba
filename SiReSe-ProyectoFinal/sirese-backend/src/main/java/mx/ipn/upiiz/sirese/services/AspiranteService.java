package mx.ipn.upiiz.sirese.services;

import mx.ipn.upiiz.sirese.entities.Aspirante;
import mx.ipn.upiiz.sirese.entities.Carrera;
import mx.ipn.upiiz.sirese.repositories.AspiranteRepository;
import mx.ipn.upiiz.sirese.repositories.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AspiranteService {

    @Autowired private AspiranteRepository aspiranteRepository;
    @Autowired private CarreraRepository carreraRepository;
    @Autowired private JavaMailSender mailSender;

    @Value("${sirese.admin.email}")
    private String adminEmail;

    public List<Aspirante> listarTodos() {
        return aspiranteRepository.findAll();
    }

    public List<Aspirante> listarRecientes() {
        return aspiranteRepository.findTop5ByOrderByFechaRegistroDesc();
    }

    public Optional<Aspirante> buscarPorId(Long id) {
        return aspiranteRepository.findById(id);
    }

    public boolean existeEmail(String email) {
        return aspiranteRepository.existsByEmail(email);
    }

    /**
     * Alta de aspirante (registro público o desde panel admin).
     * Retorna null si hay error de validación; de lo contrario el aspirante guardado.
     */
    public Map<String, Object> registrar(Map<String, Object> body) {
        String email = (String) body.get("email");

        if (aspiranteRepository.existsByEmail(email)) {
            return Map.of("success", false, "mensaje", "El correo ya está registrado.");
        }

        Long carreraId = Long.valueOf(body.get("carreraId").toString());
        Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
        if (carreraOpt.isEmpty()) {
            return Map.of("success", false, "mensaje", "Carrera no encontrada.");
        }

        Aspirante a = new Aspirante();
        a.setNombre((String) body.get("nombre"));
        a.setTelefono((String) body.get("telefono"));
        a.setEmail(email);
        a.setCarrera(carreraOpt.get());
        aspiranteRepository.save(a);

        notificarAdminNuevoAspirante(a);

        return Map.of("success", true, "mensaje", "Registro exitoso.", "id", a.getId());
    }

    /**
     * Modificación de un aspirante existente.
     */
    public Map<String, Object> actualizar(Long id, Map<String, Object> body) {
        Optional<Aspirante> opt = aspiranteRepository.findById(id);
        if (opt.isEmpty()) {
            return Map.of("success", false, "mensaje", "Aspirante no encontrado.");
        }

        Aspirante a = opt.get();
        String nuevoEmail = (String) body.get("email");

        if (nuevoEmail != null && !nuevoEmail.equalsIgnoreCase(a.getEmail())
                && aspiranteRepository.existsByEmail(nuevoEmail)) {
            return Map.of("success", false, "mensaje", "El correo ya está registrado por otro aspirante.");
        }

        if (body.get("carreraId") != null) {
            Long carreraId = Long.valueOf(body.get("carreraId").toString());
            Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
            if (carreraOpt.isEmpty()) {
                return Map.of("success", false, "mensaje", "Carrera no encontrada.");
            }
            a.setCarrera(carreraOpt.get());
        }

        if (body.get("nombre") != null)   a.setNombre((String) body.get("nombre"));
        if (body.get("telefono") != null)  a.setTelefono((String) body.get("telefono"));
        if (nuevoEmail != null)            a.setEmail(nuevoEmail);

        aspiranteRepository.save(a);
        return Map.of("success", true, "mensaje", "Aspirante actualizado correctamente.");
    }

    /**
     * Baja de un aspirante.
     */
    public boolean eliminar(Long id) {
        if (!aspiranteRepository.existsById(id)) return false;
        aspiranteRepository.deleteById(id);
        return true;
    }

    // ---- Correo ----

    private void notificarAdminNuevoAspirante(Aspirante a) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(adminEmail);
            msg.setSubject("SiReSe: Nuevo aspirante registrado");
            msg.setText("Se registró un nuevo aspirante:\n\n"
                + "Nombre: "   + a.getNombre()              + "\n"
                + "Correo: "   + a.getEmail()               + "\n"
                + "Teléfono: " + a.getTelefono()            + "\n"
                + "Carrera: "  + a.getCarrera().getNombre() + "\n"
                + "Fecha: "    + a.getFechaRegistro());
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Error al enviar correo al admin: " + e.getMessage());
        }
    }

    public boolean enviarCorreoIndividual(Long aspiranteId, String asunto, String mensaje) {
        Optional<Aspirante> opt = aspiranteRepository.findById(aspiranteId);
        if (opt.isEmpty()) return false;
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(opt.get().getEmail());
            msg.setSubject(asunto);
            msg.setText(mensaje);
            mailSender.send(msg);
            return true;
        } catch (Exception e) {
            System.err.println("Error enviando correo individual: " + e.getMessage());
            return false;
        }
    }

    public int enviarCorreoMasivo(String asunto, String mensaje) {
        List<Aspirante> aspirantes = aspiranteRepository.findAll();
        int enviados = 0;
        for (Aspirante a : aspirantes) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(a.getEmail());
                msg.setSubject(asunto);
                msg.setText("Estimado(a) " + a.getNombre() + ",\n\n" + mensaje
                    + "\n\nAtentamente,\nAdministración SiReSe - UPIIZ IPN");
                mailSender.send(msg);
                enviados++;
            } catch (Exception e) {
                System.err.println("Error enviando a " + a.getEmail() + ": " + e.getMessage());
            }
        }
        return enviados;
    }

    public long contarTodos() {
        return aspiranteRepository.count();
    }
}
