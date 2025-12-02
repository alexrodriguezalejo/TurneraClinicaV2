package ps.tif.turnera.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ps.tif.turnera.entity.EstadoTurno;
import ps.tif.turnera.entity.Turno;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByMedicoId(Long medicoId);
    List<Turno> findByPacienteId(Long pacienteId);
    List<Turno> findByFecha(LocalDate fecha);
    boolean existsByMedicoIdAndFechaAndHora(Long medicoId, LocalDate fecha, LocalTime hora);
    List<Turno> findByMedicoIdAndPacienteIsNullAndEstado(Long medicoId, EstadoTurno estado);
    List<Turno> findByMedicoEspecialidadAndEstado(String especialidad, EstadoTurno estado);
    List<Turno> findByMedicoIdAndFecha(Long medicoId, LocalDate fecha);
    List<Turno> findByMedicoIdAndFechaAndEstado(Long medicoId, LocalDate fecha, EstadoTurno estado);
    List<Turno> findByMedicoEspecialidadAndFechaAndEstadoAndPacienteIsNull(
    String especialidad,
    LocalDate fecha,
    EstadoTurno estado
);

List<Turno> findByPacienteIdAndFechaAfter(Long pacienteId, LocalDate fecha);

List<Turno> findByPacienteIdAndFechaAfterAndMedicoEspecialidad(Long pacienteId, LocalDate fecha, String especialidad);

List<Turno> findByPacienteIdAndFechaBefore(Long pacienteId, LocalDate fecha);

List<Turno> findByPacienteIdAndFechaBeforeAndMedicoEspecialidad(Long pacienteId, LocalDate fecha, String especialidad);

}
