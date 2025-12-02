package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.tif.turnera.entity.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
