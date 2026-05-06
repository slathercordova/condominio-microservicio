CREATE TYPE tipo_sexo AS ENUM (
  'MASCULINO',
  'FEMENINO'
);

CREATE TABLE tipo_documento (
  id uuid PRIMARY KEY,
  nombre varchar(50) NOT NULL,
  nombre_corto varchar(5) NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE persona (
  id uuid PRIMARY KEY,
  tipo_documento uuid NOT NULL,
  numero_documento varchar(20) NOT NULL,
  nacimiento date NOT NULL,
  celular varchar(20),
  celular_2 varchar(20),
  correo varchar(100),
  correo_2 varchar(100),
  nombres varchar(30) NOT NULL,
  apellido_paterno varchar(30) NOT NULL,
  apellido_materno varchar(30) NOT NULL,
  sexo tipo_sexo NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);


CREATE UNIQUE INDEX uq_tipdoc_nombre ON tipo_documento (nombre);

CREATE UNIQUE INDEX uq_persona_docum ON persona (tipo_documento, numero_documento);

COMMENT ON TABLE tipo_documento IS 'Tipos Documentos';

COMMENT ON TABLE persona IS 'Personas';

ALTER TABLE persona ADD CONSTRAINT fk_persona_tipdoc FOREIGN KEY (tipo_documento) REFERENCES tipo_documento (id) DEFERRABLE INITIALLY IMMEDIATE;

INSERT INTO tipo_documento (id, nombre, nombre_corto, estado, created_by)
VALUES ('11111111-1111-1111-1111-111111111111', 'DOCUMENTO NACIONAL DE IDENTIDAD', 'DNI', true, '11111111-1111-1111-1111-111111111111');

INSERT INTO persona (id, tipo_documento, numero_documento, nacimiento, celular, correo, nombres, apellido_paterno, apellido_materno, sexo, estado, created_by)
VALUES ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '00000000', '1900-01-01', '999999999', 'slathercordova@gmail.com', 'Admin', 'Sistema', 'Root',
  'MASCULINO', true, '11111111-1111-1111-1111-111111111111');