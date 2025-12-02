package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ps.tif.turnera.entity.Rol;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.repository.UsuarioRepository;
import ps.tif.turnera.repository.RolRepository;
import ps.tif.turnera.config.JwtUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository; // ✅ AÑADIDO
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public String login(String correo, String contraseña) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, contraseña)
        );

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return jwtUtils.generarToken(usuario.getCorreo());
    }

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return usuarioRepository.save(usuario);
    }

    public void registrarConRol(Usuario usuario, String nombreRol) {
        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuario.setRoles(Set.of(rol));

        usuarioRepository.save(usuario);
    }
}
