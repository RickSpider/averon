Select 
p.personaid as id, 
p.idInterno, 
COALESCE(NULLIF(p.razonsocial, ''), p.nombre || ' ' || p.apellido) AS nombre_completo, 
COALESCE(td.tipo, p.documentonum, '')  as TipoDoc, 
COALESCE(p.ruc, p.documentonum, '') AS documento
from personas p 
left join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = 2
order by p.personaid asc;