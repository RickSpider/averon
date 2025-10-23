select a.agendamientoid, a.titulo, a.inicio, a.fin 
from agendamientos a
where a.empresaid = ?1 and a.sucursalid = ?2
order by agendamientoid desc;