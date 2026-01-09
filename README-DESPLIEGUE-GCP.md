# README: Despliegue de Microservicios HIMFG en Google Cloud Platform

Este documento resume el proceso que hemos seguido para desplegar la aplicación de microservicios en Google Cloud, los problemas que hemos solucionado y los próximos pasos para continuar.

## 1. Arquitectura de Despliegue

El objetivo es desplegar la aplicación de microservicios basada en Spring Boot y Docker en un entorno escalable y gestionado en Google Cloud.

La arquitectura seleccionada consiste en:

-   **Orquestación de Contenedores:** Google Kubernetes Engine (GKE) para desplegar, escalar y gestionar los microservicios.
-   **Registro de Imágenes Docker:** Artifact Registry para almacenar de forma segura las imágenes de Docker de cada servicio.
-   **Base de Datos:** Cloud SQL para PostgreSQL, configurado con una IP privada para una comunicación segura y de baja latencia con los servicios en GKE.
-   **Exposición a Internet:** Un GKE Ingress, que provisiona un Balanceador de Carga de Google Cloud para dirigir el tráfico externo de forma segura al `api-gateway`.

## 2. Pasos Realizados

1.  **Preparación de GCP:** Se habilitaron las APIs necesarias (GKE, Artifact Registry, Cloud SQL) en el proyecto de Google Cloud.
2.  **Containerización:** Se construyeron las imágenes Docker para cada microservicio (`api-gateway`, `auth-service`, etc.) y se subieron a un repositorio en Artifact Registry.
3.  **Configuración de la Base de Datos:** Se creó una instancia de Cloud SQL (`himfg-db`), se habilitó el acceso por IP privada y se crearon las bases de datos y el usuario necesarios.
4.  **Creación del Clúster:** Se provisionó un clúster de GKE (`himfg-cluster`).
5.  **Manifiestos de Kubernetes:** Se creó una carpeta `k8s/` con todos los archivos de manifiesto (`.yaml`) necesarios para el despliegue:
    -   `Deployments` y `Services` para cada microservicio.
    -   Un `Secret` para gestionar las credenciales de la base de datos.
    -   Un `Ingress` para exponer el `api-gateway`.
    -   Un `kustomization.yaml` para facilitar la aplicación de todos los manifiestos.
6.  **Despliegue Inicial:** Se aplicaron los manifiestos al clúster con `kubectl apply -k k8s/`.

## 3. Troubleshooting y Soluciones

Durante el despliegue, nos encontramos y solucionamos los siguientes problemas:

### Problema 1: Pods en `CrashLoopBackOff`
-   **Síntoma:** Los servicios que dependían de la base de datos (`auth-service`, `user-service`, etc.) no arrancaban y se quedaban en un bucle de reinicios.
-   **Diagnóstico:** Los logs de los pods mostraron un error `Unable to determine Dialect without JDBC metadata`, indicando que la aplicación Spring Boot no podía conectarse a la base de datos porque no recibía la URL de conexión.
-   **Solución:** Se corrigió la variable de entorno `SPRING_DATASOURCE_URL` en los archivos de despliegue (`.yaml`) para que apuntara a la **IP privada correcta** de la instancia de Cloud SQL.

### Problema 2: Error `Server Error` (502) al acceder a la IP pública
-   **Síntoma:** Al acceder a la IP pública del Ingress, el navegador mostraba un error genérico del servidor.
-   **Diagnóstico:** El comando `kubectl describe ingress api-ingress` reveló que el backend del `api-gateway` estaba marcado como **`UNHEALTHY`**. Esto se debía a que el health check por defecto del balanceador de carga (una petición `GET` a la ruta `/`) fallaba, ya que el `api-gateway` no tiene un endpoint en esa ruta y devolvía un `404 Not Found`.
-   **Solución (en progreso):**
    1.  Se añadió la dependencia de **Spring Boot Actuator** al `pom.xml` del `api-gateway`.
    2.  Se modificó el `application.yml` del `api-gateway` para exponer el endpoint de salud en `/actuator/health`.
    3.  Se reconstruyó y subió una nueva imagen del `api-gateway` con la etiqueta `:v2`.
    4.  Se actualizó el manifiesto `k8s/api-gateway.yaml` para:
        -   Usar la nueva imagen `:v2`.
        -   Añadir `readinessProbe` y `livenessProbe` apuntando al nuevo endpoint `/actuator/health`.
    5.  Se aplicaron los cambios al clúster.

## 4. Estado Actual y Próximos Pasos (Para Mañana)

-   **Estado Actual:** Acabamos de forzar el reinicio del `api-gateway-deployment` con `kubectl rollout restart` para asegurarnos de que tome la última configuración que incluye los health checks. Los nuevos pods del `api-gateway` deberían estar arrancando.

-   **Próximo Paso Mañana:**
    1.  Verificar que los nuevos pods del `api-gateway` estén en estado `Running (1/1)`.
        ```bash
        kubectl get pods
        ```
    2.  Verificar que el backend del Ingress ahora esté `HEALTHY`.
        ```bash
        kubectl describe ingress api-ingress
        ```
    3.  Una vez que esté saludable, intentar acceder de nuevo al Swagger UI a través de la IP pública del Ingress.
        ```
        http://<IP_PUBLICA_DEL_INGRESS>/webjars/swagger-ui/index.html
        ```

¡Que descanses! Mañana continuamos desde este punto.
