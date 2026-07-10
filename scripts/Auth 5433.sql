SELECT a.* FROM public.usuario a;
delete from usuario a where a.id <> '11111111-1111-1111-1111-111111111111';
select * from usuario a where "id" = '093c1f79-42fe-4521-906c-baaf70cdb4a2';
select * from refresh_token a order by expiracion_at desc;

update usuario usu set usu.tipo_bloqueo = 'SIN_BLOQUEO' and usu.estado = 'ACTIVO' and usu.intento_erroneo = 0
where usu."id" = '00000000-0000-0000-0000-000000000000';