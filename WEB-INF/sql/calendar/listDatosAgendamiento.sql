select 
a.agendamientoid,
a.inicio, 
a.fin, 
a.titulo, 
a.contenido, 
s.nombre, 
COALESCE(p.razonsocial, p.nombre || ' ' || p.apellido) AS nombre_completo, 
t.sigla,
te.color
from agendamientos a
left join productos s on s.productoid = a.servicioid 
left join personas p on p.personaid = a.personaid
join tipos t on t.tipoid = a.agendamientotipoid
left join tipos te on te.tipoid = a.estadotipoid
where te.sigla != 'AGENDAMIENTOESTADO_CANCELADO' 
and a.empresaid = ?1 
--and sucursalid = ?2
order by agendamientoid asc;