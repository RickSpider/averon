Select p.productoid, idInterno ,p.nombre, p.precioventa, tiva.tipo
from productos p
join tipos tiva on tiva.tipoid = p.ivatipoid
where p.empresaid = ?1
and p.productotipoid = ?2
order by productoid asc;