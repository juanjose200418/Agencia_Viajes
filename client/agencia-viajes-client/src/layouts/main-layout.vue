<template>
  <v-container fluid>
    <v-header class="header">
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" />
      <div class="contact-info">
        <v-btn variant="plain" class="contact-btn" @click="call">
          <i class="fas fa-phone"></i> + xx xxx xxx xxxx
        </v-btn>
        <v-btn variant="plain" class="contact-btn" @click="email">
          <i class="fas fa-envelope"></i> ejemplo@company.com
        </v-btn>
      </div>
      <div class="social-icons">
        <a href="#"><i class="fab fa-facebook-f"></i></a>
        <a href="#"><i class="fab fa-twitter"></i></a>
        <a href="#"><i class="fab fa-instagram"></i></a>
        <a href="#"><i class="fab fa-pinterest"></i></a>
      </div>
    </v-header>

    <v-nav>
      <ul class="nav-list">
        <h1 class="header-title">EncuentraTuChollo.com</h1>
        <li>
          <v-btn @click="home">Home</v-btn> 
        </li>
        <li>
          <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
              <v-btn v-bind="props">Destinations</v-btn>
            </template>
            <v-list>
              <v-list-item @click="goToDestinations">Destinations</v-list-item>
              <v-list-item @click="goToDestinations">Destinations Detail</v-list-item>
            </v-list>
          </v-menu>
        </li>
        <li>
          <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
              <v-btn v-bind="props">Hotels</v-btn>
            </template>
            <v-list>
              <v-list-item @click="goToHotels">Hotels</v-list-item>
              <v-list-item @click="goToHotels">Hotels List</v-list-item>
              <v-list-item @click="goToHotels">Hotels Details</v-list-item>
            </v-list>
          </v-menu>
        </li>
        <li>
          <v-btn @click="goToContacts">Contactos</v-btn>
        </li>
        <li>
          <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
              <v-btn v-bind="props">Cuenta</v-btn>
            </template>
            <v-list>
              <v-list-item @click="login">Login</v-list-item>
              <v-list-item @click="register">Register</v-list-item>
            </v-list>
          </v-menu>
        </li>
      </ul>
    </v-nav>

    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">

        <main>
          <slot></slot>
        </main>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
/*import PostContainer from './components/PostContainer.vue';
import PostCard from './components/PostCard.vue';
import Page from './pages/register';*/
import router from '../router';


const login = () => {
  console.log('Redirigir a la página de inicio de sesión');
  router.push('/login')
}

const register = () => {
  console.log('Redirigiendo a la página de registro'); 
  router.push('/register'); 
};

const email = () => {
  window.location.href = 'mailto:ejemplo@company.com';
}

const call = () => {
  window.location.href = 'tel:+xx-xxx-xxx-xxxx';
}

const home = () => {
  router.push('/'); 
};

const goToDestinations = () => {
  console.log('Ir a Destinos');
}

const goToHotels = () => {
  console.log('Ir a Hoteles');
  router.push('/hotels')
}

const goToContacts = () => {
  console.log('Ir a Contactos');
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #073ce900;
  font-size: 12px; 
  padding: 10px; 
}

.contact-info {
  display: flex;
  gap: 20px;
}

.contact-btn {
  font-size: 10px; 
}

.social-icons {
  display: flex;
  gap: 10px;
}

.social-icons a {
  color: white;
  font-size: 18px;
}

.header-title {
  color: black; 
  margin: 0; 
}

.nav-list {
  list-style: none;
  display: flex;
  justify-content: center;
  align-items: center; 
  padding: 20px;
  margin: 0;
  background-color: rgb(211, 211, 211);
}

.nav-list li {
  margin: 0 15px;
}

.nav-list a,
.nav-list v-btn {
  color: black; 
  text-decoration: none;
  font-size: 16px; 
  text-transform: none; 
}

.nav-list a:hover,
.nav-list v-btn:hover {
  text-decoration: underline;
}

.post-section {
  margin-top: 20px;
}

.content {
  padding: 20px;
  background-color: #000;
}
</style>
