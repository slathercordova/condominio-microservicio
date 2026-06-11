select * from empresa;
select * from edificio;
select * from rol;
select * from usuario_edificio;
select * from usuario_edificio_rol;
select * from unidad;
select sum(a.metraje), sum(a.porcentaje) from unidad a where a.id_edificio = '00000000-0000-0000-0000-000000000000';
select r.nombre ,u.* from usuario_edificio_rol u inner join rol r on u.id_rol = r.id;

delete from usuario_edificio_rol a where a.id = '027e2ec0-0f88-4079-970e-72603e7f7141';