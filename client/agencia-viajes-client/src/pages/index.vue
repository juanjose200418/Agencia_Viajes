<template>
  <div>
    <v-sheet class="hero" :style="{ backgroundImage: `linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.7)), url(${heroImage})` }">
      <h1 class="hero-title font-weight-bold">Encuentra tu chollo de viaje</h1>
      <p class="text-subtitle-1 mt-2">Hoteles, vuelos y destinos al mejor precio.</p>
      <form class="hero-search mx-auto mt-6" @submit.prevent="goToSearch">
        <v-text-field
          v-model="searchQuery"
          placeholder="Busca con IA: playa, montaña, luna de miel, ciudad…"
          variant="solo-inverted"
          rounded="lg"
          hide-details
          density="comfortable"
          class="hero-search-field"
        >
          <template v-slot:prepend-inner>
            <v-icon icon="mdi-magnify" />
          </template>
        </v-text-field>
        <v-btn type="submit" color="accent" size="large" rounded="lg">
          <v-icon icon="mdi-auto-fix" class="me-1" />
          Buscar
        </v-btn>
      </form>
      <div class="hero-actions mt-6">
        <v-btn color="accent" size="large" @click="goToDestinations">Ver destinos</v-btn>
        <v-btn variant="outlined" color="white" size="large" @click="goToHotels">Ver hoteles</v-btn>
      </div>
    </v-sheet>

    <template v-if="isAuthenticated">
      <h2 class="section-title mt-10">Destinos destacados</h2>
      <v-alert v-if="loadError" type="error" class="mt-4">{{ loadError }}</v-alert>
      <v-row class="mt-2">
        <v-col v-for="d in destinations" :key="d.id" cols="12" sm="6" md="4">
          <v-card class="dest-card" rounded="xl" elevation="4" @click="goToDestination(d)">
            <v-img :src="destinationImage(d, 600, 400)" :alt="destinationAlt(d)" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" height="220" cover>
              <div class="img-overlay" />
              <div class="img-content">
                <h3 class="text-h6 font-weight-bold text-white">{{ d.name }}</h3>
                <p v-if="d.location" class="text-caption text-white mb-0">
                  <v-icon icon="mdi-map-marker" size="small" /> {{ d.location }}
                </p>
              </div>
            </v-img>
          </v-card>
        </v-col>
      </v-row>
      <p v-if="destinations.length === 0 && !loadError" class="mt-4">No hay destinos disponibles.</p>
    </template>

    <v-sheet v-else class="text-center my-8 my-md-12 pa-5 pa-md-10" rounded="xl">
      <p class="text-h5">Inicia sesión para ver destinos, hoteles y vuelos.</p>
      <v-btn color="primary" class="mt-4" @click="goToLogin">Iniciar sesión</v-btn>
      <v-btn variant="outlined" class="mt-4 ml-2" @click="goToRegister">Registrarse</v-btn>
    </v-sheet>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import router from '../router';
import CachedDataService from '../services/CachedDataService';
import { Destination } from '../models/Destination';
import { destinationImage } from '../utils/images';

const heroImage = 'https://picsum.photos/seed/hero-viajes/1600/600';
const searchQuery = ref('');

const goToSearch = () => {
  router.push({ path: '/search', query: { q: searchQuery.value } });
};

const isAuthenticated = computed(() => !!localStorage.getItem('authToken'));
const destinations = ref<Destination[]>([]);
const loadError = ref<string | null>(null);

onMounted(async () => {
  if (!isAuthenticated.value) return;
  try {
    destinations.value = await CachedDataService.getDestinations();
  } catch (error) {
    loadError.value = error instanceof Error ? error.message : 'Error al cargar destinos';
  }
});

const goToDestination = (d: Destination) => {
  if (d.slug) router.push(`/destinos/${d.slug}/hoteles`);
};
const destinationAlt = (d: Destination) => {
  const name = d.name || 'destino de viaje';
  const location = d.country || d.location;
  return location ? `Vista de ${name}, ${location}` : `Vista de ${name}`;
};
const goToDestinations = () => router.push('/destinos');
const goToHotels = () => router.push('/hotels');
const goToLogin = () => router.push('/login');
const goToRegister = () => router.push('/register');
</script>

<style scoped>
.hero-search {
  max-width: 640px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(4px);
  border-radius: 24px;
  padding: 12px;
}

.hero-search-field {
  width: 100%;
  flex: 1;
}

.hero-search :deep(.v-btn) {
  width: 100%;
  min-height: 46px;
}

.hero {
  padding: clamp(36px, 12vw, 80px) 16px;
  text-align: center;
  background-size: cover;
  background-position: center;
  border-radius: 24px;
  color: #fff;
}

.hero-title,
.section-title {
  font-size: clamp(1.8rem, 8vw, 3rem);
  line-height: 1.1;
}

.hero-actions {
  display: grid;
  gap: 12px;
  max-width: 420px;
  margin-inline: auto;
}

.hero-actions :deep(.v-btn) {
  min-height: 46px;
}

.dest-card {
  cursor: pointer;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.dest-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.25) !important;
}

.img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.05));
}

.img-content {
  position: absolute;
  left: 16px;
  right: 16px;
  bottom: 16px;
  z-index: 1;
}

@media (min-width: 768px) {
  .hero-search {
    flex-direction: row;
    padding: 8px 8px 8px 20px;
  }

  .hero-search :deep(.v-btn) {
    width: auto;
  }

  .hero-actions {
    display: flex;
    justify-content: center;
  }
}
</style>
