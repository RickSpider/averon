select s.suscripcionid as id, p.documentonum as documento, p.razonsocial as Suscriptor, srv.nombre as Servicio, srv.precioventa as precio
from suscripciones s
join personas p on p.personaid = s.personaid
join productos srv on srv.productoid = s.servicioid
where s.empresaid = ?1
order by servicioid asc;