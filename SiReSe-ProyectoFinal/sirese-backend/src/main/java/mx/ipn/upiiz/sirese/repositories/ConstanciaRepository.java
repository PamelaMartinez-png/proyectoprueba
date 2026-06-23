package mx.ipn.upiiz.sirese.repositories;

import mx.ipn.upiiz.sirese.entities.Constancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstanciaRepository extends JpaRepository<Constancia, Long> {
    List<Constancia> findAllByOrderByFechaEmisionDesc();
}
