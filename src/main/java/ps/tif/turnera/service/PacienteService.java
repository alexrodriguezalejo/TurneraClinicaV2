package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ps.tif.turnera.entity.Paciente;
import ps.tif.turnera.entity.Rol;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.repository.PacienteRepository;
import ps.tif.turnera.repository.RolRepository;
import ps.tif.turnera.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final RolRepository rolRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository; // 🆕 Agregamos para obtener el usuario

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorId(Long id) {
        return pacienteRepository.findById(id);
    }
    public Optional<Paciente> buscarPorCorreoUsuario(String correo) {
        return pacienteRepository.findByUsuarioCorreo(correo);
    }


    public Paciente guardarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }

    public Paciente registrarDesdeUsuario(String correo, String dni, String telefono) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<Paciente> existente = pacienteRepository.findByUsuarioId(usuario.getId());
        if (existente.isPresent()) {
            throw new RuntimeException("El usuario ya está registrado como paciente");
        }

    // Buscar el rol PACIENTE
        Rol rolPaciente = rolRepository.findByNombre("PACIENTE")
            .orElseThrow(() -> new RuntimeException("Rol PACIENTE no encontrado"));

        // Agregar solo si no lo tiene
        if (!usuario.getRoles().contains(rolPaciente)) {
            usuario.getRoles().add(rolPaciente);
            usuarioRepository.save(usuario); // importante guardar solo si cambia algo
        }

    // Crear paciente y asociarlo al usuario
        Paciente paciente = new Paciente();
        paciente.setDni(dni);
        paciente.setTelefono(telefono);
        paciente.setUsuario(usuario);

        return pacienteRepository.save(paciente);
    }

}
