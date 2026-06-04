CREATE TYPE estado_usuario AS ENUM (
  'ACTIVO',
  'INACTIVO',
  'BLOQUEADO'
);

CREATE TYPE tipo_bloqueo AS ENUM (
  'ADMINISTRADOR',
  'INACTIVIDAD',
  'OLVIDE_CONTRASEÑA',
  'SIN_BLOQUEO',
  'INTENTOS_FALLIDOS'
);

CREATE TABLE usuario (
  id uuid PRIMARY KEY,
  id_persona uuid NOT NULL,
  username varchar(30) NOT NULL,
  password varchar(255) NOT NULL,
  correo varchar(100),
  correo_2 varchar(100),
  ultimo_login timestamptz,
  intento_erroneo int NOT NULL DEFAULT 0,
  bloqueo_at timestamptz,
  tipo_bloqueo tipo_bloqueo NOT NULL,
  primera_vez boolean NOT NULL DEFAULT true,
  estado estado_usuario NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE rol (
  id uuid PRIMARY KEY,
  nombre varchar(30) NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE refresh_token(
    id uuid PRIMARY KEY,
    usuario_id uuid NOT NULL,
    token_hash varchar(255) NOT NULL,
    expiracion_at timestamptz NOT NULL,
    usado boolean NOT NULL DEFAULT FALSE,
    revocado boolean NOT NULL DEFAULT FALSE,
    dispositivo varchar(100),
    ip varchar(15),
    created_by uuid NOT NULL,
    updated_by uuid,
    created_at timestamptz NOT NULL,
    updated_at timestamptz
);

CREATE UNIQUE INDEX uq_usuario_username ON usuario (username);

CREATE UNIQUE INDEX uq_usuario_correo ON usuario (correo);

CREATE UNIQUE INDEX uq_usuario_persona ON usuario (id_persona);

CREATE UNIQUE INDEX uq_rol_nombre ON rol (nombre);

CREATE INDEX idx_refresh_usuario ON refresh_token(usuario_id);

CREATE INDEX idx_refresh_hash ON refresh_token(token_hash);

COMMENT ON TABLE usuario IS 'Usuarios';

COMMENT ON TABLE rol IS 'Roles';

ALTER TABLE usuario ADD CONSTRAINT fk_usuario_usuario_ins FOREIGN KEY (created_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE usuario ADD CONSTRAINT fk_usuario_usuario_upd FOREIGN KEY (updated_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE rol ADD CONSTRAINT fk_rol_usuario_ins FOREIGN KEY (created_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE rol ADD CONSTRAINT fk_rol_usuario_upd FOREIGN KEY (updated_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE refresh_token ADD CONSTRAINT fk_refresh_token_usuario_ins FOREIGN KEY (created_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE refresh_token ADD CONSTRAINT fk_refresh_token_usuario_upd FOREIGN KEY (updated_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;

-- insert de primer usuario
ALTER TABLE usuario DROP CONSTRAINT fk_usuario_usuario_ins;

INSERT INTO usuario (id, id_persona, username, password, correo, tipo_bloqueo, primera_vez, estado, created_by)
VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'ADMIN', '$2a$10$cQL.hWmztP4t./i9yeRcP.lHhxJuzG7/0TbrApLmHkbPO2SMh1zLe',
        '00000@xxx.xxx', 'SIN_BLOQUEO', false, 'ACTIVO','00000000-0000-0000-0000-000000000000');

INSERT INTO usuario (id, id_persona, username, password, correo, tipo_bloqueo, primera_vez, estado, created_by)
VALUES ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'PASSWORD_RECOVERY_JOB', '$2a$10$cQL.hWmztP4t./i9yeRcP.lHhxJuzG7/0TbrApLmHkbPO2SMh1zLe',
        '11111@xxx.xxx', 'SIN_BLOQUEO', false, 'ACTIVO','00000000-0000-0000-0000-000000000000');

INSERT INTO usuario (id, id_persona, username, password, correo, tipo_bloqueo, primera_vez, estado, created_by)
VALUES ('22222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 'LCORDOVA', '$2a$10$cQL.hWmztP4t./i9yeRcP.lHhxJuzG7/0TbrApLmHkbPO2SMh1zLe',
        'slathercordova@gmail.com', 'SIN_BLOQUEO', false, 'ACTIVO','00000000-0000-0000-0000-000000000000');

ALTER TABLE usuario ADD CONSTRAINT fk_usuario_usuario_ins FOREIGN KEY (created_by) REFERENCES usuario (id) DEFERRABLE INITIALLY IMMEDIATE;