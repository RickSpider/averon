select f.funcionarioid, f.idInterno, p.nombre||' '||p.apellido, documentonum, p.telefono, p.email, f.activo
from funcionarios f
left join personas p on p.personaid = f.personaid
where f.empresaid = ?1
order by funcionarioid asc;