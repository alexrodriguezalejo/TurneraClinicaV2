package ps.tif.turnera.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegistroUsuarioDTO {
    private String nombre;
    private String correo;
    private String contraseña;
    private List<Long> rolesIds; // IDs de roles asignados al usuario
}
