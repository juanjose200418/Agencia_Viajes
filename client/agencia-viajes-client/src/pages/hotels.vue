<template>
  <div class="hotels-page">
    <h1 class="page-title mb-2">Hoteles</h1>
    <p class="text-subtitle-1 mb-6 text-medium-emphasis">
      Todos nuestros alojamientos disponibles.
    </p>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <v-row v-if="hotels.length">
      <v-col v-for="hotel in hotels" :key="hotel.id" cols="12" sm="6" md="4" lg="3">
        <HotelCard :hotel="hotel" @select="goToHotel" />
      </v-col>
    </v-row>
    <p v-if="hotels.length === 0 && !loading && !error">No hay hoteles disponibles.</p>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import CachedDataService from '../services/CachedDataService';
import { Hotel } from '../models/Hotel';
import HotelCard from '../components/cards/HotelCard.vue';
import { canonicalUrl, setSeo } from '../utils/seo';

const router = useRouter();

const hotels = ref<Hotel[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const destinationSlugs = ref<Record<number, string>>({});

onMounted(async () => {
  loading.value = true;
  try {
    const [hotelList, dests] = await Promise.all([
      CachedDataService.getHotels(),
      CachedDataService.getDestinations(),
    ]);
    hotels.value = hotelList;
    destinationSlugs.value = Object.fromEntries(
      dests.map((d) => [d.id ?? 0, d.slug ?? '']).filter(([, slug]) => slug),
    );
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar hoteles';
  } finally {
    loading.value = false;
    updateSeo();
  }
});

function updateSeo() {
  setSeo({
    title: 'Hoteles disponibles para tus viajes',
    description: 'Consulta hoteles destacados con precios por noche, valoraciones, servicios y enlaces de acceso.',
    path: '/hotels',
    structuredData: {
      '@context': 'https://schema.org',
      '@type': 'CollectionPage',
      name: 'Hoteles disponibles',
      description: 'Listado de hoteles disponibles para reservar viajes.',
      url: canonicalUrl('/hotels'),
      mainEntity: {
        '@type': 'ItemList',
        itemListElement: hotels.value.slice(0, 20).map((hotel, index) => ({
          '@type': 'ListItem',
          position: index + 1,
          name: hotel.name,
        })),
      },
    },
  });
}

const slugFor = (id?: number) => (id != null ? destinationSlugs.value[id] : '');

const goToHotel = (hotel: Hotel) => {
  const slug = slugFor(hotel.destinationId);
  if (hotel.id != null && slug) router.push(`/destinos/${slug}/hoteles/${hotel.id}`);
};

</script>

<style scoped>
.hotels-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.page-title {
  font-size: clamp(1.8rem, 8vw, 2.5rem);
  line-height: 1.1;
}

</style>
