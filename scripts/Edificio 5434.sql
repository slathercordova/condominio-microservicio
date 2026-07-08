select * from empresa;
select * from edificio;
select * from rol;
select * from usuario_edificio;
select * from usuario_edificio_rol;
select * from unidad;
select * from persona_unidad;
select sum(a.metraje), sum(a.porcentaje) from unidad a where a.id_edificio = '00000000-0000-0000-0000-000000000000';
select sum(a.metraje), sum(a.porcentaje) from unidad a group by a.id_edificio;
select r.nombre ,u.* from usuario_edificio_rol u inner join rol r on u.id_rol = r.id;

delete from usuario_edificio_rol a where a.id = '027e2ec0-0f88-4079-970e-72603e7f7141';

select pu."id" as idPersonaUnidad, pu.id_persona as idPersona,
u."id" as idUnidad, u.codigo, u.metraje, u.porcentaje, u.tipo_unidad as tipoUnidad,
e."id" as idEdificio, e.nombre as edificioNombre, e.direccion as edificioDireccion
from persona_unidad pu
join unidad u on pu.id_unidad = u."id"
join edificio e on u.id_edificio = e."id"
--where pu.id_persona = :idPersona
;

update edificio set tipo_cobro = 'PORCENTAJE' where id = '00000000-0000-0000-0000-000000000000';