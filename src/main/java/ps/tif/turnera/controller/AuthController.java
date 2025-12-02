package ps.tif.turnera.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import ps.tif.turnera.dto.LoginRequest;
import ps.tif.turnera.dto.RegistroDTO;
import ps.tif.turnera.dto.UsuarioInfoDTO;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.service.AuthService;
import ps.tif.turnera.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    
    @GetMapping("/me")
    public ResponseEntity<UsuarioInfoDTO> obtenerInfoUsuario(Authentication authentication) {
        String correo = authentication.getName();
        Usuario usuario = usuarioService.buscarPorCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Asumimos que el usuario tiene un rol principal para simplificar
        String rol = usuario.getRoles().stream().findFirst()
                .map(r -> r.getNombre())
                .orElse("USUARIO");

        UsuarioInfoDTO info = new UsuarioInfoDTO(usuario.getCorreo(), usuario.getNombre(), rol);
        return ResponseEntity.ok(info);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getCorreo(), request.getContraseña());
        return ResponseEntity.ok(token);
    }
    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody RegistroDTO request) {
        Usuario usuario = new Usuario();
        usuario.setCorreo(request.getCorreo());
        usuario.setContraseña(request.getContraseña());
        usuario.setNombre(request.getNombre());

        authService.registrarConRol(usuario, "USUARIO"); // Usa "USUARIO" como nombre de rol

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

}
