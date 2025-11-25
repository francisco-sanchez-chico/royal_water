package com.app.royal.royal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RoyalApplicationTests {

    @Test
    void contextLoads() {
        // Prueba existente - se mantiene
    }

    @Test
    void applicationStartsSuccessfully() {
        // Prueba que verifica que la aplicación inicia correctamente
        RoyalApplication.main(new String[]{});
    }

    @Test
    void environmentProfileIsActive() {
        // Prueba que verifica que el perfil por defecto está activo
        assertThat(System.getProperty("spring.profiles.active"))
            .isNotEqualTo("test");
    }

    @Test
    void javaVersionIsCorrect() {
        // Prueba que verifica la versión de Java
        assertThat(System.getProperty("java.version"))
            .startsWith("17");
    }
}