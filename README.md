#Aplicación URL Shortener

Indicaciones para Ejecutar Proyecto

Base de datos (MongoDB)
Correr los siguientes scripts (MongoDB Shell)
1.	use urlshortener
2.	db.createCollection("url")
3.	db.url.createIndex({urlShort: 1}, {unique:true});

Aplicación Url Shortener
Spring Boot

1.	Correr las imágenes de docker-compose.yml (Redis, Prometheus, Grafana).

2.	Correr los componentes en el siguiente orden:
2.1	discovery-server
2.2	urlshortener
2.3	agy-gateway

Si deseas revisar el contenido en Prometheus y Grafana:
- Prometheus: http://localhost:9090/targets?search=
- Grafana: http://localhost:3000/ (En caso de solicitar clave y usuario = ADMIN)


