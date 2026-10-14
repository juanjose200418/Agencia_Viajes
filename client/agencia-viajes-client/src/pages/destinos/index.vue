<template>
  <div class="destinos-page">
    <h1 class="page-title mb-2">Descubre destinos</h1>
    <p class="text-subtitle-1 mb-6 text-medium-emphasis">
      Elige un destino y verás sus hoteles, vuelos y fotos al instante.
    </p>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <template v-if="countries.length">
      <div v-for="group in countries" :key="group.country" class="mb-10">
        <h2 class="text-h5 mb-4">{{ group.country }}</h2>
        <v-row>
          <v-col v-for="dest in group.destinations" :key="dest.id" cols="12" sm="6" md="4">
            <DestinationCard :destination="dest" @select="goToDestination" />
          </v-col>
        </v-row>
      </div>
    </template>

    <v-skeleton-loader v-else-if="loading" type="card, card, card" class="mt-4" />
    <p v-if="countries.length === 0 && !loading && !error">No hay destinos disponibles.</p>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import CachedDataService from '../../services/CachedDataService';
import { Destination } from '../../models/Destination';
import DestinationCard from '../../components/cards/DestinationCard.vue';
import { canonicalUrl, setSeo } from '../../utils/seo';

const router = useRouter();

const destinations = ref<Destination[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const countries = computed(() => {
  const map = new Map<string, Destination[]>();
  for (const d of destinations.value) {
    const country = d.country || 'Otros';
    if (!map.has(country)) map.set(country, []);
    map.get(country)!.push(d);
  }
  return [...map.entries()]
    .map(([country, list]) => ({
      country,
      destinations: list.sort((a, b) => (a.name || '').localeCompare(b.name || '')),
    }))
    .sort((a, b) => a.country.localeCompare(b.country));
});

onMounted(load);

async function load() {
  loading.value = true;
  try {
    destinations.value = await CachedDataService.getDestinations();
    error.value = null;
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar destinos';
  } finally {
    loading.value = false;
    updateSeo();
  }
}

function updateSeo() {
  setSeo({
    title: 'Destinos de viaje recomendados',
    description: 'Explora destinos por país y encuentra hoteles, vuelos y puntos de interés para organizar tu próxima escapada.',
    path: '/destinos',
    structuredData: {
      '@context': 'https://schema.org',
      '@type': 'CollectionPage',
      name: 'Destinos de viaje recomendados',
      description: 'Listado de destinos disponibles para planificar viajes con hoteles y vuelos.',
      url: canonicalUrl('/destinos'),
      mainEntity: {
        '@type': 'ItemList',
        itemListElement: destinations.value.slice(0, 20).map((dest, index) => ({
          '@type': 'ListItem',
          position: index + 1,
          name: dest.name,
          url: dest.slug ? canonicalUrl(`/destinos/${dest.slug}/hoteles`) : canonicalUrl('/destinos'),
        })),
      },
    },
  });
}

const goToDestination = (dest: Destination) => {
  if (dest.slug) router.push(`/destinos/${dest.slug}/hoteles`);
};
</script>

<style scoped>
.destinos-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.page-title {
  font-size: clamp(1.8rem, 8vw, 2.5rem);
  line-height: 1.1;
}
</style>
