package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import ps.tif.turnera.dto.GenerarTurnosDTO;
import ps.tif.turnera.dto.TurnoDTO;
import ps.tif.turnera.dto.TurnoDetalleDTO;
import ps.tif.turnera.entity.AgendaMedica;
import ps.tif.turnera.entity.EstadoTurno;
import ps.tif.turnera.entity.Medico;
import ps.tif.turnera.entity.Paciente;
import ps.tif.turnera.entity.Turno;
import ps.tif.turnera.repository.AgendaMedicaRepository;
import ps.tif.turnera.repository.MedicoRepository;
import ps.tif.turnera.repository.PacienteRepository;
import ps.tif.turnera.repository.TurnoRepository;
import ps.tif.turnera.validation.ValidadorTurno;
import ps.tif.turnera.repository.AgendaMedicaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final ValidadorTurno validadorTurno;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final AgendaMedicaRepository agendaRepository;
    public List<Turno> listarTodos() {
        return turnoRepository.findAll();
    }

    public Optional<Turno> obtenerPorId(Long id) {
        return turnoRepository.findById(id);
    }

    public Turno crear(Turno turno) {
        // ✅ Validar antes de guardar
        validadorTurno.validarTurno(turno);
        return turnoRepository.save(turno);
    }

    public void eliminar(Long id) {
        turnoRepository.deleteById(id);
    }

    public List<Turno> buscarPorPaciente(Long pacienteId) {
        return turnoRepository.findByPacienteId(pacienteId);
    }

    public List<Turno> buscarPorMedico(Long medicoId) {
        return turnoRepository.findByMedicoId(medicoId);
    }
    public Turno crearDesdeDTO(TurnoDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Turno turno = Turno.builder()
            .fecha(dto.getFecha())
            .hora(dto.getHora())
            .medico(medico)
            .paciente(paciente)
            .estado(EstadoTurno.AGENDADO)
            .build();

        validadorTurno.validarTurno(turno);
        validadorTurno.validarTurnoDuplicado(turno);
        return turnoRepository.save(turno);
    }
    public List<Turno> generarTurnosDelDia(Medico medico, LocalDate fecha) {
        AgendaMedica agenda = agendaRepository.findByMedicoId(medico.getId())
            .orElseThrow(() -> new RuntimeException("Agenda no encontrada para el médico"));

        List<Turno> turnos = new ArrayList<>();
        int duracion = agenda.getDuracionTurno();

        // Generar turnos para la mañana
        LocalTime hora = agenda.getMañanaInicio();
        while (!hora.isAfter(agenda.getMañanaFin().minusMinutes(duracion))) {
            turnos.add(crearTurno(fecha, hora, medico));
            hora = hora.plusMinutes(duracion);
        }

        // Generar turnos para la tarde
        hora = agenda.getTardeInicio();
        while (!hora.isAfter(agenda.getTardeFin().minusMinutes(duracion))) {
            turnos.add(crearTurno(fecha, hora, medico));
            hora = hora.plusMinutes(duracion);
        }

        return turnoRepository.saveAll(turnos);
    }

    private Turno crearTurno(LocalDate fecha, LocalTime hora, Medico medico) {
        return Turno.builder()
            .fecha(fecha)
            .hora(hora)
            .estado(EstadoTurno.AGENDADO)
            .medico(medico)
            .paciente(null) // porque aún no fue reservado
            .build();
    }

    public List<Turno> generarTurnosParaDia(GenerarTurnosDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        return generarTurnosDelDia(medico, dto.getFecha());
    }

    public Turno cancelarTurno(Long id) {
    Turno turno = turnoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

    if (turno.getEstado() == EstadoTurno.CANCELADO) {
        throw new RuntimeException("El turno ya fue cancelado anteriormente");
    }

    if (turno.getEstado() == EstadoTurno.REALIZADO) {
        throw new RuntimeException("No se puede cancelar un turno ya realizado");
    }

    // Validación de tiempo: solo se permite cancelar si faltan más de 24 horas
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime fechaHoraTurno = LocalDateTime.of(turno.getFecha(), turno.getHora());

    if (ahora.plusHours(24).isAfter(fechaHoraTurno)) {
        throw new RuntimeException("Solo se puede cancelar turnos con al menos 24 horas de anticipación.");
    }

    turno.setEstado(EstadoTurno.CANCELADO);
    return turnoRepository.save(turno);
}


    public List<Turno> buscarDisponiblesPorMedico(Long medicoId) {
        return turnoRepository.findByMedicoIdAndPacienteIsNullAndEstado(medicoId, EstadoTurno.AGENDADO);
    }
    
    public List<TurnoDetalleDTO> buscarDisponiblesPorEspecialidad(String especialidad) {
        List<Turno> turnos = turnoRepository.findByMedicoEspecialidadAndEstado(especialidad, EstadoTurno.AGENDADO);

        return turnos.stream().map(t -> {
            TurnoDetalleDTO dto = new TurnoDetalleDTO();
            dto.setId(t.getId());
            dto.setFechaHora(t.getFecha() + "T" + t.getHora());
            dto.setEstado(t.getEstado().name());
            dto.setMedicoNombre(t.getMedico().getUsuario().getNombre());
            dto.setMedicoEspecialidad(t.getMedico().getEspecialidad());
            return dto;
        }).toList();
    }

    
    public Turno reservarTurno(Long turnoId, Long pacienteId) {
        Turno turno = turnoRepository.findById(turnoId)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        if (turno.getPaciente() != null) {
            throw new RuntimeException("El turno ya fue reservado");
        }

        if (turno.getEstado() != EstadoTurno.AGENDADO) {
            throw new RuntimeException("El turno no está disponible para reservar");
        }

        Paciente paciente = pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        turno.setPaciente(paciente);
        return turnoRepository.save(turno);
    }
    public List<Turno> buscarPorMedicoYFecha(Long medicoId, LocalDate fecha) {
        return turnoRepository.findByMedicoIdAndFecha(medicoId, fecha);
    }

    public List<Turno> buscarPorMedicoFechaYEstado(Long medicoId, LocalDate fecha, EstadoTurno estado) {
        return turnoRepository.findByMedicoIdAndFechaAndEstado(medicoId, fecha, estado);
    }
    // Turnos DISPONIBLES por especialidad Y fecha
public List<TurnoDetalleDTO> buscarDisponiblesPorEspecialidadYFecha(String especialidad, LocalDate fecha) {
    List<Turno> turnos = turnoRepository
        .findByMedicoEspecialidadAndFechaAndEstadoAndPacienteIsNull(especialidad, fecha, EstadoTurno.AGENDADO);

    return turnos.stream().map(t -> {
        TurnoDetalleDTO dto = new TurnoDetalleDTO();
        dto.setId(t.getId());
        dto.setFechaHora(t.getFecha() + "T" + t.getHora());
        dto.setEstado(t.getEstado().name());
        dto.setMedicoNombre(t.getMedico().getUsuario().getNombre());
        dto.setMedicoEspecialidad(t.getMedico().getEspecialidad());
        return dto;
    }).toList();
}

// Turnos VIGENTES (futuros) por paciente, opcionalmente filtrados por especialidad
public List<Turno> buscarTurnosVigentes(Long pacienteId, String especialidad) {
    LocalDate hoy = LocalDate.now();

    if (especialidad != null && !especialidad.isEmpty()) {
        return turnoRepository.findByPacienteIdAndFechaAfterAndMedicoEspecialidad(
            pacienteId, hoy.minusDays(1), especialidad
        );
    } else {
        return turnoRepository.findByPacienteIdAndFechaAfter(pacienteId, hoy.minusDays(1));
    }
}

// Historial de turnos (pasados) por paciente, opcionalmente filtrados por especialidad
public List<Turno> buscarTurnosHistorial(Long pacienteId, String especialidad) {
    LocalDate hoy = LocalDate.now();

    if (especialidad != null && !especialidad.isEmpty()) {
        return turnoRepository.findByPacienteIdAndFechaBeforeAndMedicoEspecialidad(
            pacienteId, hoy, especialidad
        );
    } else {
        return turnoRepository.findByPacienteIdAndFechaBefore(pacienteId, hoy);
    }
}
public Turno marcarComoRealizado(Long id) {
    Turno turno = turnoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

    if (turno.getEstado() != EstadoTurno.AGENDADO) {
        throw new RuntimeException("Solo se pueden marcar como realizados los turnos agendados.");
    }

    turno.setEstado(EstadoTurno.REALIZADO);
    return turnoRepository.save(turno);
}

}
