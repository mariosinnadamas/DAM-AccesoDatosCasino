# Proyecto Casino

## Paquete Casino

### app
Paquete principal donde se encuentran los main del json/xml y de la base de datos
- CanelaTest* : Main de la DaoDB
- Main: Main del json/xml

### dao
Paquete con el contenido de la DAO, excluyendo la DAO de la base de datos que va aparte
- CasinoDAO : Interfaz DAO usada por el json/xml/bd

#### helper
- CasinoGestorArchivos: Incluye métodos para generar copias de seguridad tanto del json como del xml

#### impl
Implementaciones de la DAO con ficheros
- CasinoDAOFileJSON: Implementación de la DAO para el formato JSON
- CasinoDAOFileXML:

### db
Paquete con el contenido de la DAO respecto a bases de datos
- CasinoDAODB: Implementación de la DAO para base de datos PostgreSQL
- ConexionDB: Clase que permite la conexión a una BD 

### model
Clases de negocio
- Cliente: Contiene información de los clientes
- Log: Contiene información de los logs(registros)
- Servicio: Contiene información de los servicios
- TipoConcepto: Enum. Declara el motivo por el que se ha hecho un log en un servicio
- TipoServicio: Enum. Declara el tipo de servicio

### util
Paquete que contiene clases que no son de negocio pero facilitan los tests y otras pruebas
- DummyGenerator: Permite popular arrays con datos y por extensión bases de datos

## exceptions
Alberga todas las excepciones del proyecto