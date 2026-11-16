# Bitácora de Sops

## 13 de noviembre de 2026 - Pantalla de inicio

**Avance:** Se cambió la pantalla inicial por una bienvenida con secciones de mascotas, vacunas y cuidados. Se definieron los colores y estilos que se usarán en las siguientes pantallas. La actividad principal se movió a `controller` y se preparó el paquete `view` para los adaptadores.

**Dificultad:** La revisión detectó que una opción de color de la barra de navegación requería Android 8.1, pero la app también admite versiones anteriores.

**Solución:** Se retiró esa opción del tema para mantener la compatibilidad. Las tarjetas de inicio son informativas en este avance; la navegación se agregará junto con las siguientes pantallas.

## 13 de noviembre de 2026 - Lista y ficha de mascotas

**Avance:** Se agregó una lista con las cinco mascotas del modelo, sus tarjetas y una ficha con nombre, raza, especie, edad, sexo y esterilización. Desde Inicio ya se puede abrir la lista y elegir una mascota.

**Dificultad:** La ficha necesita recibir la mascota seleccionada sin repetir los datos de ejemplo dentro de la pantalla o del adaptador.

**Solución:** La lista se obtiene de `DataSource` y la mascota se envía con `putExtra`. La ficha recibe el objeto y comprueba que exista antes de mostrarlo. También se creó una barra compartida para volver a la pantalla anterior.

## 15 de noviembre de 2026 - Registro de mascotas

**Avance:** Se agregó el formulario con nombre, especie, raza, nacimiento, peso, sexo y esterilización. Incluye campos de texto, un selector de especie, calendario, opciones de sexo, casilla y botón de envío. Al completarlo se abre una ficha de confirmación sin guardar los datos.

**Dificultad:** Había que mostrar los datos enviados sin mezclarlos con las cinco mascotas de ejemplo, y permitir completar el formulario aunque el teclado ocupe parte de la pantalla.

**Solución:** Se reutilizó la ficha recibiendo la mascota con `putExtra` y un aviso de demostración. El formulario se puede desplazar y se ajusta al espacio del teclado. Se validan los campos, el peso positivo y las selecciones; el calendario impide elegir fechas futuras.

## 15 de noviembre de 2026 - lista de vacunas

**avance:** agregue la pantalla de vacunas y el boton para abrirla desde la ficha. muestra nombre, dosis, fechas y si esta aplicada o pendiente.

**dificultad:** las mascotas recien ingresadas no deben recibir las vacunas de ejemplo de las otras mascotas.

**solucion:** se pasa tambien el aviso de registro de prueba y para esas mascotas se muestra la lista vacia. los ejemplos siguen en el modelo y el formulario de vacunas queda para el siguiente avance.

## 15 de noviembre de 2026 - formulario de vacunas

**avance:** agregue la vista del formulario y su acceso desde la lista de vacunas con un intent.

**dificultad:** mostrar en el formulario la mascota seleccionada.

**solucion:** pase la mascota con putExtra. el formulario muestra una confirmacion de ejemplo sin guardar datos.

## 15 de noviembre de 2026 - menu

**avance:** agregue la barra verde con iconos blancos de huella y agregar. en los tres puntos deje inicio.

**dificultad:** compartir el menu entre las pantallas.

**solucion:** lo puse en la barra y use intents desde BaseActivity.

## 15 de noviembre de 2026 - consejos

**avance:** agregue la vista de consejos con ejemplos para perros y gatos.

**dificultad:** abrir la pantalla desde el menu.

**solucion:** puse consejos en los tres puntos y lo abri con un intent.

## 15 de noviembre de 2026 - historial medico

**avance:** agregue la vista del historial con dos registros de ejemplo.

**dificultad:** mostrar a que mascota pertenece el historial.

**solucion:** pase la mascota con un intent y mostre su nombre en la pantalla.

## 15 de noviembre de 2026 - registrar visita

**avance:** agregue el formulario de visita desde el historial.

**dificultad:** mostrar la mascota elegida en el formulario.

**solucion:** la pase con putExtra. al enviar se revisan los campos y se muestra un resumen.

## 15 de noviembre de 2026 - alimentacion

**avance:** agregue la vista de alimentacion con alimento, cantidad y horarios.

**dificultad:** abrir los datos desde la ficha de la mascota.

**solucion:** use un intent y mostre los datos del modelo.

## 15 de noviembre de 2026 - registrar alimentacion

**avance:** agregue el formulario de alimentacion.

**dificultad:** abrirlo con la mascota elegida.

**solucion:** use un intent con putExtra y mostre un resumen al enviar.

## 15 de noviembre de 2026 - acerca de Sops

**avance:** agregue la vista con el proposito de la app, autora y version.

**dificultad:** abrirla desde los tres puntos.

**solucion:** agregue la opcion al menu y use un intent.

## 15 de noviembre de 2026 - revision

**avance:** probe el menu, las pantallas y los formularios en el emulador.

**dificultad:** comprobar que los datos lleguen a la mascota elegida.

**solucion:** revise los recorridos y el envio de formularios con seis pruebas. todas pasaron.

## 15 de noviembre de 2026 - capturas

**avance:** guarde 16 capturas de las pantallas y del menu en evidencias/capturas.

**dificultad:** algunas pantallas no entran completas en una imagen.

**solucion:** tome otra captura de la parte inferior para mostrar los campos y datos que faltaban.

## 15 de noviembre de 2026 - preparar room

**avance:** agregue room, converti Mascota en una entidad y cree MascotaDao y SopsDatabase. por ahora la base solo incluye mascotas.

**dificultad:** entender cuando se crea el archivo de la base de datos.

**solucion:** prepare la entidad, el dao y la database. el archivo se creara al abrir la base desde la app. falta conectar el formulario y la lista.

## 16 de noviembre de 2026 - guardar mascota

**avance:** conecte el formulario de mascotas con room.

**dificultad:** abrir la ficha despues de guardar los datos.

**solucion:** use el dao para guardar y pase la mascota a su ficha. falta mostrar los registros guardados en la lista.

## 16 de noviembre de 2026 - listar mascotas

**avance:** conecte mis mascotas con room para mostrar los registros guardados.

**dificultad:** actualizar la lista al regresar a la pantalla.

**solucion:** consulte las mascotas al abrir la lista y mostre un aviso cuando no hay registros.
