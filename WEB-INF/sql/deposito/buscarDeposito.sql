Select d.depositoid as id ,d.nombre as deposito, s.nombre as sucursal from depositos d
join sucursales s on s.sucursalid = d.sucursalid
where d.empresaid = ?1
order by d.depositoid asc;