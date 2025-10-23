Select c.cajaid, s.nombre, u.account, TO_CHAR(c.fechaapertura, 'DD/MM/YYYY HH24:MI:SS')as apertura, uc.account, TO_CHAR( c.fechacierre, 'DD/MM/YYYY HH24:MI:SS') as cierre
from cajas c
join sucursales s on s.sucursalid = c.sucursalid
join usuarios u on u.usuarioid = c.usuarioCajaid
left join usuarios uc on uc.usuarioid = c.usuariocierreid
where c.empresaid = ?1
order by cajaid desc;