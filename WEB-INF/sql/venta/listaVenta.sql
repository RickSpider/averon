select TO_CHAR(v.fecha, 'DD/MM/YYYY HH24:MI:SS') as fecha, tdoc.tipo ,v.personadocumento ,v.razonsocial, v.timbrado ,compt.tipo as comprobante, v.comprobantenum, condt.tipo as condicion, mon.tipo as moneda, (v.total10+v.total5+v.totalexento) as total
from ventas v
join tipos compt on compt.tipoid = v.comprobantetipoid
join tipos condt on condt.tipoid = v.condicionventatipoid
join tipos mon on mon.tipoid = v.monedatipoid
join tipos tdoc on tdoc.tipoid = v.personatocumentotipoid
where empresaid = ?1
and v.fecha between '?2' and '?3'
order by fecha desc;