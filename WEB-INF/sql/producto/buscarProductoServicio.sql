Select p.productoid as id, p.nombre as Item , p.precioVenta  as Precio
from productos p
where p.empresaid = ?1
order by productoid asc;