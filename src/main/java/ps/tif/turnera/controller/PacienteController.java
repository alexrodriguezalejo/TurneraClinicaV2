package ps.tif.turnera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ps.tif.turnera.dto.RegistroPacienteDesdeUsuarioDTO;
import ps.tif.turnera.entity.Paciente;
import ps.tif.turnera.service.PacienteService;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping
    public List<Paciente> listarTodos() {
        return pacienteService.listarPacientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public ResponseEntity<Paciente> registrarPacienteDesdeUsuario(@RequestBody RegistroPacienteDesdeUsuarioDTO dto) {
        Paciente paciente = pacienteService.registrarDesdeUsuario(dto.getCorreo(), dto.getDni(), dto.getTelefono());
        return ResponseEntity.ok(paciente);
    }
    
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Paciente> obtenerPorCorreo(@PathVariable String correo) {
        return pacienteService.buscarPorCorreoUsuario(correo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    

}
