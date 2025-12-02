package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroAgendaMedicaDTO {
    private Long medicoId;
    private String mañanaInicio;
    private String mañanaFin;
    private String tardeInicio;
    private String tardeFin;
    private int duracionTurno;
}
