<template>
  <div class="destination-detail">
    <v-btn variant="text" class="mb-4" prepend-icon="mdi-arrow-left" @click="goBack">
      Volver a destinos
    </v-btn>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <template v-if="destination">
      <v-sheet class="hero" :style="{ backgroundImage: `linear-gradient(rgba(0,0,0,0.45), rgba(0,0,0,0.75)), url(${heroImage})` }">
        <v-chip color="accent" class="hero-cost" v-if="destination.averageCost">
          Coste medio desde {{ formatPrice(destination.averageCost) }}
        </v-chip>
        <h1 class="text-h3 font-weight-bold">{{ destination.name }}</h1>
        <p v-if="destination.location" class="text-subtitle-1 mt-2">
          <v-icon icon="mdi-map-marker" size="small" /> {{ destination.location }}
        </p>
      </v-sheet>

      <div class="section">
        <h2 class="text-h5 mb-2">Sobre {{ destination.name }}</h2>
        <p class="text-body-1">{{ destination.description }}</p>
        <div v-if="destination.pointsOfInterest?.length" class="points mt-4">
          <div class="text-subtitle-2 mb-2">Puntos de interés</div>
          <v-chip v-for="point in destination.pointsOfInterest" :key="point" prepend-icon="mdi-star" class="mr-1 mb-1" color="primary" variant="outlined">
            {{ point }}
          </v-chip>
        </div>
      </div>

      <div class="section">
        <div class="d-flex align-center justify-space-between mb-4">
          <h2 class="text-h5">Hoteles en {{ destination.name }}</h2>
          <v-chip color="primary">{{ hotels.length }} hoteles</v-chip>
        </div>
        <v-row v-if="hotels.length">
          <v-col v-for="hotel in hotels" :key="hotel.id" cols="12" sm="6" md="4">
            <v-card class="hotel-card" rounded="xl" elevation="4">
              <v-img :src="hotelImage(hotel)" :alt="hotelAlt(hotel)" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" height="180" cover class="card-img">
                <div class="img-overlay" />
              </v-img>
              <v-card-title class="font-weight-bold">{{ hotel.name }}</v-card-title>
              <v-card-subtitle v-if="hotel.location">
                <v-icon icon="mdi-map-marker" size="small" /> {{ hotel.location }}
              </v-card-subtitle>
              <v-card-text>
                <p class="text-body-2">{{ hotel.description }}</p>
                <div class="mt-2">
                  <v-chip v-if="hotel.freeWifi" size="x-small" class="mr-1 mb-1">WiFi</v-chip>
                  <v-chip v-if="hotel.breakfastIncluded" size="x-small" class="mr-1 mb-1">Desayuno</v-chip>
                  <v-chip v-if="hotel.pool" size="x-small" class="mr-1 mb-1">Piscina</v-chip>
                  <v-chip v-if="hotel.gym" size="x-small" class="mr-1 mb-1">Gimnasio</v-chip>
                  <v-chip v-if="hotel.spa" size="x-small" class="mr-1 mb-1">Spa</v-chip>
                  <v-chip v-if="hotel.parking" size="x-small" class="mr-1 mb-1">Parking</v-chip>
                </div>
              </v-card-text>
              <v-card-actions v-if="hotel.phone || hotel.externalUrl">
                <v-btn v-if="hotel.phone" size="small" prepend-icon="mdi-phone" variant="text">{{ hotel.phone }}</v-btn>
                <v-spacer />
                <v-btn v-if="hotel.externalUrl" size="small" color="secondary" variant="tonal" prepend-icon="mdi-open-in-new" @click="openAccess(hotel)">
                  Acceder
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
        <p v-else>No hay hoteles registrados para este destino.</p>
      </div>

      <div class="section">
        <div class="d-flex align-center justify-space-between mb-4">
          <h2 class="text-h5">Vuelos a {{ destination.name }}</h2>
          <v-chip color="primary">{{ flights.length }} vuelos</v-chip>
        </div>
        <v-card v-if="flights.length" rounded="xl" elevation="4">
          <v-table>
            <thead>
              <tr>
                <th>Vuelo</th>
                <th>Aerolínea</th>
                <th>Salida</th>
                <th>Llegada</th>
                <th>Duración</th>
                <th class="text-right">Precio</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="flight in flights" :key="flight.id">
                <td class="font-weight-medium">{{ flight.flightNumber }}</td>
                <td>{{ flight.airline }}</td>
                <td>{{ formatDate(flight.departure) }}</td>
                <td>{{ formatDate(flight.arrival) }}</td>
                <td>{{ formatDuration(flight.duration) }}</td>
                <td class="text-right font-weight-bold text-primary">{{ formatPrice(flight.price) }}</td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
        <p v-else>No hay vuelos registrados para este destino.</p>
      </div>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DestinationService from '../../services/DestinationService';
import HotelService from '../../services/HotelService';
import FlightService from '../../services/FlightService';
import { Destination } from '../../models/Destination';
import { Hotel } from '../../models/Hotel';
import { Flight } from '../../models/Flight';
import { destinationImage, hotelImage } from '../../utils/images';

const route = useRoute();
const router = useRouter();

const destination = ref<Destination | null>(null);
const hotels = ref<Hotel[]>([]);
const flights = ref<Flight[]>([]);
const error = ref<string | null>(null);

const heroImage = computed(() => destinationImage(destination.value, 1600, 500));

onMounted(async () => {
  const id = Number(route.params.id);
  if (!id) {
    error.value = 'Destino no válido';
    return;
  }
  try {
    const [dest, hotelList, flightList] = await Promise.all([
      DestinationService.getById(id),
      HotelService.getByDestinationId(id),
      FlightService.getByDestinationId(id),
    ]);
    destination.value = dest;
    hotels.value = hotelList;
    flights.value = flightList;
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar el destino';
  }
});

const goBack = () => router.push('/destinations');

function openAccess(hotel: Hotel) {
  const url = hotel.externalUrl;
  if (!url) return;
  if (url.startsWith('/')) {
    router.push(url);
    return;
  }
  window.open(url, '_blank', 'noopener,noreferrer');
}

const hotelAlt = (hotel: Hotel) => {
  const name = hotel.name || 'hotel';
  const location = hotel.location || destination.value?.name;
  return location ? `Imagen de ${name} en ${location}` : `Imagen de ${name}`;
};

const formatPrice = (value?: number) =>
  value != null ? `${value.toLocaleString('es-ES')} €` : '—';

const formatDate = (value?: string) => {
  if (!value) return '—';
  const d = new Date(value);
  if (isNaN(d.getTime())) return value;
  return d.toLocaleString('es-ES', {
    day: '2-digit',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit',
  });
};

const formatDuration = (minutes?: number) => {
  if (minutes == null) return '—';
  const h = Math.floor(minutes / 60);
  const m = Math.round(minutes % 60);
  return h > 0 ? `${h}h ${m}m` : `${m}m`;
};
</script>

<style scoped>
.destination-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.hero {
  position: relative;
  background-size: cover;
  background-position: center;
  border-radius: 24px;
  padding: clamp(42px, 12vw, 60px) clamp(16px, 5vw, 40px);
  color: #fff;
  text-align: left;
}

.hero-cost {
  position: static;
  margin-bottom: 12px;
}

.section {
  margin-top: clamp(24px, 7vw, 36px);
}

.hotel-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.hotel-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.25) !important;
}

.card-img {
  border-radius: 16px 16px 0 0;
  height: clamp(190px, 48vw, 220px);
}

.img-overlay {
  height: 100%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.4), transparent);
}

@media (min-width: 768px) {
  .hero-cost {
    position: absolute;
    top: 20px;
    right: 20px;
    margin-bottom: 0;
  }

  .card-img {
    height: 180px;
  }
}
</style>
