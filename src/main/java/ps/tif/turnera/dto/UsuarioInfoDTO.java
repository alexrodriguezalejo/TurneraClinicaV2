// DTO para la info que devolveremos
package ps.tif.turnera.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioInfoDTO {
    private String correo;
    private String nombre;
    private String rol;  // asumimos que solo un rol principal
}
