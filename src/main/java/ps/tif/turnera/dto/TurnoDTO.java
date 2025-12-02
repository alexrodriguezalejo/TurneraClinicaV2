package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TurnoDTO {
    private LocalDate fecha;
    private LocalTime hora;
    private Long medicoId;
    private Long pacienteId;
}
