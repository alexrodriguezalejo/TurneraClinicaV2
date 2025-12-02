package ps.tif.turnera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(ExcepcionPersonalizada.class)
    public ResponseEntity<Object> manejarExcepcionPersonalizada(ExcepcionPersonalizada ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());
        error.put("fecha", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarExcepcionesGenericas(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("mensaje", "Ha ocurrido un error inesperado");
        error.put("fecha", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
