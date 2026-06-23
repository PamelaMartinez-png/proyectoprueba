package mx.ipn.upiiz.sirese.repositories;

import mx.ipn.upiiz.sirese.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByUsuario(String usuario);
}
