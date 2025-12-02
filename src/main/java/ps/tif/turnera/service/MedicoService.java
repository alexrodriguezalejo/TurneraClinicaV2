package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ps.tif.turnera.entity.Medico;
import ps.tif.turnera.entity.Rol;
import ps.tif.turnera.entity.Usuario;
import ps.tif.turnera.repository.MedicoRepository;
import ps.tif.turnera.repository.RolRepository;
import ps.tif.turnera.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> obtenerPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public Medico crear(Medico medico) {
        return medicoRepository.save(medico);
    }
    public Medico registrarDesdeUsuario(String correo, String matricula, String especialidad) {
    Usuario usuario = usuarioRepository.findByCorreo(correo)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    Optional<Medico> existente = medicoRepository.findByUsuarioId(usuario.getId());
    if (existente.isPresent()) {
        throw new RuntimeException("El usuario ya está registrado como médico");
    }

    // ❌ NO volver a asignar el rol MEDICO

    Medico medico = new Medico();
    medico.setUsuario(usuario);
    medico.setMatricula(matricula);
    medico.setEspecialidad(especialidad);

    return medicoRepository.save(medico);
}
    public Optional<Medico> buscarPorCorreoUsuario(String correo) {
        return medicoRepository.findByUsuarioCorreo(correo);
    }  


}
