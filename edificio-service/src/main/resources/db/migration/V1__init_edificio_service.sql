CREATE TYPE tipo_cobro AS ENUM (
  'PORCENTAJE',
  'FLAT',
  'CONSUMO'
);

CREATE TYPE periodo_mora AS ENUM (
  'DIARIO',
  'SEMANAL',
  'MENSUAL'
);

CREATE TYPE tipo_unidad AS ENUM (
  'DEPARTAMENTO',
  'COCHERA',
  'DEPOSITO',
  'LOCAL'
);

CREATE TYPE tipo_alquiler AS ENUM (
  'ALQUILER',
  'AIRBNB'
);

CREATE TYPE tipo_propiedad AS ENUM (
  'PROPIETARIO',
  'COPROPIETARIO',
  'APODERADO',
  'INQUILINO'
);

CREATE TYPE tipo_concepto AS ENUM (
  'ORDINARIO',
  'EXTRAORDINARIO'
);

CREATE TYPE estado_periodo AS ENUM (
  'BORRADOR',
  'GENERADO',
  'CERRADO',
  'ANULADO'
);

CREATE TYPE tipo_concepto_recibo AS ENUM (
  'SERVICIO_BASICOS',
  'MANTENIMIENTO_EQUIPOS',
  'ADMINISTRACION',
  'MORA',
  'MULTA',
  'PENALIDAD'
);

CREATE TYPE estado_recibo AS ENUM (
  'PENDIENTE',
  'PAGADO',
  'VENCIDO',
  'ANULADO'
);

CREATE TYPE origen_detalle AS ENUM (
  'GASTO_PERIODO',
  'CARGO_EXTRA'
);

CREATE TYPE estado_pago AS ENUM (
  'REGISTRADO',
  'CONFIRMADO',
  'ANULADO'
);

CREATE TYPE metodo_pago AS ENUM (
  'EFECTIVO',
  'TRANSFERENCIA',
  'YAPE',
  'PLIN',
  'TARJETA',
  'OTRO'
);

CREATE TYPE origen_ledger AS ENUM (
  'GASTO_PERIODO_UNIDAD',
  'CARGO_EXTRA',
  'PAGO',
  'RECIBO',
  'SISTEMA',
  'AJUSTE'
);

CREATE TYPE tipo_evento_ledger AS ENUM (
  'RECIBO_GENERADO',
  'CARGO_EXTRA',
  'MORA_APLICADA',
  'PAGO_REGISTRADO',
  'REVERSO_PAGO',
  'RECIBO_ANULADO',
  'AJUSTE_MANUAL'
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

CREATE TABLE empresa (
  id uuid PRIMARY KEY,
  razon_Social varchar(100) NOT NULL,
  direccion varchar(250) NOT NULL,
  ruc varchar(11) NOT NULL,
  telefono varchar(20),
  celular varchar(20),
  correo varchar(100),
  id_representante UUID NOT NULL,
  logo_url varchar(300),
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE edificio (
  id uuid PRIMARY KEY,
  id_empresa uuid,
  nombre varchar(100) NOT NULL,
  logo_url varchar(300),
  direccion varchar(250) NOT NULL,
  ruc varchar(11),
  contingencia numeric(6,2),
  tipo_cobro tipo_cobro NOT NULL,
  aplica_mora boolean NOT NULL,
  monto_mora numeric(10,2),
  periodo_mora periodo_mora,
  dia_generacion int NOT NULL,
  dia_vencimiento int NOT NULL,
  dia_gracia int NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE usuario_edificio (
  id uuid PRIMARY KEY,
  id_usuario uuid NOT NULL,
  id_edificio uuid NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE usuario_edificio_rol (
  id uuid PRIMARY KEY,
  id_usuario uuid NOT NULL,
  id_edificio uuid NOT NULL,
  id_rol uuid NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE unidad (
  id uuid PRIMARY KEY,
  id_edificio uuid NOT NULL,
  codigo varchar(10) NOT NULL,
  logo_url varchar(300),
  piso numeric(3) NOT NULL,
  torre varchar(10) NOT NULL,
  metraje numeric(10,3) NOT NULL,
  porcentaje numeric(6,3) NOT NULL,
  tipo_unidad tipo_unidad NOT NULL,
  tipo_alquiler tipo_alquiler,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE persona_unidad (
  id uuid PRIMARY KEY,
  id_unidad uuid NOT NULL,
  id_persona uuid NOT NULL,
  es_responsable boolean NOT NULL,
  fecha_inicio date NOT NULL,
  fecha_fin date,
  tipo_propiedad tipo_propiedad NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE concepto_base (
  id uuid PRIMARY KEY,
  nombre varchar(100) NOT NULL,
  tipo tipo_concepto NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE concepto_edificio (
  id uuid PRIMARY KEY,
  id_concepto_base uuid,
  id_edificio uuid NOT NULL,
  tipo tipo_concepto NOT NULL,
  es_recurrente boolean NOT NULL,
  descripcion varchar(100) NOT NULL,
  monto_base numeric(12,2) NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE periodo (
  id uuid PRIMARY KEY,
  ano int NOT NULL,
  mes int NOT NULL,
  id_edificio uuid NOT NULL,
  ss_contingencia numeric(5,2),
  ss_tipo_cobro tipo_cobro NOT NULL,
  ss_aplica_mora boolean NOT NULL,
  ss_monto_mora numeric(10,2),
  ss_periodo_mora periodo_mora,
  ss_dia_generacion int NOT NULL,
  ss_dia_vencimiento int NOT NULL,
  ss_dia_gracia int NOT NULL,
  fecha_generacion date NOT NULL,
  fecha_vencimiento date NOT NULL,
  estado_periodo estado_periodo NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE unidad_medidor_agua (
  id uuid PRIMARY KEY,
  id_unidad uuid NOT NULL,
  id_periodo uuid NOT NULL,
  lectura_anterior numeric(10,3) NOT NULL,
  lectura_actual numeric(10,3) NOT NULL,
  consumo numeric(10,3) NOT NULL,
  observacion varchar(200),
  foto_url varchar(300),
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE gasto_periodo (
  id uuid PRIMARY KEY,
  id_periodo uuid NOT NULL,
  id_concepto_edificio uuid,
  orden int NOT NULL,
  ss_tipo tipo_concepto NOT NULL,
  ss_es_recurrente boolean NOT NULL,
  ss_descripcion varchar(100) NOT NULL,
  ss_monto numeric(12,2) NOT NULL,
  ss_tipo_cobro tipo_cobro NOT NULL,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE gasto_periodo_unidad (
  id uuid PRIMARY KEY,
  id_gasto_periodo uuid NOT NULL,
  id_unidad uuid NOT NULL,
  ss_tipo tipo_concepto NOT NULL,
  ss_metraje numeric(10,3) NOT NULL,
  ss_porcentaje numeric(6,3) NOT NULL,
  concepto tipo_concepto_recibo NOT NULL,
  monto_base numeric(12,2) NOT NULL,
  monto_calculado numeric(12,2) NOT NULL,
  cantidad numeric(12,2),
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE unidad_cargo_extra (
  id uuid PRIMARY KEY,
  id_unidad uuid NOT NULL,
  id_periodo uuid NOT NULL,
  tipo tipo_concepto_recibo NOT NULL,
  descripcion varchar(100),
  fecha_origen date NOT NULL,
  monto numeric(12,2) NOT NULL,
  id_recibo_origen uuid,
  dias_atraso int,
  estado boolean NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE recibo (
  id uuid PRIMARY KEY,
  id_unidad uuid NOT NULL,
  id_periodo uuid NOT NULL,
  id_persona_responsable uuid NOT NULL,
  numero varchar(20) NOT NULL,
  fecha_emision date NOT NULL,
  fecha_vencimiento date NOT NULL,
  fecha_pago_total date,
  total_base numeric(12,2) NOT NULL,
  total_extra numeric(12,2) NOT NULL,
  total_pagar numeric(12,2) NOT NULL,
  estado estado_recibo NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE recibo_detalle (
  id uuid PRIMARY KEY,
  id_recibo uuid NOT NULL,
  orden int NOT NULL,
  descripcion varchar(100) NOT NULL,
  categoria tipo_concepto_recibo NOT NULL,
  monto numeric(12,2) NOT NULL,
  origen origen_detalle NOT NULL,
  id_origen uuid NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE pago (
  id uuid PRIMARY KEY,
  id_recibo uuid NOT NULL,
  fecha_pago date NOT NULL,
  monto numeric(12,2) NOT NULL,
  metodo metodo_pago NOT NULL,
  observacion varchar(200),
  estado estado_pago NOT NULL,
  created_by uuid NOT NULL,
  updated_by uuid,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz
);

CREATE TABLE ledger_recibo (
  id uuid PRIMARY KEY,
  id_recibo uuid NOT NULL,
  id_unidad uuid NOT NULL,
  id_periodo uuid NOT NULL,
  tipo_evento tipo_evento_ledger NOT NULL,
  monto numeric(12,2) NOT NULL,
  referencia_tipo origen_ledger NOT NULL,
  id_referencia uuid NOT NULL,
  descripcion varchar(200),
  created_by uuid NOT NULL,
  created_at timestamptz not NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_rol_nombre ON rol (nombre);

CREATE INDEX idx_empresa_raz_soci ON empresa (razon_Social);

CREATE UNIQUE INDEX uq_empresa_ruc ON empresa (ruc);

CREATE INDEX idx_edificio_nombre ON edificio (nombre);

CREATE INDEX idx_edificio_ruc ON edificio (ruc);

CREATE UNIQUE INDEX uq_edificio_empresa_nombre ON edificio (id_empresa, nombre);

CREATE UNIQUE INDEX uq_usuario_edificio ON usuario_edificio (id_usuario, id_edificio);

CREATE INDEX idx_usuario_edificio_usuario ON usuario_edificio (id_usuario);

CREATE INDEX idx_usuario_edificio_edificio ON usuario_edificio (id_edificio);

CREATE UNIQUE INDEX uq_usuario_edificio_rol ON usuario_edificio_rol (id_usuario, id_edificio, id_rol);

CREATE UNIQUE INDEX uq_unidad_edificio_unidad ON unidad (id_edificio, codigo);

CREATE INDEX idx_unidad_ubicacion ON unidad (torre, piso, codigo);

CREATE INDEX idx_unidad_metraje ON unidad (metraje);

CREATE INDEX idx_unidad_porcentaje ON unidad (porcentaje);

CREATE UNIQUE INDEX uq_persona_unidad_prop_ini ON persona_unidad (id_unidad, id_persona, fecha_inicio);

CREATE INDEX idx_persona_unidad_unidad ON persona_unidad (id_unidad);

CREATE INDEX idx_persona_unidad_persona ON persona_unidad (id_persona);

CREATE INDEX idx_persona_unidad_tipo_propiedad ON persona_unidad (tipo_propiedad);

CREATE INDEX idx_persona_unidad_fin ON persona_unidad (id_unidad, fecha_fin);

CREATE UNIQUE INDEX uq_concepto_base_nombre ON concepto_base (nombre);

CREATE INDEX idx_concepto_base_tipo ON concepto_base (tipo);

CREATE INDEX idx_concepto_edificio_recurrente_tipo ON concepto_edificio (id_edificio, es_recurrente, tipo);

CREATE UNIQUE INDEX uq_concepto_edificio_desc_concepto ON concepto_edificio (id_edificio, descripcion);

CREATE UNIQUE INDEX uq_periodo_ano_mes ON periodo (id_edificio, ano, mes);

CREATE UNIQUE INDEX uq_unidad_medidor_agua_periodo_unidad ON unidad_medidor_agua (id_unidad, id_periodo);

CREATE UNIQUE INDEX uq_gasto_periodo_orden ON gasto_periodo (id_periodo, orden);

CREATE INDEX idx_gasto_periodo_descripcion ON gasto_periodo (id_periodo, ss_descripcion);

CREATE UNIQUE INDEX uq_gasto_periodo_unidad ON gasto_periodo_unidad (id_gasto_periodo, id_unidad);

CREATE INDEX idx_gasto_periodo_unidad_unidad ON gasto_periodo_unidad (id_unidad);

CREATE INDEX idx_unidad_cargo_extra ON unidad_cargo_extra (id_periodo, id_unidad);

CREATE INDEX idx_unidad_cargo_extra_unidad ON unidad_cargo_extra (id_unidad);

CREATE UNIQUE INDEX uq_recibo_periodo_unidad ON recibo (id_unidad, id_periodo);

CREATE UNIQUE INDEX uq_recibo_periodo_recibo ON recibo (numero, id_periodo);

CREATE INDEX idx_recibo_periodo ON recibo (id_periodo);

CREATE UNIQUE INDEX uq_recibo_detalle_orden ON recibo_detalle (id_recibo, orden);

CREATE INDEX idx_pago_recibo_fecha ON pago (id_recibo, fecha_pago);

CREATE INDEX idx_pago_fecha ON pago (fecha_pago);

CREATE INDEX idx_ledger_recibo_fecha ON ledger_recibo (id_recibo, created_at);

CREATE INDEX idx_ledger_unidad_periodo ON ledger_recibo (id_unidad, id_periodo);

COMMENT ON TABLE rol IS 'Roles';

COMMENT ON TABLE empresa IS 'Empresas';

COMMENT ON TABLE edificio IS 'Edificios';

COMMENT ON COLUMN edificio.contingencia IS 'Contingencia para el calculo mensual';

COMMENT ON TABLE usuario_edificio IS 'Usuarios por Edificios';

COMMENT ON TABLE usuario_edificio_rol IS 'Usuarios x Edificios x Roles';

COMMENT ON TABLE unidad IS 'Unidades del edificio';

COMMENT ON COLUMN unidad.piso IS 'Debe aceptar negativos para los sótanos';

COMMENT ON COLUMN unidad.torre IS 'NO Debe aceptar negativos, podría ser letras';

COMMENT ON COLUMN unidad.porcentaje IS 'Validar que el total sumen 100%, sino no se podrá generar los recibos de un periodo';

COMMENT ON TABLE persona_unidad IS 'Muestra el historial de persona_unidad
En una unidad:
Puede haber muchos propietarios históricos (controlar con copropietarios y propietarios)
Puede haber muchos copropietarios activos
Solo 1 responsable activo = note: UNIQUE (id_unidad) WHERE
es_responsable = true AND fecha_fin IS NULL';

COMMENT ON COLUMN persona_unidad.es_responsable IS 'a nivel lógico Solo 1 registro activo por unidad puede tener esResponsable = true';

COMMENT ON TABLE concepto_base IS 'Conceptos base';

COMMENT ON TABLE concepto_edificio IS 'Conceptos X edificio';

COMMENT ON COLUMN concepto_edificio.id_concepto_base IS 'podría ser nulo ya que se incluye conceptos personalizados que no existen en base';

COMMENT ON COLUMN concepto_edificio.es_recurrente IS 'si es false no tendrá id_concepto_base';

COMMENT ON TABLE periodo IS 'Periodos';

COMMENT ON COLUMN periodo.estado_periodo IS 'debería haber solo 1 activo por edificio';

COMMENT ON TABLE unidad_medidor_agua IS 'Medidor de agua';

COMMENT ON TABLE gasto_periodo IS 'Gastos del periodo';

COMMENT ON TABLE gasto_periodo_unidad IS 'Gastos del periodo por unidad';

COMMENT ON COLUMN recibo.numero IS 'correlativo por edificio';

COMMENT ON COLUMN recibo_detalle.id_origen IS 'id_gasto_periodo_unidad o id_unidad_cargo_extra';

ALTER TABLE edificio ADD CONSTRAINT fk_edificio_empresa FOREIGN KEY (id_empresa) REFERENCES empresa (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE usuario_edificio ADD CONSTRAINT fk_usuario_edificio_edificio FOREIGN KEY (id_edificio) REFERENCES edificio (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE usuario_edificio_rol ADD CONSTRAINT fk_usuario_edificio_rol_usu_edif FOREIGN KEY (id_usuario, id_edificio) REFERENCES usuario_edificio (id_usuario, id_edificio) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE usuario_edificio_rol ADD CONSTRAINT fk_usuario_edificio_rol_rol FOREIGN KEY (id_rol) REFERENCES rol (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE unidad ADD CONSTRAINT fk_unidad_edificio FOREIGN KEY (id_edificio) REFERENCES edificio (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE persona_unidad ADD CONSTRAINT fk_persona_unidad_unidad FOREIGN KEY (id_unidad) REFERENCES unidad (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE concepto_edificio ADD CONSTRAINT fk_concepto_edificio_concepto_base FOREIGN KEY (id_concepto_base) REFERENCES concepto_base (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE concepto_edificio ADD CONSTRAINT fk_concepto_edificio_edificio FOREIGN KEY (id_edificio) REFERENCES edificio (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE periodo ADD CONSTRAINT fk_periodo_edificio FOREIGN KEY (id_edificio) REFERENCES edificio (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE unidad_medidor_agua ADD CONSTRAINT fk_unidad_medidor_agua_unidad FOREIGN KEY (id_unidad) REFERENCES unidad (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE unidad_medidor_agua ADD CONSTRAINT fk_unidad_medidor_agua_periodo FOREIGN KEY (id_periodo) REFERENCES periodo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE gasto_periodo ADD CONSTRAINT fk_gasto_periodo_periodo FOREIGN KEY (id_periodo) REFERENCES periodo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE gasto_periodo ADD CONSTRAINT fk_gasto_periodo_edificio FOREIGN KEY (id_concepto_edificio) REFERENCES concepto_edificio (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE gasto_periodo_unidad ADD CONSTRAINT fk_gasto_periodo_unidad_periodo FOREIGN KEY (id_gasto_periodo) REFERENCES gasto_periodo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE gasto_periodo_unidad ADD CONSTRAINT fk_gasto_periodo_unidad_unidad FOREIGN KEY (id_unidad) REFERENCES unidad (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE unidad_cargo_extra ADD CONSTRAINT fk_unidad_cargo_extra_periodo FOREIGN KEY (id_periodo) REFERENCES periodo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE unidad_cargo_extra ADD CONSTRAINT fk_unidad_cargo_extra_unidad FOREIGN KEY (id_unidad) REFERENCES unidad (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE unidad_cargo_extra ADD CONSTRAINT fk_unidad_cargo_extra_recibo FOREIGN KEY (id_recibo_origen) REFERENCES recibo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE recibo ADD CONSTRAINT fk_recibo_unidad FOREIGN KEY (id_unidad) REFERENCES unidad (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE recibo ADD CONSTRAINT fk_recibo_periodo FOREIGN KEY (id_periodo) REFERENCES periodo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE recibo_detalle ADD CONSTRAINT fk_recibo_detalle_recibo FOREIGN KEY (id_recibo) REFERENCES recibo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE pago ADD CONSTRAINT fk_pago_recibo FOREIGN KEY (id_recibo) REFERENCES recibo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE ledger_recibo ADD CONSTRAINT fk_ledger_recibo FOREIGN KEY (id_recibo) REFERENCES recibo (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE ledger_recibo ADD CONSTRAINT fk_ledger_unidad FOREIGN KEY (id_unidad) REFERENCES unidad (id) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE ledger_recibo ADD CONSTRAINT fk_ledger_periodo FOREIGN KEY (id_periodo) REFERENCES periodo (id) DEFERRABLE INITIALLY IMMEDIATE;

insert into empresa (id,razon_Social,direccion,ruc,telefono,celular,correo,id_representante,logo_url,estado,created_by)
values('00000000-0000-0000-0000-000000000000','EMPRESA DE PRUEBA','Av. Cuba 123','11111111111',null,null,null,'22222222-2222-2222-2222-222222222222',null,true,'00000000-0000-0000-0000-000000000000');

insert into edificio (id,id_empresa,nombre,logo_url,direccion,ruc,contingencia,tipo_cobro,aplica_mora,monto_mora,periodo_mora,dia_generacion,dia_vencimiento,dia_gracia,estado,created_by)
values
('00000000-0000-0000-0000-000000000000',null,'EDIFICIO TALARA',null,'Av. Talara 765',null,6,'PORCENTAJE',true,1,'DIARIO',25,5,0,true,'00000000-0000-0000-0000-000000000000'),
('11111111-1111-1111-1111-111111111111',null,'EDIFICIO PRUEBA',null,'Av. Prueba 123',null,10,'PORCENTAJE',true,5,'DIARIO',15,30,5,true,'00000000-0000-0000-0000-000000000000'),
('22222222-2222-2222-2222-222222222222',null,'EDIFICIO PRUEBA 2',null,'Av. Prueba 456',null,8,'PORCENTAJE',true,10,'DIARIO',25,30,2,true,'00000000-0000-0000-0000-000000000000');

insert into usuario_edificio (id,id_usuario,id_edificio,estado,created_by)
values
('00000000-0000-0000-0000-000000000000','00000000-0000-0000-0000-000000000000','00000000-0000-0000-0000-000000000000',true,'00000000-0000-0000-0000-000000000000'),
('11111111-1111-1111-1111-111111111111','00000000-0000-0000-0000-000000000000','11111111-1111-1111-1111-111111111111',true,'00000000-0000-0000-0000-000000000000'),
('22222222-2222-2222-2222-222222222222','00000000-0000-0000-0000-000000000000','22222222-2222-2222-2222-222222222222',true,'00000000-0000-0000-0000-000000000000'),
('33333333-3333-3333-3333-333333333333','22222222-2222-2222-2222-222222222222','00000000-0000-0000-0000-000000000000',true,'00000000-0000-0000-0000-000000000000');

insert into rol(id, nombre, estado, created_by)
values
('00000000-0000-0000-0000-000000000000','ADMINISTRADOR',true,'00000000-0000-0000-0000-000000000000'),
('11111111-1111-1111-1111-111111111111','ADMINISTRACION',true,'00000000-0000-0000-0000-000000000000'),
('22222222-2222-2222-2222-222222222222','PROPIETARIO',true,'00000000-0000-0000-0000-000000000000');

insert into usuario_edificio_rol(id,id_usuario,id_edificio,id_rol,estado,created_by)
values
('00000000-0000-0000-0000-000000000000','00000000-0000-0000-0000-000000000000','00000000-0000-0000-0000-000000000000','00000000-0000-0000-0000-000000000000',true,'00000000-0000-0000-0000-000000000000'),
('11111111-1111-1111-1111-111111111111','00000000-0000-0000-0000-000000000000','11111111-1111-1111-1111-111111111111','00000000-0000-0000-0000-000000000000',true,'00000000-0000-0000-0000-000000000000'),
('22222222-2222-2222-2222-222222222222','00000000-0000-0000-0000-000000000000','22222222-2222-2222-2222-222222222222','00000000-0000-0000-0000-000000000000',true,'00000000-0000-0000-0000-000000000000');