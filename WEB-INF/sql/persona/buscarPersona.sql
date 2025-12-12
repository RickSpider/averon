Select 
p.personaid as id, 
p.idInterno, 
COALESCE(p.razonsocial, p.nombre || ' ' || p.apellido) AS nombre_completo, 
td.tipo as TipoDoc, 
COALESCE(p.ruc, p.documentonum, '') AS documento
from personas p 
left join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = ?1
order by p.personaid asc;
