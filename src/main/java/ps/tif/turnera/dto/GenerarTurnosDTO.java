package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GenerarTurnosDTO {
    private Long medicoId;
    private LocalDate fecha;
}
