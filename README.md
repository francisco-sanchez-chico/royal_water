# royal_water
Para ejecutar el programa habrá que crear una base de datos llamada "bd_royal". Se esta usando MySQL. O si se desea cambiar el nombre de la base de datos, solo hay que cambiar la configuracion del spring.datasource.url del application.properties del proyecto.

🧠 FUNCIONALIDADES IMPLEMENTADAS (100% funcionales, API REST significa que solo se puede visualizar con postman y que no tiene una vista funcional)
Funcionalidad	Estado	Ubicación / Enlace
Inicio de sesión con Thymeleaf	                                      ✔️	/login
Redirección al dashboard según rol	                                  ✔️	/dashboard
Control de acceso por rol (ADMINISTRADOR, EMPLEADO)	                  ✔️	Seguridad con Spring Security
Validaciones de formularios (@NotNull, etc.)	                        ✔️	Todas las entidades
CRUD de usuarios (API REST)	                                          ✔️	Solo ADMINISTRADOR
CRUD de productos	                                                    ✔️	API REST y vista en /productos
CRUD de categorías	                                                  ✔️	API REST
Registro de movimientos (entrada/salida)	                            ✔️	API REST
Registro de defectos	                                                ✔️	API REST
Reportes de inventario, movimientos, defectos, ventas (PDF y Excel)	  ✔️	Descargables desde Postman o navegador
Filtro por fecha en reportes de movimientos y defectos	              ✔️	Implementado
Reporte de ventas (salidas con motivo VENTA)	                        ✔️	PDF y Excel
Alerta de stock bajo	                                                ✔️	API REST
Vista funcional con Thymeleaf para productos	                        ✔️	/productos

🌐 URLs DISPONIBLES
🔐 AUTENTICACIÓN
URL	Método	Descripción
/login	GET / POST	Página de login (Thymeleaf)
/logout	POST	Cerrar sesión

🏠 DASHBOARD
URL	Método	Descripción
/dashboard	GET	Vista principal con menú por rol

📦 PRODUCTOS
URL	Método	Descripción
/productos	GET / POST	Vista funcional para listar y registrar productos
/api/productos	GET / POST	API REST para productos
/api/productos/reporte/pdf	GET	Reporte PDF de inventario
/api/productos/reporte/excel	GET	Reporte Excel de inventario
/api/productos/alerta-stock	GET	Productos con stock bajo

📁 CATEGORÍAS (solo API)
URL	Método	Descripción
/api/categorias	GET / POST	API REST para categorías

🔁 MOVIMIENTOS
URL	Método	Descripción
/api/movimientos	GET / POST	Registrar entrada/salida
/api/movimientos/producto/{idProducto}	GET	Movimientos de un producto
/api/movimientos/filtrar?desde=...&hasta=...	GET	Filtrar movimientos por fecha
/api/movimientos/reporte/pdf	GET	Reporte completo PDF
/api/movimientos/reporte/excel	GET	Reporte completo Excel
/api/movimientos/reporte/pdf?desde=...	GET	Reporte filtrado PDF
/api/movimientos/reporte/ventas/pdf	GET	Reporte de ventas PDF
/api/movimientos/reporte/ventas/excel	GET	Reporte de ventas Excel

❌ DEFECTOS
URL	Método	Descripción
/api/defectos	GET / POST	Registrar y listar defectos
/api/defectos/producto/{id}	GET	Defectos por producto
/api/defectos/reporte/pdf?desde=...&hasta=...	GET	Reporte PDF filtrado de defectos

👤 USUARIOS (API – solo ADMINISTRADOR)
URL	Método	Descripción
/api/usuarios	GET / POST	Crear y listar usuarios
/api/usuarios/{id}	GET / DELETE	Buscar / eliminar usuario
/api/usuarios/buscar?username=...	GET	Buscar por username

📌 ¿QUÉ FALTA POR HACER?
Tarea	Estado sugerido
Vista Thymeleaf para registrar movimientos	                      🔜
Vista para listar defectos y reportarlos vía formulario	          🔜
Vista para reportes (botones que llamen a las URLs PDF/Excel)	    🔜
Vista para alertas de stock bajo	                                🔜
Editar y eliminar productos desde la vista (opcional)	            🔜
Registro y vista de usuarios en frontend (opcional)	              🔜
Estilizar el frontend con CSS o framework (opcional)	            🔜
