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
