<template>
  <div class="page-container">
    <div class="header">
      <h1>EncuentraTuChollo.com</h1>
      <div class="auth-links">
        <h5>inicio de sesion</h5>
        <h5>Registro</h5>
      </div>
    </div>
  
    <div class="content">
      <CounterOption />
      <CounterComposition />
      <ExampleSelective />
      <ExampleFor />
    </div>
    
    <!-- Colocamos PostContainer y PostCard abajo a la derecha -->
    <div class="post-section">
      <PostContainer class="post-container" />
      <PostCard class="post-card" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import PostContainer from './components/PostContainer.vue'
import PostCard from './components/PostCard.vue'
import CounterOption from './components/CounterOption.vue'
import CounterComposition from './components/CounterComposition.vue'
import ExampleSelective from './components/ExampleSelective.vue'
import ExampleFor from './components/ExampleFor.vue'
</script>

<style scoped>
/* Contenedor de la página completa */
.page-container {
  position: relative; /* Para que el PostContainer y PostCard se posicionen relativos a este contenedor */
  min-height: 100vh;  /* Asegura que la página ocupe toda la altura de la ventana */
  padding-bottom: 1800px; /* Espacio de seguridad para el contenido */
}

/* Cabecera */
.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}

/* Enlaces de autenticación */
.auth-links {
  display: flex;
  gap: 20px;
}

/* Estilo del título */
h1 {
  color: red;
  margin: 0;
  text-align: center;
}

/* Sección que contiene el PostContainer y PostCard */
.post-section {
  position: absolute;
  bottom: 0;
  right: 0;
  display: flex;
  flex-direction: column;
  align-items: flex; /* Alinea los elementos a la derecha */
  margin: 20px;
}

/* Posicionamiento de PostContainer y PostCard */
.post-container,
.post-card {
  margin-bottom: 10px; /* Añade un pequeño espacio entre PostContainer y PostCard */
}

.auth-links h5 {
  margin: 0;
  cursor: pointer;
}
</style>
