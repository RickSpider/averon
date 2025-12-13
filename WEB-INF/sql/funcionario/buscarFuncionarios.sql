select f.funcionarioid as id, 
f.idInterno, 
TRIM(
        COALESCE(NULLIF(p.nombre, ''), '') || ' ' ||
        COALESCE(NULLIF(p.apellido, ''), '')
    ) as nombreCompleto,
COALESCE(p.documentonum, '') AS documento
from funcionarios f
join personas p on p.personaid = f.personaid
where f.empresaid = ?1
order by funcionarioid asc;