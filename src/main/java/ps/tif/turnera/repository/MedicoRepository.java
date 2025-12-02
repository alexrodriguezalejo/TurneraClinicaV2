package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.tif.turnera.entity.Medico;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByUsuarioId(Long usuarioId);
    Optional<Medico> findByUsuarioCorreo(String correo);

}
