-- Insertar permisos
INSERT INTO permiso (nombre)
SELECT 'CREATE' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE nombre = 'CREATE');

INSERT INTO permiso (nombre)
SELECT 'READ' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE nombre = 'READ');

INSERT INTO permiso (nombre)
SELECT 'UPDATE' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE nombre = 'UPDATE');

INSERT INTO permiso (nombre)
SELECT 'DELETE' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM permiso WHERE nombre = 'DELETE');

-- Insertar roles
INSERT INTO rol (nombre)
SELECT 'ADMIN' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'ADMIN');

INSERT INTO rol (nombre)
SELECT 'MEDICO' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'MEDICO');

INSERT INTO rol (nombre)
SELECT 'PACIENTE' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'PACIENTE');

INSERT INTO rol (nombre)
SELECT 'USUARIO' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM rol WHERE nombre = 'USUARIO');

-- Asignar permisos a roles
-- ADMIN → todos los permisos
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM rol r, permiso p
WHERE r.nombre = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM rol_permiso rp WHERE rp.rol_id = r.id AND rp.permiso_id = p.id
  );

-- MEDICO → READ, UPDATE
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM rol r, permiso p
WHERE r.nombre = 'MEDICO'
  AND p.nombre IN ('READ','UPDATE')
  AND NOT EXISTS (
    SELECT 1 FROM rol_permiso rp WHERE rp.rol_id = r.id AND rp.permiso_id = p.id
  );

-- PACIENTE → CREATE, READ
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM rol r, permiso p
WHERE r.nombre = 'PACIENTE'
  AND p.nombre IN ('CREATE','READ')
  AND NOT EXISTS (
    SELECT 1 FROM rol_permiso rp WHERE rp.rol_id = r.id AND rp.permiso_id = p.id
  );

-- USUARIO → READ
INSERT INTO rol_permiso (rol_id, permiso_id)
SELECT r.id, p.id
FROM rol r, permiso p
WHERE r.nombre = 'USUARIO'
  AND p.nombre = 'READ'
  AND NOT EXISTS (
    SELECT 1 FROM rol_permiso rp WHERE rp.rol_id = r.id AND rp.permiso_id = p.id
  );

-- Usuario administrador
INSERT INTO usuarios (correo, contraseña, nombre)
SELECT 'admin@gmail.com', '$2a$10$10XivKLFI1oFll4728nBK.CC8WtDQnEItn.gGg8Jt1il1vNYGAbc.', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE correo = 'admin@gmail.com');

-- Asignar rol ADMIN al usuario 1
INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, rol r
WHERE u.correo = 'admin@gmail.com' AND r.nombre = 'ADMIN'
  AND NOT EXISTS (
    SELECT 1 FROM usuario_rol ur WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
  );
