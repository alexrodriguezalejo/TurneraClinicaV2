package ps.tif.turnera.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ps.tif.turnera.entity.AgendaMedica;
import ps.tif.turnera.entity.Medico;
import ps.tif.turnera.repository.AgendaMedicaRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgendaMedicaService {

    private final AgendaMedicaRepository agendaRepository;

    public AgendaMedica guardar(AgendaMedica agenda) {
        return agendaRepository.save(agenda);
    }

    public Optional<AgendaMedica> buscarPorMedico(Long medicoId) {
        return agendaRepository.findByMedicoId(medicoId);
    }
    
    public Optional<AgendaMedica> obtenerPorId(Long id) {
    return agendaRepository.findById(id);
    }


}
