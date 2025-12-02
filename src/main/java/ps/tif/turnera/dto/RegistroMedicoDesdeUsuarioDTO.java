package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroMedicoDesdeUsuarioDTO {
    private String correo; // con esto buscamos al usuario
    private String matricula;
    private String especialidad;
}
