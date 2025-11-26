Select 
p.personaid, 
p.idInterno, 
COALESCE(NULLIF(p.razonsocial,''), p.nombre || ' ' || COALESCE(p.apellido, '')) AS nombre_completo, 
COALESCE(NULLIF(p.ruc,''), NULLIF(p.documentonum,''), '') AS documento,
p.email,
p.telefono,
p.fechanacimiento
from personas p 
left join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = ?1
order by p.personaid asc;
