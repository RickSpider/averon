select 
f.funcionarioid, 
f.idInterno, 
TRIM(
        COALESCE(NULLIF(p.nombre, ''), '') || ' ' ||
        COALESCE(NULLIF(p.apellido, ''), '')
    ) as nombreCompleto, 
documentonum, 
p.telefono, 
p.email, 
case when f.activo then 'SI' else 'NO' end
from funcionarios f
left join personas p on p.personaid = f.personaid
where f.empresaid = ?1
order by funcionarioid asc;