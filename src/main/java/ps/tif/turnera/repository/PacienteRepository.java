package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.tif.turnera.entity.Paciente;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByUsuarioId(Long usuarioId);
    Optional<Paciente> findByUsuarioCorreo(String correo);

}
