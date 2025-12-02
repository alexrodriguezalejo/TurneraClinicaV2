package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ps.tif.turnera.entity.Rol;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.repository.AgendaMedicaRepository;
import ps.tif.turnera.repository.MedicoRepository;
import ps.tif.turnera.repository.PacienteRepository;
import ps.tif.turnera.repository.RolRepository;
import ps.tif.turnera.repository.UsuarioRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final AgendaMedicaRepository agendaMedicaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

 public void eliminar(Long id) {
        // 1. Buscar si hay un médico
        medicoRepository.findByUsuarioId(id).ifPresent(medico -> {
            // 2. Si hay agenda médica asociada, eliminarla
            agendaMedicaRepository.findByMedicoId(medico.getId())
                .ifPresent(agendaMedicaRepository::delete);

            // 3. Eliminar el médico
            medicoRepository.delete(medico);
        });

        // 4. Buscar si hay un paciente y eliminarlo
        pacienteRepository.findByUsuarioId(id)
            .ifPresent(pacienteRepository::delete);

        // 5. Eliminar el usuario
        usuarioRepository.deleteById(id);
    }

public Usuario crearUsuarioConRoles(String nombre, String correo, String contraseña, List<Long> rolesIds) {

        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        Set<Rol> roles = new HashSet<>();
        for (Long rolId : rolesIds) {
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));
            roles.add(rol);
        }

        Usuario nuevoUsuario = Usuario.builder()
                .nombre(nombre)
                .correo(correo)
                .contraseña(passwordEncoder.encode(contraseña))
                .roles(roles)
                .build();

        return usuarioRepository.save(nuevoUsuario);
    }

    public void modificarRoles(Long usuarioId, List<Long> rolesIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Set<Rol> nuevosRoles = new HashSet<>(rolRepository.findAllById(rolesIds));
        if (nuevosRoles.isEmpty()) {
            throw new RuntimeException("No se encontraron roles válidos");
        }

        usuario.setRoles(nuevosRoles);
        usuarioRepository.save(usuario);
}
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
