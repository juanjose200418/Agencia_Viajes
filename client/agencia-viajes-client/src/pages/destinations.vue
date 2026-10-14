<template>
  <div class="destinations-page">
    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <v-row>
      <v-col v-for="dest in destinations" :key="dest.id" cols="12" sm="6" md="4">
        <v-card
          class="dest-card"
          rounded="xl"
          elevation="4"
          @click="goToDestination(dest)"
        >
          <v-img
            :src="destinationImage(dest, 600, 400)"
            :alt="destinationAlt(dest)"
            lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw=="
            cover
            class="dest-img"
          >
            <div class="img-overlay" />
            <div class="img-content">
              <v-chip
                v-if="dest.averageCost"
                color="accent"
                size="small"
                class="mb-2"
              >
                {{ formatPrice(dest.averageCost) }}
              </v-chip>
              <h3 class="text-h5 font-weight-bold text-white">{{ dest.name }}</h3>
              <p v-if="dest.location" class="text-subtitle-2 text-white mt-1 mb-0">
                <v-icon icon="mdi-map-marker" size="small" /> {{ dest.location }}
              </p>
            </div>
          </v-img>
          <v-card-text class="dest-body">
            <p class="text-body-2 mb-0">{{ dest.description }}</p>
            <v-btn
              color="primary"
              class="mt-3"
              block
              variant="tonal"
              append-icon="mdi-arrow-right"
            >
              Ver hoteles y vuelos
            </v-btn>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <p v-if="destinations.length === 0 && !error">No hay destinos disponibles.</p>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import DestinationService from '../services/DestinationService';
import { Destination } from '../models/Destination';
import { destinationImage } from '../utils/images';

const router = useRouter();

const destinations = ref<Destination[]>([]);
const error = ref<string | null>(null);

onMounted(async () => {
  try {
    destinations.value = await DestinationService.getAll();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar destinos';
  }
});

const goToDestination = (dest: Destination) => {
  if (dest.slug) {
    router.push(`/destinos/${dest.slug}/hoteles`);
  }
};

const destinationAlt = (dest: Destination) => {
  const name = dest.name || 'destino de viaje';
  const location = dest.country || dest.location;
  return location ? `Vista de ${name}, ${location}` : `Vista de ${name}`;
};

const formatPrice = (value?: number) =>
  value != null ? `${value.toLocaleString('es-ES')} €` : '';
</script>

<style scoped>
.destinations-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.dest-card {
  cursor: pointer;
  overflow: hidden;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.dest-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.28) !important;
}

.dest-img {
  position: relative;
  height: clamp(220px, 58vw, 270px);
}

.img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0.1));
}

.img-content {
  position: absolute;
  left: 20px;
  right: 20px;
  bottom: 20px;
  z-index: 1;
}

.dest-body {
  background: #1e1e1e;
}

@media (max-width: 600px) {
  .dest-body :deep(.v-btn) {
    min-height: 44px;
  }
}
</style>
