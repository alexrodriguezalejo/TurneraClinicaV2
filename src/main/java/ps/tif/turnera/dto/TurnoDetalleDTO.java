package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnoDetalleDTO {
    private Long id;
    private String fechaHora; // ej: 2025-07-10T09:00
    private String estado;
    private String medicoNombre;
    private String medicoEspecialidad;
}
