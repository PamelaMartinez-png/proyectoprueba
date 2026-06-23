package mx.ipn.upiiz.sirese.repositories;

import mx.ipn.upiiz.sirese.entities.Aspirante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AspiranteRepository extends JpaRepository<Aspirante, Long> {

    boolean existsByEmail(String email);

    Optional<Aspirante> findByEmail(String email);

    List<Aspirante> findTop5ByOrderByFechaRegistroDesc();
}
