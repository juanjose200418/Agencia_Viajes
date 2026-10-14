<template>
  <div class="hotel-detail">
    <div class="mb-4 d-flex align-center">
      <v-btn variant="text" prepend-icon="mdi-arrow-left" @click="goBack">
        Hoteles en {{ cityName }}
      </v-btn>
    </div>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>
    <v-skeleton-loader v-if="loading" type="image, article, article" class="mt-2" />

    <template v-if="hotel && !loading">
      <h1 class="detail-title font-weight-bold">{{ hotel.name }}</h1>
      <p v-if="hotel.location" class="text-subtitle-1 text-medium-emphasis">
        <v-icon icon="mdi-map-marker" size="small" /> {{ hotel.location }}
      </p>
      <div class="detail-actions d-flex align-center ga-2 mt-2">
        <v-chip v-if="hotel.rating" color="accent">
          <v-icon icon="mdi-star" size="small" /> {{ formatRating(hotel.rating) }}
        </v-chip>
        <v-chip color="primary">
          <span class="font-weight-bold">{{ formatPrice(hotel.pricePerNight) }}</span> / noche
        </v-chip>
        <v-btn v-if="hotel.externalUrl" color="secondary" prepend-icon="mdi-open-in-new" @click="openAccess">
          Acceder
        </v-btn>
      </div>

      <div class="gallery mt-6">
        <v-row>
          <v-col cols="12" md="8">
            <v-img :src="gallery[0]" :alt="mainImageAlt" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" height="360" cover rounded="xl" />
          </v-col>
          <v-col cols="6" md="4">
            <v-img :src="gallery[1]" :alt="secondaryImageAlt" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" height="174" cover rounded="xl" class="mb-2" />
            <v-img :src="gallery[2]" :alt="roomImageAlt" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" height="174" cover rounded="xl" />
          </v-col>
        </v-row>
      </div>

      <div class="section">
        <h2 class="text-h5 mb-2">Servicios</h2>
        <v-row v-if="services.length">
          <v-col v-for="s in services" :key="s.label" cols="6" sm="4" md="3">
            <v-chip variant="outlined" class="pa-3">
              <v-icon :icon="s.icon" size="small" class="mr-2" /> {{ s.label }}
            </v-chip>
          </v-col>
        </v-row>
        <p v-else>Información de servicios no disponible.</p>
      </div>

      <div class="section">
        <h2 class="text-h5 mb-2">Descripción</h2>
        <p class="text-body-1">{{ hotel.description }}</p>
        <p v-if="hotel.guestReviews" class="text-body-2">
          <v-icon icon="mdi-comment-quote" size="small" /> {{ hotel.guestReviews }}
        </p>
        <p v-if="hotel.specialPackages" class="text-body-2">
          <v-icon icon="mdi-gift" size="small" /> {{ hotel.specialPackages }}
        </p>
      </div>

      <div class="section detail-grid">
        <v-card rounded="xl" variant="tonal">
          <v-card-title>Información del alojamiento</v-card-title>
          <v-card-text>
            <p v-if="destination?.name" class="text-body-2 mb-2">
              <v-icon icon="mdi-map" size="small" /> Destino: {{ destination.name }}
            </p>
            <p v-if="hotel.address" class="text-body-2 mb-2">
              <v-icon icon="mdi-map-marker" size="small" /> {{ hotel.address }}
            </p>
            <p class="text-body-2 mb-0">
              <v-icon icon="mdi-cash" size="small" /> {{ formatPrice(hotel.pricePerNight) }} / noche
            </p>
          </v-card-text>
        </v-card>

        <v-card rounded="xl" variant="tonal">
          <v-card-title>Información de la reserva</v-card-title>
          <v-card-text>
            <div v-if="hotel.cancellationPolicy">
              <div class="text-subtitle-2 mb-1">Política de cancelación</div>
              <p class="text-body-2 mb-0">{{ hotel.cancellationPolicy }}</p>
            </div>
            <p v-else class="text-body-2 text-medium-emphasis mb-0">Política de cancelación no disponible.</p>
          </v-card-text>
        </v-card>

        <v-card v-if="hotel.email || hotel.phone" rounded="xl" variant="tonal">
          <v-card-title>Contacto</v-card-title>
          <v-card-text>
            <p v-if="hotel.email" class="text-body-2 mb-2">
              <v-icon icon="mdi-email" size="small" /> {{ hotel.email }}
            </p>
            <p v-if="hotel.phone" class="text-body-2 mb-0">
              <v-icon icon="mdi-phone" size="small" /> {{ hotel.phone }}
            </p>
          </v-card-text>
        </v-card>
      </div>

      <v-divider class="my-8" />

      <div class="section">
        <h2 class="text-h5 mb-4">Vuelos disponibles</h2>
        <p class="text-body-2 text-medium-emphasis mb-4">
          Vuelos hacia {{ cityName }}. Selecciona tu ciudad de origen:
        </p>

        <v-select
          v-model="selectedOrigin"
          :items="origins"
          label="Ciudad de origen"
          variant="outlined"
          density="comfortable"
          class="mb-4"
          prepend-icon="mdi-airplane-takeoff"
          @update:model-value="selectFirstFlight"
        />

        <v-row v-if="filteredFlights.length">
          <v-col cols="12" md="7">
            <FlightCard
              v-for="flight in filteredFlights"
              :key="flight.id"
              :flight="flight"
              :destination-name="cityName"
              :selected="selectedFlight?.id === flight.id"
              @select="selectedFlight = flight"
              class="mb-3"
            />
          </v-col>
          <v-col cols="12" md="5">
            <div class="section-inner">
              <h3 class="text-subtitle-1 font-weight-bold mb-3">Fecha de tu estancia</h3>
              <v-row>
                <v-col cols="6">
                  <v-text-field v-model="checkIn" label="Entrada" type="date" density="compact" />
                </v-col>
                <v-col cols="6">
                  <v-text-field v-model="checkOut" label="Salida" type="date" density="compact" />
                </v-col>
              </v-row>
              <p class="text-caption text-medium-emphasis mb-4">
                {{ nights }} {{ nights === 1 ? 'noche' : 'noches' }} de estancia
              </p>
              <TripSummary
                :hotel-price-per-night="hotel.pricePerNight"
                :flight-price="selectedFlight?.price"
                :nights="nights"
                :origin-short="originCode"
              />
            </div>
          </v-col>
        </v-row>

        <v-alert v-else-if="!flightsLoading" type="info">
          No hay vuelos disponibles hacia {{ cityName }}.
        </v-alert>
        <v-skeleton-loader v-else type="card" class="mt-2" />
      </div>
    </template>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import HotelService from '../../../../services/HotelService';
import FlightService from '../../../../services/FlightService';
import DestinationService from '../../../../services/DestinationService';
import { Hotel } from '../../../../models/Hotel';
import { Flight } from '../../../../models/Flight';
import { Destination } from '../../../../models/Destination';
import {
  hotelGallery,
  formatPrice,
  formatRating,
} from '../../../../utils/images';
import FlightCard from '../../../../components/cards/FlightCard.vue';
import TripSummary from '../../../../components/trip/TripSummary.vue';
import { absoluteUrl, canonicalUrl, setSeo } from '../../../../utils/seo';

const route = useRoute();
const router = useRouter();

const hotel = ref<Hotel | null>(null);
const destination = ref<Destination | null>(null);
const flights = ref<Flight[]>([]);
const selectedFlight = ref<Flight | null>(null);
const selectedOrigin = ref<string | null>(null);
const loading = ref(false);
const flightsLoading = ref(false);
const error = ref<string | null>(null);

const today = new Date();
const checkIn = ref(defaultDate(today, 7));
const checkOut = ref(defaultDate(today, 11));

const slug = computed(() => String(route.params.slug ?? ''));
const hotelId = computed(() => Number(route.params.hotelId));
const cityName = computed(() => destination.value?.name || slug.value);
const gallery = computed(() => hotelGallery(hotel.value));
const mainImageAlt = computed(() => imageAlt('fachada o vista principal'));
const secondaryImageAlt = computed(() => imageAlt('zona común'));
const roomImageAlt = computed(() => imageAlt('habitación'));

const origins = computed(() => [...new Set(flights.value.map((f) => f.origin).filter(Boolean))] as string[]);

const filteredFlights = computed(() =>
  selectedOrigin.value
    ? flights.value.filter((f) => f.origin === selectedOrigin.value)
    : flights.value,
);

const originCode = computed(() => {
  if (!selectedFlight.value?.origin) return '';
  const match = selectedFlight.value.origin.match(/\(([^)]+)\)/);
  return match ? match[1] : selectedFlight.value.origin;
});

const services = computed(() => {
  const h = hotel.value;
  if (!h) return [];
  return [
    { icon: 'mdi-wifi', label: 'WiFi gratis', on: h.freeWifi },
    { icon: 'mdi-food-variant', label: 'Desayuno incluido', on: h.breakfastIncluded },
    { icon: 'mdi-swim', label: 'Piscina', on: h.pool },
    { icon: 'mdi-dumbbell', label: 'Gimnasio', on: h.gym },
    { icon: 'mdi-spa', label: 'Spa', on: h.spa },
    { icon: 'mdi-silverware-fork-knife', label: 'Restaurante', on: h.restaurant },
    { icon: 'mdi-bed', label: 'Room service', on: h.roomService },
    { icon: 'mdi-washing-machine', label: 'Lavandería', on: h.laundryService },
    { icon: 'mdi-bus', label: 'Shuttle aeropuerto', on: h.airportShuttle },
    { icon: 'mdi-parking', label: 'Parking', on: h.parking },
  ].filter((s) => s.on);
});

const nights = computed(() => {
  const start = new Date(checkIn.value);
  const end = new Date(checkOut.value);
  if (isNaN(start.getTime()) || isNaN(end.getTime())) return 0;
  const diff = Math.round((end.getTime() - start.getTime()) / 86400000);
  return diff > 0 ? diff : 0;
});

watch(hotelId, load);
onMounted(load);

async function load() {
  loading.value = true;
  error.value = null;
  try {
    const h = await HotelService.getById(hotelId.value);
    hotel.value = h;
    if (h.destinationId != null) {
      destination.value = await DestinationService.getById(h.destinationId);
      flightsLoading.value = true;
      flights.value = await FlightService.getByDestinationId(h.destinationId);
      selectedOrigin.value = origins.value[0] ?? null;
      selectFirstFlight();
    }
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar el hotel';
  } finally {
    loading.value = false;
    flightsLoading.value = false;
    updateSeo();
  }
}

function updateSeo() {
  const h = hotel.value;
  const path = `/destinos/${slug.value}/hoteles/${hotelId.value}`;
  if (!h) {
    setSeo({
      title: 'Detalle de hotel',
      description: 'Consulta servicios, precios, ubicación y vuelos disponibles para completar tu viaje.',
      path,
    });
    return;
  }

  const title = `${h.name} en ${cityName.value}`;
  const description = h.description
    ? truncate(h.description, 155)
    : `Consulta servicios, precio por noche y vuelos disponibles para alojarte en ${h.name} durante tu viaje a ${cityName.value}.`;

  setSeo({
    title,
    description,
    path,
    image: gallery.value[0],
    structuredData: [
      {
        '@context': 'https://schema.org',
        '@type': 'BreadcrumbList',
        itemListElement: [
          { '@type': 'ListItem', position: 1, name: 'Inicio', item: canonicalUrl('/') },
          { '@type': 'ListItem', position: 2, name: 'Destinos', item: canonicalUrl('/destinos') },
          { '@type': 'ListItem', position: 3, name: cityName.value, item: canonicalUrl(`/destinos/${slug.value}/hoteles`) },
          { '@type': 'ListItem', position: 4, name: h.name, item: canonicalUrl(path) },
        ],
      },
      {
        '@context': 'https://schema.org',
        '@type': 'Hotel',
        name: h.name,
        description,
        image: absoluteUrl(gallery.value[0]),
        url: canonicalUrl(path),
        address: h.address || h.location,
        priceRange: h.pricePerNight != null ? `${h.pricePerNight} EUR por noche` : undefined,
        aggregateRating: h.rating
          ? {
            '@type': 'AggregateRating',
            ratingValue: h.rating,
            bestRating: 5,
          }
          : undefined,
      },
    ],
  });
}

function selectFirstFlight() {
  selectedFlight.value = filteredFlights.value[0] ?? null;
}

function defaultDate(base: Date, offsetDays: number): string {
  const d = new Date(base.getTime() + offsetDays * 86400000);
  return d.toISOString().slice(0, 10);
}

function imageAlt(kind: string) {
  const name = hotel.value?.name || 'hotel';
  return `Imagen de ${kind} de ${name} en ${cityName.value}`;
}

function truncate(value: string, maxLength: number) {
  return value.length > maxLength ? `${value.slice(0, maxLength - 3)}...` : value;
}

const goBack = () => router.push(`/destinos/${slug.value}/hoteles`);

function openAccess() {
  const url = hotel.value?.externalUrl;
  if (!url) return;
  if (url.startsWith('/')) {
    router.push(url);
    return;
  }
  window.open(url, '_blank', 'noopener,noreferrer');
}
</script>

<style scoped>
.hotel-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.detail-title {
  font-size: clamp(1.8rem, 8vw, 3rem);
  line-height: 1.1;
  overflow-wrap: anywhere;
}

.gallery :deep(.v-img) {
  width: 100%;
}

.detail-actions {
  flex-wrap: wrap;
}

.detail-actions :deep(.v-btn) {
  min-height: 44px;
}

@media (max-width: 600px) {
  .detail-actions :deep(.v-btn) {
    width: 100%;
  }
}

.section {
  margin-top: clamp(24px, 7vw, 40px);
}

.section-inner {
  position: static;
}

.detail-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
}

@media (min-width: 768px) {
  .detail-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (min-width: 960px) {
  .section-inner {
    position: sticky;
    top: 20px;
  }
}
</style>
