Select c.comprobanteid, t.tipo ,c.timbrado, (s.establecimiento||' ('||s.nombre||')') as establecimiento, c.puntoexpedicion, TO_CHAR(c.emision, 'DD/MM/YYYY'),TO_CHAR(c.vencimiento, 'DD/MM/YYYY') , c.inicio, c.fin, c.activo 
from comprobantes c
join sucursales s on s.sucursalid = c.sucursalid
join tipos t on t.tipoid = c.comprobanteTipoId
where c.empresaid = ?1
order by c.comprobanteid desc;