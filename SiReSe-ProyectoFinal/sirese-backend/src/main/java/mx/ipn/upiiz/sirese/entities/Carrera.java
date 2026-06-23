package mx.ipn.upiiz.sirese.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carreras")
@Data
@NoArgsConstructor
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Integer semestres;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
