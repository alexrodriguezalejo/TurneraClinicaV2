package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.tif.turnera.entity.Permiso;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByNombre(String nombre);
}
