package ps.tif.turnera.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "medico_id", nullable = false, unique = true)
    private Medico medico;

    private LocalTime mañanaInicio;  // 08:00
    private LocalTime mañanaFin;     // 12:00

    private LocalTime tardeInicio;   // 13:00
    private LocalTime tardeFin;      // 17:00

    private int duracionTurno; // en minutos (ej: 30)
}
