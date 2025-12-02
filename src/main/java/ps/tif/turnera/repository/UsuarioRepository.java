package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.tif.turnera.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
