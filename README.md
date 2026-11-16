# Sops 🐾

Sops es una aplicación para organizar el cuidado de las mascotas. Reúne sus datos, vacunas, visitas veterinarias y alimentación en un solo lugar.

Este proyecto fue desarrollado por **Génesis Solange Sánchez Paredes**, para el curso **Desarrollo de Aplicaciones Móviles**, a cargo del docente **Ing. Omar Sulca Correa**, en la Universidad de Huánuco.

Este repositorio reúne el proyecto, las capturas de sus pantallas y la bitácora del desarrollo.

## Funciones desarrolladas

- Ver la lista de mascotas y abrir la ficha de cada una.
- Registrar y guardar una mascota en el celular.
- Editar sus datos o eliminar su ficha con confirmación.
- Consultar sus vacunas y llenar el formulario de una nueva vacuna.
- Revisar el historial médico y completar los datos de una visita.
- Consultar y registrar los datos de alimentación.
- Leer consejos de cuidado para perros y gatos.

## Funcionamiento

Desde **Inicio → Ver mis mascotas** se puede seleccionar una mascota y consultar su ficha. Allí se encuentran los accesos a vacunas, historial médico y alimentación. Cada sección cuenta con un botón para abrir su formulario.

En la ficha de una mascota guardada, **editar mascota** abre el formulario con sus datos. **Eliminar mascota** pide confirmar antes de borrar el registro y regresar a la lista.

La huella de la barra abre la lista de mascotas y el signo **+** abre su registro. En los tres puntos están Inicio, Consejos y Acerca de Sops. La flecha y el botón Atrás permiten regresar a la pantalla anterior.

Los formularios indican si falta completar un campo obligatorio. Al enviar los datos, se muestra una ficha o un resumen de lo ingresado.

## Estado del proyecto

El registro de mascotas ya guarda los datos en el celular y abre su ficha. Mis mascotas muestra los registros guardados y se actualiza al volver a la pantalla. También permite editar y eliminar mascotas. Los otros formularios aún no guardan datos y los recordatorios no envían avisos.

## Antes y ahora

### Primera versión

Se crearon las pantallas, el menú y los formularios. La lista mostraba cinco mascotas de ejemplo y permitía abrir sus fichas. Al completar un formulario se mostraba un resumen, pero los datos no se guardaban.

### Avances con Room

Primero se preparó la base de datos de mascotas. Después se conectó el formulario para guardar los registros y abrir su ficha. Luego se conectó Mis mascotas para consultar los datos guardados. El siguiente avance agregó la edición y eliminación desde la ficha.

| Parte de la app | Antes | Ahora |
| --- | --- | --- |
| Registro de mascotas | Mostraba una ficha sin guardar los datos. | Guarda la mascota en el celular y abre su ficha. |
| Mis mascotas | Mostraba las cinco mascotas de ejemplo. | Muestra las mascotas guardadas y se actualiza al regresar. |
| Editar mascota | No había una opción para cambiar sus datos. | Abre el formulario con los datos actuales y guarda los cambios en el mismo registro. |
| Eliminar mascota | No se podían borrar mascotas desde la ficha. | Pide confirmación, elimina el registro y actualiza la lista. |
| Al cerrar la app | Los datos ingresados se perdían. | Las mascotas registradas permanecen guardadas. |
| Vacunas, visitas y alimentación | Los formularios mostraban un resumen. | Mantienen ese funcionamiento; falta conectar su guardado. |

Esta sección se irá ampliando con los siguientes avances.

## Cómo abrir el proyecto

Se desarrolló con **Android Studio Quail 3 | 2026.1.3**.

1. Descargar el proyecto y abrir su carpeta en Android Studio.
2. Esperar a que termine de cargar e instalar los componentes que solicite.
3. Seleccionar un emulador o conectar un celular con Android 7.0 o una versión posterior.
4. Pulsar el botón **▶** para ejecutar la aplicación.

## Avances del proyecto

La [bitácora](BITACORA.md) reúne los avances y las dificultades encontradas durante el desarrollo. Las [capturas](evidencias/capturas/) muestran las pantallas de la aplicación.

## Pantallas

Estas capturas corresponden a la primera versión de Sops. Se conservan para mostrar cómo empezó el proyecto; las capturas de los nuevos avances se agregarán por separado.

### Primera versión

| Inicio | Mis mascotas | Ficha de mascota |
| --- | --- | --- |
| <img src="evidencias/capturas/01_inicio.png" width="220" alt="Inicio de Sops"> | <img src="evidencias/capturas/02_lista_mascotas.png" width="220" alt="Lista con las cinco mascotas"> | <img src="evidencias/capturas/03_detalle_mascota.png" width="220" alt="Ficha de Firulais"> |

| Registrar mascota | Vacunas | Registrar vacuna |
| --- | --- | --- |
| <img src="evidencias/capturas/04_registro_mascota.png" width="220" alt="Formulario de mascota"> | <img src="evidencias/capturas/05_vacunas.png" width="220" alt="Lista de vacunas"> | <img src="evidencias/capturas/06_registro_vacuna.png" width="220" alt="Formulario de vacuna"> |

| Historial médico | Registrar visita | Alimentación |
| --- | --- | --- |
| <img src="evidencias/capturas/07_historial_medico.png" width="220" alt="Historial médico de Firulais"> | <img src="evidencias/capturas/08_registro_visita.png" width="220" alt="Formulario de visita veterinaria"> | <img src="evidencias/capturas/09_alimentacion.png" width="220" alt="Datos de alimentación"> |

| Registrar alimentación | Consejos | Acerca de Sops |
| --- | --- | --- |
| <img src="evidencias/capturas/10_registro_alimentacion.png" width="220" alt="Formulario de alimentación"> | <img src="evidencias/capturas/11_consejos.png" width="220" alt="Consejos para perros y gatos"> | <img src="evidencias/capturas/12_acerca_de_sops.png" width="220" alt="Acerca de Sops"> |

### Menú

<img src="evidencias/capturas/13_menu.png" width="260" alt="Menú de opciones desplegado">

### Continuación de las pantallas

| Registro de mascota | Registro de vacuna | Acerca de Sops |
| --- | --- | --- |
| <img src="evidencias/capturas/04b_registro_mascota_continuacion.png" width="220" alt="Parte inferior del registro de mascota"> | <img src="evidencias/capturas/06b_registro_vacuna_continuacion.png" width="220" alt="Parte inferior del registro de vacuna"> | <img src="evidencias/capturas/12b_acerca_de_sops_continuacion.png" width="220" alt="Autora y versión de Sops"> |
