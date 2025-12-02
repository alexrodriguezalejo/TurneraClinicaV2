package ps.tif.turnera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ps.tif.turnera.dto.GenerarTurnosDTO;
import ps.tif.turnera.dto.TurnoDTO;
import ps.tif.turnera.dto.TurnoDetalleDTO;
import ps.tif.turnera.entity.EstadoTurno;
import ps.tif.turnera.entity.Turno;
import ps.tif.turnera.service.TurnoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping
    public List<Turno> listarTodos() {
        return turnoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turno> obtenerPorId(@PathVariable Long id) {
        return turnoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public ResponseEntity<Turno> registrarTurno(@RequestBody TurnoDTO dto) {
        Turno turno = turnoService.crearDesdeDTO(dto);
        return ResponseEntity.ok(turno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente/{id}")
    public List<Turno> listarPorPaciente(@PathVariable Long id) {
        return turnoService.buscarPorPaciente(id);
    }

    // Turnos futuros (vigentes) del paciente, con filtro opcional por especialidad
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente/{id}/vigentes")
    public List<Turno> listarTurnosVigentes(
        @PathVariable Long id,
        @RequestParam(required = false) String especialidad
    ) {
        return turnoService.buscarTurnosVigentes(id, especialidad);
    }

    // Historial (anteriores) del paciente, con filtro opcional por especialidad
    @PreAuthorize("hasRole('PACIENTE')")
    @GetMapping("/paciente/{id}/historial")
    public List<Turno> listarHistorialTurnos(
        @PathVariable Long id,
        @RequestParam(required = false) String especialidad
    ) {
        return turnoService.buscarTurnosHistorial(id, especialidad);
    }

    @PreAuthorize("hasRole('MEDICO')")
    @GetMapping("/medico/{id}")
    public List<Turno> listarPorMedico(@PathVariable Long id) {
        return turnoService.buscarPorMedico(id);
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/generar-dia")
    public ResponseEntity<List<Turno>> generarTurnosParaDia(@RequestBody GenerarTurnosDTO dto) {
        List<Turno> turnos = turnoService.generarTurnosParaDia(dto);
        return ResponseEntity.ok(turnos);
    }

    @PreAuthorize("hasRole('PACIENTE')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarTurno(@PathVariable Long id) {
        try {
            Turno turnoCancelado = turnoService.cancelarTurno(id);
            return ResponseEntity.ok(turnoCancelado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
}


    @GetMapping("/disponibles/medico/{id}")
    public List<Turno> listarDisponiblesPorMedico(@PathVariable Long id) {
        return turnoService.buscarDisponiblesPorMedico(id);
    }

    @PatchMapping("/{id}/reservar")
    public ResponseEntity<Turno> reservarTurno(@PathVariable Long id, @RequestParam Long pacienteId) {
        Turno reservado = turnoService.reservarTurno(id, pacienteId);
        return ResponseEntity.ok(reservado);
    }

    // Permite buscar por especialidad y opcionalmente por fecha
    @GetMapping("/disponibles/especialidad/{especialidad}")
    public List<TurnoDetalleDTO> listarDisponiblesPorEspecialidadYFecha(
        @PathVariable String especialidad,
        @RequestParam(required = false) LocalDate fecha
    ) {
        if (fecha != null) {
            return turnoService.buscarDisponiblesPorEspecialidadYFecha(especialidad, fecha);
        } else {
            return turnoService.buscarDisponiblesPorEspecialidad(especialidad);
        }
    }

    // Filtro de turnos por fecha y estado, solo accesible por médicos
    @PreAuthorize("hasRole('MEDICO')")
    @GetMapping("/medico/{medicoId}/filtrar")
    public ResponseEntity<List<Turno>> filtrarTurnos(
        @PathVariable Long medicoId,
        @RequestParam LocalDate fecha,
        @RequestParam(required = false) EstadoTurno estado
    ) {
        List<Turno> turnos = (estado != null)
            ? turnoService.buscarPorMedicoFechaYEstado(medicoId, fecha, estado)
            : turnoService.buscarPorMedicoYFecha(medicoId, fecha);

        return ResponseEntity.ok(turnos);
    }

    @PreAuthorize("hasRole('MEDICO')")
@PatchMapping("/{id}/realizar")
public ResponseEntity<?> marcarComoRealizado(@PathVariable Long id) {
    try {
        Turno turno = turnoService.marcarComoRealizado(id);
        return ResponseEntity.ok(turno);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
}
