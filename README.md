# Para los comentarios  -ToDo:
- Prioridad
  - !1 MUY IMPORTANTE
  - !2 Menos prioridad
  - !3 Sin prioridad 

- Numero del ToDo
- Explicar el motivo del ToDo

## Ejemplos
- //ToDo\[!1#1]: "Se me ha roto la Dao"
- //ToDo\[!2#2]: "Implementar metodo darDeAlta"
- //ToDo\[!1#3]: "Se me ha vuelto a romper la Dao"
- //ToDo\[!3#4]: "Añadir JavaDoc"
- //ToDo\[!2#5]: "Me he quedado calvo"

# PRUEBAS
## XML
### ADD Y CREAR CLIENTE
- Crear el objeto completo 
- No creo el objeto con un DNI inválido
- No crea objeto con nombre/apellido vacío
- No puede crear un objeto duplicado
- Si el nombre es compuesto lo mantiene, elimina espacios antes y después
- Si el cliente es nulo salta la excepción

### CONSULTAR
- Con DNI existente funciona
- Con DNI no válido salta la excepción de validarDni
- Con DNI válido pero que no existe salta la excepción de ClienteNotFound

### ADD Y CREAR SERVICIO
- Con valores válidos lo crea
- Con nombre vacío y TipoServicio nulo salta la excepción
- Con Duplicados salta la excepción

### CONSULTAR SERVICIO
- 

### ADD Y CREAR LOG
- Con valores válidos lo crea
- Con valores vacíos o nulos saltan las excepciones
- Con apuestas inferiores a 1 saltan excepciones

### CONSULTAR LOG
- Todo bien


Técnicamente pasamos todas las pruebas y todo está perfecto