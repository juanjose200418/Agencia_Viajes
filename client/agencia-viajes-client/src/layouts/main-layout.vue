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
          <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
              <v-btn v-bind="props" @click="home">Home</v-btn>
            </template>
          </v-menu>
        </li>
        <li>
          <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
              <v-btn v-bind="props">Destinations</v-btn>
            </template>
            <v-list>
              <v-list-item @click="destinations">Destinations</v-list-item>
             <!--<v-list-item @click="goToDestinations">Destinations Detail</v-list-item>--> 
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
              <v-list-item @click="goToHotelsList">Hotels List</v-list-item>
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

    <main>
      <slot></slot>
    </main>

    <v-footer class="footer" padless>
      <v-container fluid>
        <v-row>
          <v-col cols="12" md="4">
            <h4>Welcome to our Trip and Tour Agency</h4>
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
            <p><strong>Tel:</strong> + 92 666 999 0000</p>
            <p><strong>Email:</strong> needhelp@company.com</p>
            <p><strong>Address:</strong> 666 Road, Brooklyn Street, New York</p>
          </v-col>
          <v-col cols="12" md="4">
            <h4>Company</h4>
            <ul class="footer-links">
              <li><v-btn @click="goToAboutUs">About Us</v-btn></li>
              <li><v-btn @click="goToBlog">Community Blog</v-btn></li>
              <li><v-btn @click="goToRewards">Rewards</v-btn></li>
              <li><v-btn @click="goToWorkWithUs">Work with Us</v-btn></li>
              <li><v-btn @click="goToMeetTheTeam">Meet the Team</v-btn></li>
            </ul>
          </v-col>
          <v-col cols="12" md="4">
            <h4>Explore</h4>
            <ul class="footer-links">
              <li><v-btn @click="goToAccount">Account</v-btn></li>
              <li><v-btn @click="goToLegal">Legal</v-btn></li>
              <li><v-btn @click="goToContact">Contact</v-btn></li>
              <li><v-btn @click="goToAffiliate">Affiliate Program</v-btn></li>
              <li><v-btn @click="goToPrivacy">Privacy Policy</v-btn></li>
            </ul>
          </v-col>
        </v-row>
        <v-divider></v-divider>
        <v-row>
          
          <v-col cols="12" md="6" class="text-center">
            <p class="footer-copy">© All Copyright 2024, EncuentraTuChollo.com</p>
          </v-col>
        </v-row>
      </v-container>
    </v-footer>
  </v-container>
</template>

<script lang="ts" setup>
import router from '../router';

const login = () => {
  console.log('Redirigir a la página de inicio de sesión');
  router.push('/login');
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

const destinations = () => {
  console.log('Ir a Destinos');
  router.push('/destinations');
}

const goToHotels = () => {
  console.log('Ir a Hoteles');
  router.push('/hotels');
}

const goToHotelsList = () => {
  console.log('Ir a Listado de hoteles');
  router.push('/listHotel');
}

const goToContacts = () => {
  console.log('Ir a Contactos');
}

const goToAboutUs = () => {

}

const goToBlog = () => {

}

const goToRewards = () => {

}

const goToWorkWithUs = () => {

}

const goToMeetTheTeam = () => {
}

const goToAccount = () => {
  // Redirigir a la página de Account
}

const goToLegal = () => {
  // Redirigir a la página de Legal
}

const goToContact = () => {
  // Redirigir a la página de Contact
}

const goToAffiliate = () => {
  // Redirigir a la página de Affiliate Program
}

const goToPrivacy = () => {
  // Redirigir a la página de Privacy Policy
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

.footer {
  background-color: rgb(211, 211, 211);
  color: black;
  padding: 40px 0;
}

.footer-title {
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: bold;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin: 5px 0;
}

.footer-copy {
  margin-top: 20px;
  text-align: center;
  font-size: 12px;
}
</style>
