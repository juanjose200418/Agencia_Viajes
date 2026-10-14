<template>
  <div class="hotels-page">
    <div class="mb-4 d-flex align-center">
      <v-btn variant="text" prepend-icon="mdi-arrow-left" @click="goBack">
        Ver todos los destinos
      </v-btn>
    </div>

    <h1 class="page-title mb-2" v-if="cityName">Hoteles en {{ cityName }}</h1>
    <p class="text-subtitle-1 mb-6 text-medium-emphasis">Elige el alojamiento perfecto para tu viaje.</p>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <v-row v-if="hotels.length">
      <v-col v-for="hotel in hotels" :key="hotel.id" cols="12" sm="6" md="4">
        <HotelCard :hotel="hotel" @select="goToHotel" />
      </v-col>
    </v-row>

    <v-skeleton-loader v-else-if="loading" type="card, card, card" class="mt-4" />
    <v-alert v-else-if="!loading && !error" type="info" class="mt-4">
      No hay hoteles disponibles en {{ cityName }} por ahora.
    </v-alert>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DestinationService from '../../../services/DestinationService';
import HotelService from '../../../services/HotelService';
import { Destination } from '../../../models/Destination';
import { Hotel } from '../../../models/Hotel';
import HotelCard from '../../../components/cards/HotelCard.vue';
import { canonicalUrl, setSeo } from '../../../utils/seo';

const route = useRoute();
const router = useRouter();

const destination = ref<Destination | null>(null);
const hotels = ref<Hotel[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const slug = computed(() => String(route.params.slug ?? ''));
const cityName = computed(() => destination.value?.name || slug.value);

watch(slug, load);
onMounted(load);

async function load() {
  loading.value = true;
  error.value = null;
  hotels.value = [];
  destination.value = null;
  try {
    const dest = await DestinationService.getBySlug(slug.value);
    destination.value = dest;
    if (dest.id != null) {
      hotels.value = await HotelService.getByDestinationId(dest.id);
    }
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar hoteles';
  } finally {
    loading.value = false;
    updateSeo();
  }
}

function updateSeo() {
  const name = cityName.value;
  const path = `/destinos/${slug.value}/hoteles`;
  setSeo({
    title: `Hoteles en ${name}`,
    description: `Compara hoteles en ${name}, revisa precios por noche, valoraciones y servicios para elegir tu alojamiento ideal.`,
    path,
    structuredData: [
      {
        '@context': 'https://schema.org',
        '@type': 'BreadcrumbList',
        itemListElement: [
          { '@type': 'ListItem', position: 1, name: 'Inicio', item: canonicalUrl('/') },
          { '@type': 'ListItem', position: 2, name: 'Destinos', item: canonicalUrl('/destinos') },
          { '@type': 'ListItem', position: 3, name, item: canonicalUrl(path) },
        ],
      },
      {
        '@context': 'https://schema.org',
        '@type': 'CollectionPage',
        name: `Hoteles en ${name}`,
        description: `Hoteles disponibles para viajar a ${name}.`,
        url: canonicalUrl(path),
        mainEntity: {
          '@type': 'ItemList',
          itemListElement: hotels.value.slice(0, 20).map((hotel, index) => ({
            '@type': 'ListItem',
            position: index + 1,
            name: hotel.name,
            url: hotel.id != null ? canonicalUrl(`${path}/${hotel.id}`) : canonicalUrl(path),
          })),
        },
      },
    ],
  });
}

const goToHotel = (hotel: Hotel) => {
  if (hotel.id != null) {
    router.push(`/destinos/${slug.value}/hoteles/${hotel.id}`);
  }
};
const goBack = () => router.push('/destinos');
</script>

<style scoped>
.hotels-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.page-title {
  font-size: clamp(1.7rem, 7vw, 2.4rem);
  line-height: 1.12;
}
</style>
