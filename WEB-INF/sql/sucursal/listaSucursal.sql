Select s.sucursalid, 
s.idinterno, 
s.nombre, 
s.establecimiento 
from sucursales s
where s.empresaid = ?1
order by s.sucursalid asc;