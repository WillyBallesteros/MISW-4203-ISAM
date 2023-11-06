# MISW-4203-ISAM - Aplicación móvil de la plataforma de Vynilos - Equipo 16

## Requisitos

### Backend
#### Docker:
- La última versión estable de Docker instalada y ejecutándose.

#### Docker Compose:
- La última versión de Docker Compose.

#### Cliente postgreSQL:
- La última versión del cliente postgreSQL.

#### Sistema Operativo:
- **Windows 10 Pro/Enterprise/Education**: Hyper-V y Containers deben estar habilitados.
- **macOS**: La última versión estable, con soporte para virtualización.
- **Linux**: Cualquier distribución moderna que soporte Docker.

#### Memoria RAM:
- Mínimo 4 GB de RAM (recomendado).

#### CPU:
- Procesador con soporte para virtualización de hardware.

#### Espacio en Disco:
- Suficiente espacio para alojar las imágenes de Docker y los contenedores.

### Aplicación móvil
#### Sistema Operativo:
- **Windows**: Windows 7/8/10 (64-bit).
- **macOS**: macOS 10.14 (Mojave) o superior.
- **Linux**: Distribución que soporte Gnome, KDE o Unity DE.

#### Memoria RAM:
- Mínimo de 8 GB de RAM, recomendado más para emuladores y mejor rendimiento.

#### Espacio en Disco:
- Mínimo de 2 GB de espacio disponible para instalación, recomendado 4 GB o más.

#### Android Studio:
- Última versión de Android Studio.

#### JDK (Java Development Kit):
- Incluido con Android Studio, no es necesario instalar Java por separado.

#### Android SDK:
- Necesario para el desarrollo, se instala a través de Android Studio.

#### Android Emulator:
- Opcional si usas dispositivo físico, necesario para probar en diferentes versiones de Android.

#### Android Virtual Device (AVD):
- Configuración de un dispositivo virtual para pruebas.

#### Dependencias del Proyecto:
- Gradle y dependencias de Maven necesarias.

#### Conectividad de Internet:
- Necesaria para descargar dependencias y herramientas.

#### Hardware Aceleration:
- CPU con soporte para Intel HAXM (en Windows y macOS) o KVM (en Linux).

#### Monitor:
- Resolución mínima de 1280 x 800.

#### Herramientas de Comando de Línea (CLI):
- Requeridas para operaciones de depuración

## Compilación

### Backend
- Ingresar a la carpeta *backend*
- Ejecutar los comandos:
``` 
docker-compose build
docker-compose up -d
``` 
- Ejecutar script con datos iniciales:
``` 
psql -h localhost -p 5432 -U postgres -d vinyls -a -f sql/data.sql
``` 

### Aplicación móvil
- Abrir la carpeta AndroidApp con Android Studio.
- Establecer la IP del servidor de backend en la variable *BASE_URL* de la clase *NetworkServiceAdapter*
- Ejecutar File -> Sync Project with Grale File
- Agregar configuraciones para ejecución y pruebas
- Ejecutar la aplicación o los escenarios de prueba presionando F5

## Ejecución directa en móviles físicos
- Descargar el archivo APK en el móvil
- Habilitar la opción de instalar aplicaciónes fuera de App Store
- Instalar el APK
- Ejecutar