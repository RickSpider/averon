select 

s.suscripcionid id, 
s.vencimiento as v1, 
s.vencimiento as v2, 
serv.nombre as servicio, 
p.razonsocial as rsocial

from suscripciones s
join personas p on p.personaid = s.personaid
join productos serv on serv.productoid = s.servicioid
where s.empresaid = ?1
and s.activo = true
order by s.suscripcionid asc;