Select p.productoid as id, m.nombre as marca ,p.nombre as Producto, p.precioVenta as precio 
from productos p
join tipos t on t.tipoid = p.productotipoid
left join marcas m on m.marcaid = p.marcaid
where p.empresaid = ?1
and t.sigla = 'PRODUCTO_PRODUCTO'
order by productoid asc;