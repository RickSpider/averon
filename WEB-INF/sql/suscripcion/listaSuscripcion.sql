select s.suscripcionid, p.razonsocial, p.documentonum  ,ps.nombre, TO_CHAR(s.inicio, 'DD/MM/YYYY') , TO_CHAR(s.vencimiento, 'DD/MM/YYYY') 
from suscripciones s
join personas p on p.personaid = s.personaid
join productos ps on ps.productoid = s.servicioid
where s.empresaid = ?1
order by suscripcionid;