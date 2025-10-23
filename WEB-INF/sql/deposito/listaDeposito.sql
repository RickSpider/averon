Select d.depositoid, s.nombre ,d.nombre from depositos d
join sucursales s on s.sucursalid = d.sucursalid
where d.empresaid = ?1
order by d.depositoid asc;