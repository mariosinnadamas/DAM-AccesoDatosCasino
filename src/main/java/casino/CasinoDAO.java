package casino;

import exceptions.ClientAlreadyExistsException;
import exceptions.ClientNotFoundException;
import exceptions.LogNotFoundException;
import exceptions.ServiceNotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface CasinoDAO {
    //Metodos CRUD

    /**
     * Añade un objeto cliente al almacén
     * @param cliente objeto que recibe como parámetro para agregar al almacén
     * @throws IllegalArgumentException
     * @throws ClientAlreadyExistsException
     * @throws IOException
     */
    public void addCliente(Cliente cliente) throws IllegalArgumentException, ClientAlreadyExistsException, IOException;

    /**
     * Añade un objeto Servicio al almacén
     * @param servicio objeto que recibe como parámetro para agregar al almacén
     */
    public void addServicio(Servicio servicio) throws IllegalArgumentException, IOException;

    /**
     * Añade un objeto Log al almacén
     * @param log objeto que recibe como parámetro para agregar al almacén
     */
    public void addLog(Log log) throws IllegalArgumentException, IOException;

    /**
     * Consulta toda la información del Servicio
     * @param codigo único del servicio del que se quiere consultar la información
     * @return String con la información del servicio solicitado
     */
    public String consultaServicio(String codigo) throws IOException;

    /**
     * Consulta todos los servicios almacenados
     * @return List con todos los servicios que tenemos
     */
    public List<Servicio> leerListaServicios() throws IOException;

    /**
     * Consulta la información de un cliente
     * @param dni String único de un cliente
     * @return String con toda la información del cliente solicitado
     */
    public String consultaCliente(String dni) throws IllegalArgumentException,ClientNotFoundException, IOException;

    /**
     * Consulta todos los clientes registrados
     * @return List con todos los clientes registrados
     * @throws IOException por posibles errores de entrada al leer el fichero
     */
    public List<Cliente> leerListaClientes() throws IOException;

    /**
     * Consulta un Log específico
     * @param codigoServicio del servicio
     * @param dni del cliente
     * @param fecha del log
     * @return String con la informacion
     */
    public String consultaLog(String codigoServicio, String dni, LocalDate fecha) throws IllegalArgumentException, LogNotFoundException, IOException;

    /**
     * Consulta todos los Log almacenados
     * @return List con todos los Log
     */
    public List<Log> leerListaLog() throws IOException;

    /**
     * Actualiza la información de un Servicio
     * @param codigo único de un Servicio
     * @param servicioActualizado objeto servicio por el que se actualiza
     * @return True si se ha podido actualizar los datos de la mesa
     */
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) throws IllegalArgumentException, ServiceNotFoundException, IOException ;

    /**
     * Actualiza la información de un Cliente
     * @param dni único que permite identificar al Cliente
     * @param clienteActualizado objeto del cliente actualizado
     * @return True si se ha podido actualizar los datos del Cliente
     */
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) throws IllegalArgumentException, ClientNotFoundException, IOException;

    /**
     * Borra una mesa
     * @param servicio recibe el objeto Mesa
     * @return True si se ha podido eliminar el objeto
     */
    public boolean borrarServicio(Servicio servicio) throws IllegalArgumentException,ServiceNotFoundException,IOException;

    /**
     * Borra un cliente
     * @param cliente, recibe el objeto Cliente
     * @return True si se ha podido eliminar la mesa
     */
    public boolean borrarCliente(Cliente cliente) throws IllegalArgumentException, ClientNotFoundException, IOException;



    //Metodos NO CRUD BÁSICOS
    /**
     * Devuelve el valor del dinero invertido en comida/bebida de un cliente
     * @param dni
     * @return variable gastado en alimentos por cliente
     */
    public double gananciasAlimentos(String dni) throws IllegalArgumentException, IOException;

    /**
     * Devuelve el valor del dinero invertido por un cliente en el casino
     * @param dni a devolver lo invertido en el casino
     * @return Lo gastado en el casino por cliente
     */
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha) throws IllegalArgumentException, LogNotFoundException, IOException;

    /**
     * Devuelve la cantidad de veces que un cliente ha jugado en una mesa
     * @param dni
     * @param codigo
     * @return La cantidad de veces que ha jugado cliente en una mesa
     */
    public int vecesClienteJuegaMesa(String dni, String codigo) throws IllegalArgumentException, IOException;


    /**
     * Devuelve el total ganado en mesas
     * @return double con el total ganado en mesas
     */
    public double ganadoMesas() throws IOException;

    /**
     * Devuelve el total de lo ganado en establecimientos
     * @return double con el total ganado en establecimientos
     */
    public double ganadoEstablecimientos() throws IOException;

    /**
     * Devuelve una lista con las mesas que sean de tipoJuego
     * @param tipoServicio a buscar en el archivo
     * @return Lista con las mesas de tipoJuego
     */
    public List<Servicio> devolverServiciosTipo (TipoServicio tipoServicio) throws IllegalArgumentException, IOException;
}