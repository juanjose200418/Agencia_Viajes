<template>
  <div class="search-page">
    <h1 class="page-title mb-2">
      <v-icon icon="mdi-robot-outline" class="me-1" />
      Buscador inteligente
    </h1>
    <p class="text-subtitle-1 mb-6 text-medium-emphasis">
      Escribe qué buscas (playa, montaña, ciudad, presupuesto, gastronomía…) y la IA te recomienda destinos.
    </p>

    <form class="search-bar mb-6" @submit.prevent="submitSearch">
      <v-text-field
        v-model="query"
        :loading="loading"
        placeholder="Ej: playas paradisíacas para una luna de miel con poco presupuesto"
        variant="outlined"
        hide-details
        rounded="lg"
        clearable
        density="comfortable"
        class="search-field"
      >
        <template v-slot:prepend-inner>
          <v-icon icon="mdi-magnify" />
        </template>
      </v-text-field>
      <v-btn type="submit" color="primary" size="large" rounded="lg" :loading="loading">
        <v-icon icon="mdi-auto-fix" class="me-1" />
        Buscar con IA
      </v-btn>
    </form>

    <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

    <template v-if="!loading && explanation">
      <v-card class="ai-card mb-6" rounded="xl" variant="tonal" color="primary">
        <v-card-text class="d-flex">
          <v-icon icon="mdi-robot-outline" size="x-large" class="me-3" />
          <div>
            <div class="text-subtitle-2 font-weight-bold mb-1">Recomendación de la IA</div>
            <p class="text-body-1 mb-0">{{ explanation }}</p>
          </div>
        </v-card-text>
      </v-card>
    </template>

    <template v-if="results.length">
      <h2 class="text-h5 mb-3">{{ resultsTitle }}</h2>
      <v-row>
        <v-col v-for="dest in results" :key="dest.id" cols="12" sm="6" md="4">
          <DestinationCard :destination="dest" @select="goToDestination" />
        </v-col>
      </v-row>
    </template>

    <v-skeleton-loader v-else-if="loading" type="card, card, card" class="mt-4" />

    <v-alert v-else-if="!loading && !error" type="info" class="mt-4">
      No he encontrado destinos para "{{ query }}". Prueba con otra búsqueda (ej: "playa", "Europa", "ciudad", "cultura").
    </v-alert>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import RecommendationService from '../services/RecommendationService';
import CachedDataService from '../services/CachedDataService';
import { Destination } from '../models/Destination';
import DestinationCard from '../components/cards/DestinationCard.vue';

const route = useRoute();
const router = useRouter();

const query = ref('');
const results = ref<Destination[]>([]);
const explanation = ref('');
const resultsTitle = ref('Destinos recomendados');
const loading = ref(false);
const error = ref<string | null>(null);

const allDestinations = ref<Destination[]>([]);

watch(
  () => route.query.q,
  (q) => {
    query.value = typeof q === 'string' ? q : '';
  },
);

onMounted(async () => {
  if (typeof route.query.q === 'string') query.value = route.query.q;
  await performSearch(query.value);
});

const submitSearch = () => {
  router.replace({ query: { q: query.value } });
  performSearch(query.value);
};

async function performSearch(q: string) {
  loading.value = true;
  error.value = null;
  explanation.value = '';
  results.value = [];
  try {
    if (!allDestinations.value.length) {
      allDestinations.value = await CachedDataService.getDestinations();
    }
    if (!q.trim()) {
      results.value = allDestinations.value;
      explanation.value = 'Mostrando todos los destinos disponibles.';
      resultsTitle.value = 'Todos los destinos';
      return;
    }
    const response = await RecommendationService.search(q.trim());
    explanation.value = response.explanation;
    results.value = response.destinations;
    resultsTitle.value = 'Destinos recomendados';
    if (!results.value.length) {
      const local = localFilter(q.trim());
      if (local.length) {
        results.value = local;
        resultsTitle.value = 'Destinos que coinciden con tu búsqueda';
        explanation.value += ' No encontré una coincidencia exacta con la IA, así que te muestro estas opciones.';
      }
    }
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error en la búsqueda';
    const local = localFilter(q.trim());
    if (local.length) {
      results.value = local;
      resultsTitle.value = 'Destinos que coinciden con tu búsqueda';
      explanation.value = 'No pude contactar con la IA; estos son los destinos que coinciden.';
    }
  } finally {
    loading.value = false;
  }
}

function localFilter(q: string): Destination[] {
  const words = q
    .toLowerCase()
    .split(/\s+/)
    .filter((w) => w.length > 1);
  return allDestinations.value
    .filter((d) => {
      const haystack = [d.name, d.country, d.location, ...(d.pointsOfInterest ?? []), d.description]
        .filter(Boolean)
        .join(' ')
        .toLowerCase();
      return words.some((w) => haystack.includes(w));
    })
    .slice(0, 6);
}

const goToDestination = (dest: Destination) => {
  if (dest.slug) router.push(`/destinos/${dest.slug}/hoteles`);
};
</script>

<style scoped>
.search-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.page-title {
  font-size: clamp(1.8rem, 8vw, 2.5rem);
  line-height: 1.1;
}

.search-bar {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: stretch;
}

.search-field {
  width: 100%;
  flex: 1;
}

.search-bar :deep(.v-btn) {
  min-height: 46px;
}

.ai-card {
  border-left: 4px solid rgb(var(--v-theme-primary));
}

@media (min-width: 768px) {
  .search-bar {
    flex-direction: row;
    align-items: center;
  }
}
</style>
