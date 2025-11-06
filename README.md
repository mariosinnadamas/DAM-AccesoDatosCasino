# Diseño de Clases
1. CasinoMain: Clase que gestionara los clientes, los establecimientos y las mesas de juego
2. Cliente: Clase que registra a las personas que entran en el casino, atributo clave: DNI 
3. Mesa: Clase que registra las mesas donde juegan los clientes
4. Establecimiento: Clase padre abstracta de la que heredarán las clases Bar, Restaurante, etc
   1. Bar: Clase hija, tendrá los atributos del padre y sus atributos disntintos (carta? productos? precios?)
   2. Restaurante
## Atributos de las clases
### Cliente
Atributos:
- String: dni
- String: nombre
- String: apellidos
- double: ganancias
- double: gastos
### Mesa
Atributos:
- String: codigo
- int: numeroMesa
- int: numeroJugadores
- double: ganancias
- double: perdidas
- double: balance
- TipoJuego: tipoJuego
### Establecimiento
Atributos:
- String: Codigo
- double: ganancias
#### Bar
- double: gananciasBebidas
#### Restaurante
- double: gananciasBebidas
- double: gananciasComida
# Metodos Crud
- Creación de usuario/mesa/establecimiento
- Consulta de usuario/mesa/establecimiento
- Actualizar de usuario/mesa/establecimiento
- Borrar de usuario/mesa/establecimiento
# Metodos no Crud
- dineroInvertidoAlimentos(Cliente): Devuelve el dinero invertido en comida/bebida
- dineroInvertido(Cliente): Devuelve el dinero invertido por un cliente en comida/bebida/mesa
- vecesJugadasMesa(Cliente, Mesa): Cantidad de veces que ha jugado un cliente en una mesa
- ganadoMesas(): Devuelve el total de lo ganado en mesas
- ganadoEstablecimiento(): Devuelve lo ganado en establecimiento
- mayorCliente(): Devuelve el Cliente que más ha gastado
- mesaPorJuego(tipoJuego): Devuelve una lista con las mesas que sean tipoJuego
# Implementación obligatoria
Debemos usar un archivo JSON (Podemos usar JSON-B o JSON-P) y XML
# TO-DO
## InterfazDAO
- [ ] MetodosCRUD Establecimientos
  - [ ] Bar
  - [ ] Restaurante
- [ ] Cambiar "usuario" por "cliente" en CasinoDAO
- [ ] Agregar métodos List<> para Cliente, Mesa, Establecimiento
- [ ] 