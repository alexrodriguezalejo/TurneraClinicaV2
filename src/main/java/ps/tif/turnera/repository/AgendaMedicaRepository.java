package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ps.tif.turnera.entity.AgendaMedica;

import java.util.Optional;

public interface AgendaMedicaRepository extends JpaRepository<AgendaMedica, Long> {
    Optional<AgendaMedica> findByMedicoId(Long medicoId);
}
