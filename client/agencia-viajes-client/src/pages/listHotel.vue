<template>
  <div class="list-hotel-page">
    <h1 class="page-title mb-2">Buscar hoteles por destino</h1>
    <p class="text-subtitle-1 mb-6 text-medium-emphasis">
      Selecciona un destino y encuentra alojamientos disponibles.
    </p>

    <v-select
      v-model="selectedDestinationId"
      :items="destinations"
      item-title="name"
      item-value="id"
      label="Selecciona un destino"
      clearable
      density="comfortable"
      variant="outlined"
      class="mb-4"
      @update:model-value="loadHotels"
    />

    <v-card v-if="selectedDestinationId" rounded="xl" variant="tonal" class="mb-4">
      <v-card-text>
        <v-row>
          <v-col cols="12" md="4">
            <v-text-field v-model="hotelSearch" label="Search hotels" prepend-inner-icon="mdi-magnify" variant="outlined" clearable hide-details />
          </v-col>
          <v-col cols="6" md="2">
            <v-text-field v-model.number="maxPrice" label="Max price" type="number" variant="outlined" clearable hide-details />
          </v-col>
          <v-col cols="6" md="2">
            <v-text-field v-model.number="minRating" label="Min rating" type="number" min="0" max="5" step="0.1" variant="outlined" clearable hide-details />
          </v-col>
          <v-col cols="12" md="4">
            <v-select v-model="serviceFilter" :items="serviceOptions" label="Service" variant="outlined" hide-details />
          </v-col>
        </v-row>
        <div class="mt-3 text-caption text-medium-emphasis">{{ filteredHotels.length }} hotels found</div>
      </v-card-text>
    </v-card>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <v-row v-if="filteredHotels.length">
      <v-col v-for="hotel in filteredHotels" :key="hotel.id" cols="12" sm="6" md="4">
        <HotelCard :hotel="hotel" />
      </v-col>
    </v-row>

    <template v-if="selectedDestinationId">
      <p v-if="filteredHotels.length === 0 && !error" class="mt-4">No hotels match these filters.</p>
      <div class="mt-4">
        <v-btn color="primary" append-icon="mdi-arrow-right" @click="goToDetail">
          Ver hoteles y vuelos de este destino
        </v-btn>
      </div>
    </template>

    <p v-if="destinations.length === 0 && !error">No hay destinos disponibles.</p>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import HotelService from '../services/HotelService';
import CachedDataService from '../services/CachedDataService';
import { Hotel } from '../models/Hotel';
import { Destination } from '../models/Destination';
import HotelCard from '../components/cards/HotelCard.vue';

const router = useRouter();

const destinations = ref<Destination[]>([]);
const selectedDestinationId = ref<number | null>(null);
const hotels = ref<Hotel[]>([]);
const error = ref<string | null>(null);
const hotelSearch = ref('');
const maxPrice = ref<number | undefined>();
const minRating = ref<number | undefined>();
const serviceFilter = ref('all');
const serviceOptions = [
  { title: 'All services', value: 'all' },
  { title: 'WiFi', value: 'freeWifi' },
  { title: 'Breakfast', value: 'breakfastIncluded' },
  { title: 'Pool', value: 'pool' },
  { title: 'Parking', value: 'parking' },
  { title: 'Gym', value: 'gym' },
  { title: 'Spa', value: 'spa' },
];

const selectedDestination = computed(() =>
  destinations.value.find((d) => d.id === selectedDestinationId.value),
);

const filteredHotels = computed(() => {
  const term = hotelSearch.value.trim().toLowerCase();
  return hotels.value.filter((hotel) => {
    const matchesSearch = !term || [hotel.name, hotel.location, hotel.description]
      .filter(Boolean)
      .join(' ')
      .toLowerCase()
      .includes(term);
    const matchesPrice = maxPrice.value == null || (hotel.pricePerNight ?? Number.MAX_SAFE_INTEGER) <= maxPrice.value;
    const matchesRating = minRating.value == null || (hotel.rating ?? 0) >= minRating.value;
    const matchesService = serviceFilter.value === 'all' || Boolean((hotel as any)[serviceFilter.value]);
    return matchesSearch && matchesPrice && matchesRating && matchesService;
  });
});

onMounted(async () => {
  try {
    destinations.value = await CachedDataService.getDestinations();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar destinos';
  }
});

const loadHotels = async () => {
  if (!selectedDestinationId.value) {
    hotels.value = [];
    return;
  }
  error.value = null;
  try {
    hotels.value = await HotelService.getByDestinationId(selectedDestinationId.value);
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar hoteles';
    hotels.value = [];
  }
};

const goToDetail = () => {
  const slug = selectedDestination.value?.slug;
  if (slug) {
    router.push(`/destinos/${slug}/hoteles`);
  }
};

</script>

<style scoped>
.list-hotel-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.page-title {
  font-size: clamp(1.8rem, 8vw, 2.5rem);
  line-height: 1.1;
}

.hotel-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.hotel-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.25) !important;
}

.card-img {
  border-radius: 16px 16px 0 0;
  position: relative;
  height: clamp(190px, 48vw, 220px);
}

.hotel-card :deep(.v-card-text) {
  flex: 1;
}

.access-btn {
  width: 100%;
  min-height: 44px;
}

.img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.25), transparent);
}

@media (min-width: 768px) {
  .card-img {
    height: 190px;
  }

  .access-btn {
    width: auto;
    min-width: 132px;
    margin-left: auto;
  }
}
</style>
