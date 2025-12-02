package ps.tif.turnera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ps.tif.turnera.dto.RegistroAgendaMedicaDTO;
import ps.tif.turnera.entity.AgendaMedica;
import ps.tif.turnera.entity.Medico;
import ps.tif.turnera.service.AgendaMedicaService;
import ps.tif.turnera.service.MedicoService;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
public class AgendaMedicaController {

    private final AgendaMedicaService agendaService;
    private final MedicoService medicoService;

    @PreAuthorize("hasRole('MEDICO')")
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<AgendaMedica> obtenerPorMedico(@PathVariable Long medicoId) {
        return agendaService.buscarPorMedico(medicoId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/registrar")
    public ResponseEntity<AgendaMedica> registrar(@RequestBody RegistroAgendaMedicaDTO dto) {
        Medico medico = medicoService.obtenerPorId(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        AgendaMedica agenda = AgendaMedica.builder()
                .medico(medico)
                .mañanaInicio(LocalTime.parse(dto.getMañanaInicio()))
                .mañanaFin(LocalTime.parse(dto.getMañanaFin()))
                .tardeInicio(LocalTime.parse(dto.getTardeInicio()))
                .tardeFin(LocalTime.parse(dto.getTardeFin()))
                .duracionTurno(dto.getDuracionTurno())
                .build();

        return ResponseEntity.ok(agendaService.guardar(agenda));
    }
    
    @PreAuthorize("hasRole('MEDICO')")
    @PutMapping("/{id}")
    public ResponseEntity<AgendaMedica> actualizarAgenda(
        @PathVariable Long id,
        @RequestBody RegistroAgendaMedicaDTO dto) {

        AgendaMedica existente = agendaService.obtenerPorId(id)
            .orElseThrow(() -> new RuntimeException("Agenda no encontrada"));

        Medico medico = medicoService.obtenerPorId(dto.getMedicoId())
            .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        existente.setMedico(medico);
        existente.setMañanaInicio(LocalTime.parse(dto.getMañanaInicio()));
        existente.setMañanaFin(LocalTime.parse(dto.getMañanaFin()));
        existente.setTardeInicio(LocalTime.parse(dto.getTardeInicio()));
        existente.setTardeFin(LocalTime.parse(dto.getTardeFin()));
        existente.setDuracionTurno(dto.getDuracionTurno());

        return ResponseEntity.ok(agendaService.guardar(existente));
    }

}
