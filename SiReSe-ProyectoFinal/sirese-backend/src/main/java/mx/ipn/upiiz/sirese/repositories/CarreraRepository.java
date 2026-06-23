package mx.ipn.upiiz.sirese.repositories;

import mx.ipn.upiiz.sirese.entities.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {
}
