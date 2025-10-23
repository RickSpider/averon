Select p.productoid as id, idInterno ,p.nombre as Servicio, p.precioVenta as precio 
from productos p
join tipos t on t.tipoid = p.productotipoid
where p.empresaid = ?1
and t.sigla ='PRODUCTO_SERVICIO'
order by productoid asc;