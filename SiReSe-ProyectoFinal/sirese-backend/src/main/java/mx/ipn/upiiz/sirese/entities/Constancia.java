package mx.ipn.upiiz.sirese.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "constancias")
@Data
@NoArgsConstructor
public class Constancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aspirante_id", nullable = false)
    private Aspirante aspirante;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @PrePersist
    public void prePersist() {
        this.fechaEmision = LocalDate.now();
    }
}
