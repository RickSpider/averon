Select p.productoid, 
p.idinterno,
p.nombre, 
m.nombre , 
tum.tipo, 
p.precioventa, 
tiva.tipo
from productos p
join tipos tiva on tiva.tipoid = p.ivatipoid
join tipos tum on tum.tipoid = p.unidadmedidatipoid
left join marcas m on m.marcaid = p.marcaid
where p.empresaid = ?1
and p.productotipoid = ?2
order by productoid asc;