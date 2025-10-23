Select p.proveedorid, per.razonsocial, per.documentonum from proveedores p
join personas per on per.personaid = p.personaid
where p.empresaid = ?1
order by p.proveedorid asc;