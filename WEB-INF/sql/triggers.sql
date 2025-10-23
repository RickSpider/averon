CREATE OR REPLACE FUNCTION set_idinterno_generico()
RETURNS trigger AS $$
DECLARE
    seq_name TEXT;
    next_val BIGINT;
BEGIN
    -- Construimos el nombre del secuenciador dinámicamente:
    -- ejemplo: SEQ_personas_1_ID
    seq_name := 'seq' || lower(TG_TABLE_NAME) || '_' || NEW.empresaId;

    BEGIN
        -- Intentamos obtener el siguiente valor
        EXECUTE format('SELECT nextval(%L)', seq_name) INTO next_val;
    EXCEPTION WHEN undefined_table THEN
        -- Si no existe la secuencia, la creamos automáticamente
        EXECUTE format('CREATE SEQUENCE %I START 1', seq_name);
        EXECUTE format('SELECT nextval(%L)', seq_name) INTO next_val;
    END;

    -- Asignamos el valor al campo idInterno
    NEW.idInterno := next_val;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Personas
CREATE TRIGGER trg_set_idinterno_personas
BEFORE INSERT ON personas
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Productos
CREATE TRIGGER trg_set_idinterno_productos
BEFORE INSERT ON productos
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Marcas
CREATE TRIGGER trg_set_idinterno_marcas
BEFORE INSERT ON marcas
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Depositos
CREATE TRIGGER trg_set_idinterno_depositos
BEFORE INSERT ON depositos
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Sucursales
CREATE TRIGGER trg_set_idinterno_sucursales
BEFORE INSERT ON sucursales
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Funcionarios
CREATE TRIGGER trg_set_idinterno_funcionarios
BEFORE INSERT ON funcionarios
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Agendamientos
CREATE TRIGGER trg_set_idinterno_agendamientos
BEFORE INSERT ON agendamientos
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

-- Proveedores
CREATE TRIGGER trg_set_idinterno_proveedores
BEFORE INSERT ON proveedores
FOR EACH ROW
EXECUTE FUNCTION set_idinterno_generico();

