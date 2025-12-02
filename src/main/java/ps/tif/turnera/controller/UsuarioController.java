package ps.tif.turnera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ps.tif.turnera.dto.ModificarRolesDTO;
import ps.tif.turnera.dto.RegistroUsuarioDTO;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<Usuario> crearUsuarioConRoles(@RequestBody RegistroUsuarioDTO dto) {
        Usuario usuarioCreado = usuarioService.crearUsuarioConRoles(
                dto.getNombre(),
                dto.getCorreo(),
                dto.getContraseña(),
                dto.getRolesIds()
        );
        return ResponseEntity.ok(usuarioCreado);
    }
    
    @PreAuthorize("hasRole('ADMIN')")    
    @PatchMapping("/{id}/roles")
    public ResponseEntity<String> modificarRoles(
        @PathVariable Long id,
        @RequestBody ModificarRolesDTO dto) {
        usuarioService.modificarRoles(id, dto.getRolesIds());
        return ResponseEntity.ok("Roles actualizados correctamente");
}


}
