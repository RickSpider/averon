Select 
p.personaid, 
p.idInterno, 
COALESCE(p.razonsocial, p.nombre || ' ' || p.apellido) AS nombre_completo, 
COALESCE(p.ruc, p.documentonum, '') AS documento,
p.email,
p.telefono,
p.fechanacimiento
from personas p 
join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = ?1
order by p.personaid asc;
