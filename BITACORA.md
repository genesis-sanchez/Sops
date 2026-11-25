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

## 16 de noviembre de 2026 - editar y eliminar mascotas

**avance:** agregue los botones para editar y eliminar desde la ficha.

**dificultad:** cambiar los datos sin crear otra mascota.

**solucion:** use el mismo id para actualizar. para eliminar agregue una confirmacion y el regreso a la lista.

## 16 de noviembre de 2026 - preparar vacunas

**avance:** converti Vacuna en una entidad, cree VacunaDao y la agregue a SopsDatabase.

**dificultad:** agregar la tabla sin borrar las mascotas guardadas.

**solucion:** actualice la version de la base para crear la tabla de vacunas. falta relacionarla con mascotas y conectar las pantallas.

## 16 de noviembre de 2026 - guardar vacunas por mascota

**avance:** relacione las vacunas con mascotas y conecte su formulario y lista con room.

**dificultad:** mostrar solo las vacunas de la mascota elegida.

**solucion:** guarde el id de la mascota en cada vacuna y consulte la lista usando ese id.

## 16 de noviembre de 2026 - detalle de vacuna

**avance:** agregue la pantalla con los datos de la vacuna.

**dificultad:** abrir la vacuna elegida desde la lista.

**solucion:** pase su id con un intent y consulte sus datos en room.

## 16 de noviembre de 2026 - editar vacuna

**avance:** agregue el boton editar vacuna y cargue sus datos en el formulario.

**dificultad:** guardar los cambios sin crear otra vacuna.

**solucion:** use el mismo id para actualizar y al volver se muestran los datos nuevos.

## 17 de noviembre de 2026 - eliminar vacuna

**avance:** agregue el boton eliminar vacuna en su detalle.

**dificultad:** borrar solo la vacuna elegida y actualizar la lista.

**solucion:** agregue una confirmacion y use el dao para eliminar el registro. al cancelar no se borra nada.


## 17 de noviembre de 2026 - capturas y video

**avance:** tome capturas y grabe el recorrido de mascotas y vacunas.

**dificultad:** mostrar los formularios y los cambios guardados dentro del tiempo pedido.

**solucion:** recorri el registro, la consulta, la edicion y la eliminacion. agregue una breve explicacion del codigo al final del video.

## 17 de noviembre de 2026 - base de datos de visitas

**avance:** prepare la entidad de visitas veterinarias, su dao y la tabla en room.

**dificultad:** agregar la tabla sin perder las mascotas y vacunas guardadas.

**solucion:** agregue la actualizacion de la base de datos a la version 4. el formulario todavia no guarda visitas.


## 17 de noviembre de 2026 - guardar y consultar visitas

**avance:** conecte el formulario de visitas y el historial con room.

**dificultad:** mostrar solo las visitas de la mascota elegida al regresar del formulario.

**solucion:** guarde el id de la mascota y actualice el historial al volver. la proxima revision es opcional.


## 23 de noviembre de 2026 - detalle de visita

**avance:** agregue el detalle de las visitas desde el historial de cada mascota.

**dificultad:** abrir los datos de la visita seleccionada.

**solucion:** pase su id con un intent y consulte la informacion en room.


## 23 de noviembre de 2026 - editar visita

**avance:** agregue el boton editar y cargue los datos de la visita en el formulario.

**dificultad:** guardar los cambios sin duplicar la visita.

**solucion:** use el mismo id para actualizar y al volver se muestran los datos nuevos.


## 23 de noviembre de 2026 - eliminar visita

**avance:** agregue el boton eliminar visita desde su detalle.

**dificultad:** borrar solo la visita elegida y actualizar el historial.

**solucion:** agregue una confirmacion y elimine usando su id. al cancelar se conserva la visita.

## 24 de noviembre de 2026 - base de datos de alimentacion

**avance:** prepare la entidad de alimentacion, su dao y la tabla en room.

**dificultad:** agregar la tabla sin perder los registros anteriores.

**solucion:** actualice la base a la version 5 y relacione cada registro con su mascota. el formulario todavia no guarda datos.


## 24 de noviembre de 2026 - guardar y consultar alimentacion

**avance:** conecte el formulario y la lista de alimentacion con room.

**dificultad:** guardar los registros de cada mascota y mostrarlos al volver.

**solucion:** use el id de la mascota y actualice la lista al regresar. tambien agregue el tipo de alimento y las veces al dia.


## 24 de noviembre de 2026 - detalle de alimentacion

**avance:** agregue el detalle de alimentacion al tocar una tarjeta.

**dificultad:** abrir el registro elegido desde la lista.

**solucion:** pase su id con un intent y consulte sus datos en room.


## 24 de noviembre de 2026 - editar y eliminar alimentacion

**avance:** agregue las opciones de editar y eliminar desde el detalle.

**dificultad:** actualizar el mismo registro y borrar solo el elegido.

**solucion:** use su id para guardar los cambios y agregue una confirmacion antes de eliminar.

## 24 de noviembre de 2026 - total de mascotas en inicio

**avance:** agregue el total de mascotas registradas en el inicio.

**dificultad:** mantener el numero actualizado al regresar.

**solucion:** consulte el total en room cada vez que se abre el inicio.


## 25 de noviembre de 2026 - panel de inicio

**avance:** agregue vacunas y revisiones pendientes al inicio, junto al total de mascotas.

**dificultad:** ordenar las fechas y abrir el registro correcto.

**solucion:** consulte los datos guardados y ordene los cuidados por fecha. cada tarjeta abre su detalle y el nombre abre la ficha de la mascota.


## 25 de noviembre de 2026 - ajuste del inicio

**avance:** quite los botones de ver mascotas y registrar del inicio.

**dificultad:** los accesos ya estaban en la barra superior.

**solucion:** deje la tarjeta con el total de mascotas y los accesos en los iconos.


## 25 de noviembre de 2026 - foto de mascota

**avance:** agregue la opcion de elegir, cambiar y quitar la foto al registrar o editar una mascota.

**dificultad:** conservar el acceso a la imagen despues de cerrar la app.

**solucion:** guarde su direccion y conserve el permiso de lectura. la ficha muestra la foto y la base pasa a la version 6.


## 25 de noviembre de 2026 - base de datos de medicamentos

**avance:** prepare la entidad de medicamentos, su dao y la tabla en room.

**dificultad:** relacionar cada medicamento con su mascota y una visita opcional.

**solucion:** agregue las relaciones y actualice la base a la version 7. todavia falta conectar las pantallas.


## 25 de noviembre de 2026 - guardar y consultar medicamentos

**avance:** agregue el formulario y la lista de medicamentos desde la ficha de la mascota.

**dificultad:** guardar cada tratamiento en la mascota correcta y revisar las fechas.

**solucion:** use su id para guardar y consultar. valide las horas y que la fecha de fin no sea anterior al inicio.
