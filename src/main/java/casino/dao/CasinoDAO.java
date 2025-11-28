package casino.dao;

import casino.model.Cliente;
import casino.model.Log;
import casino.model.Servicio;
import casino.model.TipoServicio;
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
     * @throws IllegalArgumentException Si el cliente o alguno de sus parámetros es nulo o está mal
     * @throws ClientAlreadyExistsException Si el cliente ya existe
     * @throws IOException Si ha habido algun problema de E/S en el fichero Cliente
     */
    public void addCliente(Cliente cliente) throws IllegalArgumentException, ClientAlreadyExistsException, IOException;

    /**
     * Añade un objeto Servicio al almacén
     * @param servicio objeto que recibe como parámetro para agregar al almacén
     * @throws IllegalArgumentException si el servicio o alguno de sus parámetros es nulo o está mal
     * @throws IOException Si ha habido algun problema de E/S en el fichero Servicio
     */
    public void addServicio(Servicio servicio) throws IllegalArgumentException, IOException;

    /**
     * Añade un objeto Log al almacén
     * @param log objeto que recibe como parámetro para agregar al almacén
     * @throws IllegalArgumentException si el log o alguno de sus parámetros es nulo o está mal
     * @throws IOException Si ha habido algun problema de E/S en el fichero de log
     */
    public void addLog(Log log) throws IllegalArgumentException, IOException;

    /**
     * Consulta toda la información del Servicio
     * @param codigo único del servicio del que se quiere consultar la información
     * @return String con la información del servicio solicitado
     * @throws IllegalArgumentException si el código que se pasa por parámetro es nulo o está mal
     * @throws IOException Si ha habido algún problema de E/S en la consulta del Servicio
     */
    public String consultaServicio(String codigo) throws IllegalArgumentException ,IOException;

    /**
     * Consulta todos los servicios almacenados
     * @return List con todos los servicios que tenemos
     * @throws IOException Si ha habido algún problema de lectura en el fichero
     */
    public List<Servicio> leerListaServicios() throws IOException;

    /**
     * Consulta la información de un cliente
     * @param dni String único de un cliente
     * @return String con toda la información del cliente solicitado
     * @throws IllegalArgumentException Si el dni que se pasa por parámetro es nulo o está mal
     * @throws ClientNotFoundException Si no se ha encontrado el cliente
     * @throws IOException Si ha habido algún problema de E/S del fichero
     */
    public String consultaCliente(String dni) throws IllegalArgumentException,ClientNotFoundException, IOException;

    /**
     * Consulta todos los clientes registrados
     * @return List con todos los clientes registrados
     * @throws IOException Si ha habido algún error al leer el fichero clientes
     */
    public List<Cliente> leerListaClientes() throws IOException;

    /**
     * Consulta un Log específico
     * @param codigoServicio del servicio a buscar
     * @param dni del cliente a buscar en el log
     * @param fecha del log
     * @return String con la informacion
     * @throws IllegalArgumentException Si el código del servicio, el dni o la fecha pasadas por parámetro son nulo o están mal
     * @throws LogNotFoundException Si no se ha encontrado el Log querido
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public String consultaLog(String codigoServicio, String dni, LocalDate fecha) throws IllegalArgumentException, LogNotFoundException, IOException;

    /**
     * Consulta todos los Log almacenados
     * @return List con todos los Log
     * @throws IOException Si ha habido algún error con la E/S al leer el documento Log
     */
    public List<Log> leerListaLog() throws IOException;

    /**
     * Actualiza la información de un Servicio
     * @param codigo único de un Servicio
     * @param servicioActualizado objeto servicio por el que se actualiza
     * @return True si se ha podido actualizar los datos de la mesa
     * @throws IllegalArgumentException Si el código, el servicio o algún parámetro del servicio son nulos o están mal
     * @throws ServiceNotFoundException Si no se ha podido encontrar el servicio querido
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public boolean actualizarServicio(String codigo, Servicio servicioActualizado) throws IllegalArgumentException, ServiceNotFoundException, IOException ;

    /**
     * Actualiza la información de un Cliente
     * @param dni único que permite identificar al Cliente
     * @param clienteActualizado objeto del cliente actualizado
     * @return True si se ha podido actualizar los datos del Cliente
     * @throws IllegalArgumentException Si el dni, el cliente actualizado o alguno de sus parámetros son nulos o están mal
     * @throws ClientNotFoundException Si no se ha podido encontrar el cliente a actualizar
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public boolean actualizarCliente(String dni, Cliente clienteActualizado) throws IllegalArgumentException, ClientNotFoundException, IOException;

    /**
     * Borra una mesa
     * @param servicio recibe el objeto Mesa
     * @return True si se ha podido eliminar el objeto
     * @throws IllegalArgumentException Si el objeto servicio es nulo o están mal alguno de sus parámetros
     * @throws ServiceNotFoundException Si no se ha podido encontrar el servicio a eliminar
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public boolean borrarServicio(Servicio servicio) throws IllegalArgumentException,ServiceNotFoundException,IOException;

    /**
     * Borra un cliente
     * @param cliente, recibe el objeto Cliente
     * @return True si se ha podido eliminar la mesa
     * @throws IllegalArgumentException Si el objeto cliente es nulo o están mal alguno de sus parámetros
     * @throws ClientNotFoundException Si no se ha podido encontrar el cliente a borrar
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public boolean borrarCliente(Cliente cliente) throws IllegalArgumentException, ClientNotFoundException, IOException;



    //Metodos NO CRUD BÁSICOS
    /**
     * Devuelve el valor del dinero invertido en comida/bebida de un cliente
     * @param dni Del cliente del que queremos saber la información
     * @return Variable con lo gastado en alimentos por el cliente
     * @throws IllegalArgumentException Si el dni pasado es nulo o está mal
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public double gananciasAlimentos(String dni) throws IllegalArgumentException, IOException;

    /**
     * Devuelve el valor del dinero invertido por un cliente en el casino
     * @param dni del cliente del que se quiere consultar la información
     * @param fecha del dia que se quiera consultar la información
     * @return Lo gastado en el casino por cliente
     * @throws IllegalArgumentException Si el dni o la fecha son nulos o están mal
     * @throws LogNotFoundException Si no se ha podido encontrar el log querido
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public double dineroInvertidoClienteEnDia(String dni, LocalDate fecha) throws IllegalArgumentException, LogNotFoundException, IOException;

    /**
     * Devuelve la cantidad de veces que un cliente ha jugado en una mesa
     * @param dni Del cliente del que se quiere consultar la información
     * @param codigo De la mesa en la que ha jugado el cliente
     * @return La cantidad de veces que ha jugado cliente en una mesa
     * @throws IllegalArgumentException Si el dni o el código son nulos o están mal
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public int vecesClienteJuegaMesa(String dni, String codigo) throws IllegalArgumentException, IOException;

    /**
     * Devuelve el total ganado en mesas para el casino
     * @return double con el total ganado en mesas
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public double ganadoMesas() throws IOException;

    /**
     * Devuelve el total de lo ganado en establecimientos
     * @return Variable con el total ganado en establecimientos
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public double ganadoEstablecimientos() throws IOException;

    /**
     * Devuelve una lista con las mesas que sean de tipoJuego
     * @param tipoServicio a buscar en el archivo
     * @return Lista con las mesas de tipoJuego
     * @throws IllegalArgumentException Si el tipoServicio es nulo o está mal
     * @throws IOException Si ha habido algún error de E/S en el fichero
     */
    public List<Servicio> devolverServiciosTipo (TipoServicio tipoServicio) throws IllegalArgumentException, IOException;
}