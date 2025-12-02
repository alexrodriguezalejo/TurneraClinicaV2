package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPacienteDesdeUsuarioDTO {
    private String correo;   // Correo del usuario ya existente
    private String dni;
    private String telefono;
}
