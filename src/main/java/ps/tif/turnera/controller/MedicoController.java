package ps.tif.turnera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ps.tif.turnera.dto.RegistroMedicoDesdeUsuarioDTO;
import ps.tif.turnera.entity.Medico;
import ps.tif.turnera.service.MedicoService;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Medico> listarTodos() {
        return medicoService.listarTodos();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtenerPorId(@PathVariable Long id) {
        return medicoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('MEDICO')")
    @PostMapping("/registrar")
    public ResponseEntity<Medico> registrarDesdeUsuario(@RequestBody RegistroMedicoDesdeUsuarioDTO dto) {
        Medico medico = medicoService.registrarDesdeUsuario(dto.getCorreo(), dto.getMatricula(), dto.getEspecialidad());
        return ResponseEntity.ok(medico);
    }

    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Medico> buscarPorCorreo(@PathVariable String correo) {
        return medicoService.buscarPorCorreoUsuario(correo)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

}
