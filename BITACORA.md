# Bitácora de Sops

## 13 de noviembre de 2026 - Pantalla de inicio

**Avance:** Se cambió la pantalla inicial por una bienvenida con secciones de mascotas, vacunas y cuidados. Se definieron los colores y estilos que se usarán en las siguientes pantallas. La actividad principal se movió a `controller` y se preparó el paquete `view` para los adaptadores.

**Dificultad:** La revisión detectó que una opción de color de la barra de navegación requería Android 8.1, pero la app también admite versiones anteriores.

**Solución:** Se retiró esa opción del tema para mantener la compatibilidad. Las tarjetas de inicio son informativas en este avance; la navegación se agregará junto con las siguientes pantallas.

## 13 de noviembre de 2026 - Lista y ficha de mascotas

**Avance:** Se agregó una lista con las cinco mascotas del modelo, sus tarjetas y una ficha con nombre, raza, especie, edad, sexo y esterilización. Desde Inicio ya se puede abrir la lista y elegir una mascota.

**Dificultad:** La ficha necesita recibir la mascota seleccionada sin repetir los datos de ejemplo dentro de la pantalla o del adaptador.

**Solución:** La lista se obtiene de `DataSource` y la mascota se envía con `putExtra`. La ficha recibe el objeto y comprueba que exista antes de mostrarlo. También se creó una barra compartida para volver a la pantalla anterior.
