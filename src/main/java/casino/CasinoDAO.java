package casino;

import exceptions.ClientNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface CasinoDAO {
    //Metodos CRUD

    /**
     * Añade un objeto cliente al almacén
     * @param cliente objeto que recibe como parámetro para agregar al almacén
     */
    public void addCliente(Cliente cliente);

    /**
     * Añade un objeto Servicio al almacén
     * @param servicio objeto que recibe como parámetro para agregar al almacén
     */
    public void addServicio(Servicio servicio);

    /**
     * Añade un objeto Log al almacén
     * @param log objeto que recibe como parámetro para agregar al almacén
     */
    public void addLog(Log log);

    /**
     * Consulta toda la información del Servicio
     * @param codigo único del servicio del que se quiere consultar la información
     * @return String con la información del servicio solicitado
     */
    public String consultaServicio(String codigo);

    /**
     * Consulta todos los servicios almacenados
     * @return List con todos los servicios que tenemos
     */
    public List<Servicio> leerListaServicios();

    /**
     * Consulta la información de un cliente
     * @param dni String único de un cliente
     * @return String con toda la información del cliente solicitado
     */
    public String consultaCliente(String dni) throws ClientNotFoundException;

    /**
     * Consulta todos los clientes registrados
     * @return List con todos los clientes registrados
     */
    public List<Cliente> leerListaClientes();

    /**
     * Consulta un Log específico
     * @param codigo del servicio
     * @param dni del cliente
     * @param fecha del log
     * @return String con la informacion
     */
    public String consultaLog(String codigo, String dni, LocalDate fecha);

    /**
     * Consulta todos los Log almacenados
     * @return List con todos los Log
     */
    public List<Log> leerListaLog();

    /**
     * Actualiza la información de un Servicio
     * @param codigo único de un Servicio
     * @return True si se ha podido actualizar los datos de la mesa
     */
    public boolean actualizarServicio(String codigo);

    /**
     * Actualiza la información de un Cliente
     * @param dni único que permite identificar al Cliente
     * @return True si se ha podido actualizar los datos del Cliente
     */
    public boolean actualizarCliente(String dni);

    /**
     * Borra una mesa
     * @param servicio recibe el objeto Mesa
     * @return True si se ha podido eliminar el objeto
     */
    public boolean borrarServicio(Servicio servicio);

    /**
     * Borra un cliente
     * @param cliente, recibe el objeto Cliente
     * @return True si se ha podido eliminar la mesa
     */
    public boolean borrarCliente(Cliente cliente);



    //Metodos NO CRUD BÁSICOS
    /**
     * Devuelve el valor del dinero invertido en comida/bebida de un cliente
     * @param dni
     * @param concepto
     * @return variable gastado en alimentos por cliente
     */
    public double GanaciasAlimentos(String dni, String concepto);

    /**
     * Devuelve el valor del dinero invertido por un cliente en el casino
     * @param dni a devolver lo invertido en el casino
     * @return Lo gastado en el casino por cliente
     */
    public double dineroGanadoClienteEnDia(String dni, LocalDate fecha);

    /**
     * Devuelve la cantidad de veces que un cliente ha jugado en una mesa
     * @param dni
     * @param codigo
     * @return La cantidad de veces que ha jugado cliente en una mesa
     */
    public int vecesClienteJuegaMesa(String dni, String codigo);


    /**
     * Devuelve el total ganado en mesas
     * @return double con el total ganado en mesas
     */
    public double ganadoMesas();

    /**
     * Devuelve el total de lo ganado en establecimientos
     * @return double con el total ganado en establecimientos
     */
    public double ganadoEstablecimientos();

    /**
     * Devuelve una lista con las mesas que sean de tipoJuego
     * @param tipoServicio a buscar en el archivo
     * @return Lista con las mesas de tipoJuego
     */
    public List<Servicio> devolverServiciosTipo (TipoServicio tipoServicio);
}