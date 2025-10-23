select a.ajusteid, a.fecha, d.nombre, ta.tipo from ajustes a
join depositos d on d.depositoid = a.depositoid
join tipos ta on ta.tipoid = a.ajustetipoid
where a.empresaid = ?1
order by a.ajusteid desc;