select 
case 
	when p.nombre is not null and  p.apellido is not null then (p.nombre||' '||p.apellido)
	else p.razonsocial
	end,
p.fechanacimiento
from personas p
where p.empresaid = ?1 
and p.fechanacimiento is not null;