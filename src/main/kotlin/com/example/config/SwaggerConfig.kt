package com.example.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.http.HttpHeaders

@OpenAPIDefinition(
    info = Info(
        title = "API AGÉNCIA",
        description = """
            La API permite la gestión de usuarios, reservas, destinos, hoteles e imágenes. 
            Implementa seguridad mediante autenticación JWT y control de acceso por roles (USER, ADMIN, SUPER_ADMIN).

            Funcionalidades principales:

                Autenticación: Inicio de sesión y registro de usuarios.
                Gestión de Usuarios: Crear, consultar, actualizar y eliminar usuarios, con restricciones de acceso por roles.
                Reservas: CRUD completo de reservas.
                Destinos y Hoteles: CRUD completo para destinos y hoteles, con control de acceso administrativo.
                Imágenes: Gestión de imágenes con soporte de paginación.

            Seguridad:
            Autenticación mediante JWT y autorización por roles. Los roles administran el acceso a los distintos endpoints.
        """,
        termsOfService = "https://juanjosejaen.com/terminos_y_servicios",
        version = "1.0.0",
        contact = Contact(
            name = "Juan José Jaén García",
            url = "https://www.linkedin.com/in/juan-josé-jaén-garcía-aaa052306",
            email = "juanjosejaen11@gmail.com"
        ),
        license = License(
            name = "Standard Software Use Licence for Agencia",
            url = "https://juanjosejaen.com/license"
        )
    ),
    servers = [
        Server(
            description = "DEV SERVER",
            url = "http://localhost:8080"
        ),
        Server(
            description = "PROD SERVER",
            url = "https://juanjosejaen.com"
        ),
        Server(
            description = "DEV SERVER",
            url = "http://localhost:8080"
        )
    ],
    security = [
        SecurityRequirement(
            name = "Security Token"
        )
    ]
)
@SecurityScheme(
    name = "Security Token",
    description = "Acceso al Token Por My API",
    type = SecuritySchemeType.HTTP,
    paramName = HttpHeaders.AUTHORIZATION,
    `in` = SecuritySchemeIn.HEADER,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class SwaggerConfig
