package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.repository.UsuarioRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        // Mapeamos roles y permisos a autoridades para Spring Security
        Set<GrantedAuthority> autoridades = new HashSet<>();

        usuario.getRoles().forEach(rol -> {
            // Rol como autoridad: e.g. "ROLE_ADMIN"
            autoridades.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));

            // Permisos del rol: e.g. "READ", "CREATE", etc.
            rol.getPermisos().forEach(permiso -> {
                autoridades.add(new SimpleGrantedAuthority(permiso.getNombre()));
            });
        });

        // Retornamos el User de Spring Security
        return new User(
                usuario.getCorreo(),
                usuario.getContraseña(),
                autoridades
        );
    }
}
