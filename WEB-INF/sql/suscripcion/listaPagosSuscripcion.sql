select TO_CHAR(v.fecha, 'DD/MM/YYYY HH24:MI:SS'), tc.tipo, v.comprobantenum 
from ventasdetalles vd
join ventas v on v.ventaid = vd.ventaid
join Tipos tc on tc.tipoid = v.comprobantetipoid
where vd.suscripcionid = ?1;