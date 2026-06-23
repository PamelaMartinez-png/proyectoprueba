package mx.ipn.upiiz.sirese.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "aspirantes")
@Data
@NoArgsConstructor
public class Aspirante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String telefono;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDate.now();
    }
}
