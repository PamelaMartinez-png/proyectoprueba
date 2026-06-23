package mx.ipn.upiiz.sirese.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administradores")
@Data
@NoArgsConstructor
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(nullable = false)
    private String contrasena;  // BCrypt hash en producción

    @Column(nullable = false, length = 200)
    private String nombreCompleto;

    @Column(nullable = false, length = 150)
    private String email;
}
