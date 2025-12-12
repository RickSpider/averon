Select 
p.personaid as id, 
p.idInterno, 
COALESCE(
    NULLIF(p.razonsocial, ''),
    TRIM(
        COALESCE(NULLIF(p.nombre, ''), '') || ' ' ||
        COALESCE(NULLIF(p.apellido, ''), '')
    )
) AS nombre_completo
COALESCE(td.tipo, p.documentonum, '')  as TipoDoc, 
COALESCE(p.ruc, p.documentonum, '') AS documento
from personas p 
left join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = ?1
order by p.personaid asc;