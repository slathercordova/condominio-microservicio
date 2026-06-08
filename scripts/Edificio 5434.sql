select * from empresa;
select * from edificio;
select * from usuario_edificio;
select * from rol;
select r.nombre ,u.* from usuario_edificio_rol u inner join rol r on u.id_rol = r.id;

