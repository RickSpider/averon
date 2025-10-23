select f.funcionarioid as id, 
f.idInterno, 
p.nombre||' '||p.apellido as nombreCompleto, 
p.documentonum
from funcionarios f
join personas p on p.personaid = f.personaid
where f.empresaid = ?1
order by funcionarioid asc;