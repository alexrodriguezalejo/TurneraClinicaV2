package ps.tif.turnera.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ps.tif.turnera.entity.Turno;
import ps.tif.turnera.exception.ExcepcionPersonalizada;
import ps.tif.turnera.repository.TurnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class ValidadorTurno {

    @Autowired
private TurnoRepository turnoRepository;

    public void validarTurnoDuplicado(Turno turno) {
        boolean yaExiste = turnoRepository.existsByMedicoIdAndFechaAndHora(
            turno.getMedico().getId(),
            turno.getFecha(),
            turno.getHora()
        );

        if (yaExiste) {
            throw new ExcepcionPersonalizada("El médico ya tiene un turno en ese horario.");
        }
    }

    public void validarTurno(Turno turno) {
        LocalDate hoy = LocalDate.now();

        if (turno.getFecha().isBefore(hoy)) {
            throw new ExcepcionPersonalizada("No se puede agendar un turno en una fecha pasada.");
        }

        // Validar hora (por ejemplo, entre 08:00 y 18:00)
        LocalTime horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(18, 0);

        if (turno.getHora().isBefore(horaInicio) || turno.getHora().isAfter(horaFin)) {
            throw new ExcepcionPersonalizada("El turno debe ser entre las 08:00 y las 18:00.");
        }

        // Podés agregar más validaciones personalizadas acá si lo necesitás
    }
}
