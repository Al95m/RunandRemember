# 🏃 Run & Remember

Aplicación Android desarrollada en Kotlin para registrar entrenamientos y
establecer recordatorios mediante alarmas.

## 🧮 Funcionalidades

- Registro de usuarios
- Creación de entrenamientos personalizados
- Visualización de entrenamientos guardados
- Gestión y eliminación de entrenamientos y horarios
- Recordatorios mediante alarmas (BroadcastReceiver)
- Persistencia de datos con SQLite
- Reproducción y control de música desde la pantalla principal

## 🛠️ Tecnologías utilizadas

- Kotlin
- Android Studio
- SQLite
- RecyclerView
- Fragments
- ViewBinding
- Material Design
- BroadcastReceiver
- AlarmManager

## 📁 Estructura del proyecto

```text
app/src/main/java/com/example/runandremember/
│
├── alarmclock/
│   ├── AlarmReceiver.kt
│   └── BootReceiver.kt
│
├── openhelper/
│   └── SQLite_OpenHelper.kt
│
├── recyclerview/
│   ├── ItemHour.kt
│   ├── ItemHourAdapter.kt
│   ├── ItemPlan.kt
│   ├── ItemPlanAdapter.kt
│   ├── ItemTraining.kt
│   └── ItemTrainingAdapter.kt
│
├── ui/
│   └── theme/
│
├── Hour.kt
├── MainActivity.kt
├── MainActivity2Register.kt
├── MainActivity3RegisterTraining.kt
├── MainActivity4ScreenTraining.kt
├── MainActivity5RegisterHour.kt
├── MainActivity6RegisterPlan.kt
├── Planning.kt
├── Sport.kt
└── Usuario.kt
```

### Organización

* **`alarmclock/`** → Gestión de alarmas y reprogramación después del reinicio del dispositivo.
* **`openhelper/`** → Gestión de la base de datos SQLite y operaciones de persistencia.
* **`recyclerview/`** → Activities y adapters encargados de mostrar y gestionar entrenamientos, horarios y planes mediante RecyclerView.
* **`ui/theme/`** → Elementos relacionados con el tema y la interfaz de usuario.
* **Clases principales** → Activities y modelos de datos utilizados por la aplicación.


## 🚀 Estado del proyecto

Proyecto educativo finalizado en su versión **v1.1**.

Esta versión corresponde a una revisión y actualización realizada en 2026
sobre el proyecto original desarrollado en 2022 como proyecto final del ciclo
de Desarrollo de Aplicaciones Multiplataforma (DAM).

## 🔄 Evolución del proyecto

### v1.0 — 2022

Versión original desarrollada como proyecto final del ciclo de
Desarrollo de Aplicaciones Multiplataforma (DAM).

### v1.1 — 2026

Revisión y actualización del proyecto original para mejorar su mantenimiento,
funcionalidad y presentación como proyecto de portfolio.

Principales mejoras realizadas:

- Revisión y limpieza del código.
- Mejora de la nomenclatura de variables y componentes.
- Mejora de la navegación entre pantallas.
- Incorporación y mejora de botones de navegación.
- Revisión de la gestión de entrenamientos y horarios.
- Revisión de la gestión de alarmas y notificaciones.
- Mejoras en la compatibilidad con versiones actuales de Android.
- Mejora de los comentarios y documentación del código.
- Mejoras visuales en diferentes elementos de la aplicación.

## 🎯 Objetivo

Este proyecto fue desarrollado durante mi formación en el grado superior de
Desarrollo de Aplicaciones Multiplataforma (DAM).

Actualmente estoy finalizando el grado superior de Administración de Sistemas
Informáticos en Red (ASIR), ampliando mis conocimientos en sistemas, redes y
administración.

## 📸 Capturas

<img width="1080" height="2400" alt="Screenshot_20260826_190719" src="https://github.com/user-attachments/assets/018a4045-8f6c-4d22-be04-c89810ed52b4" />
<img width="1080" height="2400" alt="Screenshot_20260826_190540" src="https://github.com/user-attachments/assets/07f1f702-5220-4e9c-8aca-65a8b440f286" />
<img width="1080" height="2400" alt="Screenshot_20260826_190906" src="https://github.com/user-attachments/assets/092bf01b-f250-496d-b9b3-381715457ac7" />
<img width="1080" height="2400" alt="Screenshot_20260826_190826" src="https://github.com/user-attachments/assets/b6f2e4d2-a0b8-4c37-8e47-6d3f96bb6c82" />
<img width="1080" height="2400" alt="Screenshot_20260826_190747" src="https://github.com/user-attachments/assets/e6fab78a-92db-4184-a91d-af42d2821ac3" />

## 👨‍💻 Autor

Alex García
