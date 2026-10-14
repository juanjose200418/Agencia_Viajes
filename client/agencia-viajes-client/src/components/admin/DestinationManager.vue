<template>
  <v-card rounded="xl" class="mb-6">
    <v-card-title class="manager-title">
      <span>Gestión de destinos</span>
      <v-btn color="primary" prepend-icon="mdi-plus" @click="openCreate">Nuevo destino</v-btn>
    </v-card-title>
    <v-card-text>
      <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

      <AdminFilterToolbar
        v-model:search="search"
        placeholder="Buscar destino, país, ubicación o slug..."
        :results-text="`${totalElements} destinos encontrados`"
        :active-filters="activeFilters"
        @clear-filter="clearFilter"
        @clear-all="clearAllFilters"
      >
        <v-row>
          <v-col cols="12" md="6">
            <v-select v-model="country" :items="countries" label="País" variant="outlined" clearable @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="6">
            <v-select v-model="location" :items="locations" label="Ubicación" variant="outlined" clearable @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model.number="minAverageCost" label="Coste mínimo" type="number" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model.number="maxAverageCost" label="Coste máximo" type="number" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="4">
            <v-select v-model="imageFilter" :items="imageOptions" label="Imagen" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="4">
            <v-select v-model="hotelPresence" :items="hotelPresenceOptions" label="Número de hoteles" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="4">
            <v-select v-model="sortBy" :items="sortOptions" label="Ordenar por" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
        </v-row>
      </AdminFilterToolbar>

      <v-alert v-if="!destinations.length && !error" type="info" variant="tonal" class="mb-4">
        No hemos encontrado destinos con estos filtros.
        <v-btn class="mt-2" variant="text" @click="clearAllFilters">Limpiar filtros</v-btn>
      </v-alert>

      <div v-if="destinations.length" class="table-scroll">
      <v-table>
        <thead>
          <tr>
            <th>Ciudad / destino</th>
            <th>País</th>
            <th>URL key</th>
            <th>Coste medio</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="destination in destinations" :key="destination.id">
            <td>{{ destination.name }}</td>
            <td>{{ destination.country }}</td>
            <td>{{ destination.slug }}</td>
            <td>{{ destination.averageCost ? `${destination.averageCost} €` : '—' }}</td>
            <td class="actions">
              <v-btn size="small" variant="text" @click="openEdit(destination)">Editar</v-btn>
              <v-btn size="small" variant="text" color="error" @click="deleteDestination(destination)">Eliminar</v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
      </div>
      <div class="pager">
        <span class="text-caption">Mostrando {{ rangeStart }}-{{ rangeEnd }} de {{ totalElements }}</span>
        <v-select v-model="pageSize" :items="pageSizeOptions" density="compact" hide-details label="Por página" class="page-size" @update:model-value="changePageSize" />
        <v-btn size="small" variant="tonal" :disabled="page === 0" @click="changePage(page - 1)">Anterior</v-btn>
        <span class="text-caption">Página {{ page + 1 }} / {{ Math.max(totalPages, 1) }}</span>
        <v-btn size="small" variant="tonal" :disabled="page + 1 >= totalPages" @click="changePage(page + 1)">Siguiente</v-btn>
      </div>
    </v-card-text>
  </v-card>

  <v-dialog v-model="dialogOpen" max-width="760" :fullscreen="mobile">
    <v-card>
      <v-card-title>{{ editingId ? 'Editar destino' : 'Nuevo destino' }}</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.name" label="Nombre / ciudad" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.country" label="País" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.location" label="Ubicación" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model.number="form.averageCost" label="Coste medio" type="number" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
             <v-text-field v-model="form.slug" label="URL key (optional)" variant="outlined" />
          </v-col>
          <v-col cols="12">
            <ImageUploader v-model="form.imageUrl" title="Imagen del destino" />
          </v-col>
          <v-col cols="12">
            <v-textarea v-model="form.description" label="Descripción" rows="3" variant="outlined" />
          </v-col>
          <v-col cols="12">
            <div class="text-subtitle-2 mb-2">Puntos de interés</div>
            <div v-for="(_, index) in form.pointsOfInterest" :key="index" class="interest-row mb-2">
              <v-text-field v-model="form.pointsOfInterest![index]" label="Punto de interés" variant="outlined" density="comfortable" hide-details />
              <v-btn icon="mdi-close" variant="text" color="error" @click="removePoint(index)" />
            </div>
            <v-btn variant="tonal" prepend-icon="mdi-plus" @click="addPoint">Añadir punto de interés</v-btn>
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="dialogOpen = false">Cancelar</v-btn>
        <v-btn color="primary" :loading="saving" @click="saveDestination">Guardar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useDisplay } from 'vuetify';
import DestinationService from '../../services/DestinationService';
import CachedDataService from '../../services/CachedDataService';
import { Destination } from '../../models/Destination';
import ImageUploader from './ImageUploader.vue';
import AdminFilterToolbar from './AdminFilterToolbar.vue';

const route = useRoute();
const router = useRouter();
const destinations = ref<Destination[]>([]);
const form = ref<Destination>(emptyForm());
const editingId = ref<number | null>(null);
const dialogOpen = ref(false);
const saving = ref(false);
const error = ref<string | null>(null);
const page = ref(numberQuery('d_page', 0) ?? 0);
const pageSize = ref(numberQuery('d_size', 20) ?? 20);
const totalPages = ref(0);
const totalElements = ref(0);
const { smAndDown } = useDisplay();
const mobile = computed(() => smAndDown.value);
const search = ref(stringQuery('d_search'));
const country = ref(stringQuery('d_country'));
const location = ref(stringQuery('d_location'));
const minAverageCost = ref<number | undefined>(numberQuery('d_minCost', undefined));
const maxAverageCost = ref<number | undefined>(numberQuery('d_maxCost', undefined));
const imageFilter = ref(stringQuery('d_image', 'all'));
const hotelPresence = ref(stringQuery('d_hotels', 'ALL'));
const sortBy = ref(stringQuery('d_sort', 'NAME_ASC'));
const countries = ref<string[]>([]);
const locations = ref<string[]>([]);
const pageSizeOptions = [20, 50, 100];
const imageOptions = [
  { title: 'Todos', value: 'all' },
  { title: 'Con imagen', value: 'true' },
  { title: 'Sin imagen', value: 'false' },
];
const hotelPresenceOptions = [
  { title: 'Todos', value: 'ALL' },
  { title: 'Con hoteles', value: 'WITH_HOTELS' },
  { title: 'Sin hoteles', value: 'WITHOUT_HOTELS' },
];
const sortOptions = [
  { title: 'Nombre A-Z', value: 'NAME_ASC' },
  { title: 'Nombre Z-A', value: 'NAME_DESC' },
  { title: 'País', value: 'COUNTRY_ASC' },
  { title: 'Coste menor → mayor', value: 'COST_ASC' },
  { title: 'Coste mayor → menor', value: 'COST_DESC' },
  { title: 'Más recientes', value: 'CREATED_DESC' },
  { title: 'Más antiguos', value: 'CREATED_ASC' },
];
let searchTimer: ReturnType<typeof setTimeout> | undefined;

onMounted(async () => {
  await loadFilterOptions();
  await loadDestinations();
});

watch(search, () => {
  if (searchTimer) clearTimeout(searchTimer);
  searchTimer = setTimeout(() => applyFilters(), 300);
});

function emptyForm(): Destination {
  return {
    name: '',
    slug: '',
    country: '',
    location: '',
    description: '',
    imageUrl: '',
    pointsOfInterest: [],
    averageCost: undefined,
  };
}

async function loadDestinations() {
  try {
    syncQuery();
    const result = await DestinationService.getPage(page.value, pageSize.value, filterParams());
    destinations.value = result.content;
    totalPages.value = result.totalPages;
    totalElements.value = result.totalElements;
    error.value = null;
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar destinos';
  }
}

async function loadFilterOptions() {
  const options = await DestinationService.getFilterOptions();
  countries.value = options.countries;
  locations.value = options.locations;
}

function changePage(nextPage: number) {
  page.value = nextPage;
  loadDestinations();
}

function changePageSize() {
  page.value = 0;
  loadDestinations();
}

function applyFilters() {
  page.value = 0;
  loadDestinations();
}

function openCreate() {
  editingId.value = null;
  form.value = emptyForm();
  dialogOpen.value = true;
}

function openEdit(destination: Destination) {
  editingId.value = destination.id ?? null;
  form.value = { ...destination, pointsOfInterest: [...(destination.pointsOfInterest ?? [])] };
  dialogOpen.value = true;
}

async function saveDestination() {
  saving.value = true;
  try {
    if (editingId.value) {
      await DestinationService.update(editingId.value, { ...form.value, id: editingId.value });
    } else {
      await DestinationService.create(form.value);
    }
    CachedDataService.invalidateDestinations();
    dialogOpen.value = false;
    await loadDestinations();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al guardar destino';
  } finally {
    saving.value = false;
  }
}

async function deleteDestination(destination: Destination) {
  if (!destination.id) return;
  if (!confirm(`¿Eliminar destino ${destination.name}?`)) return;
  try {
    await DestinationService.deleteById(destination.id);
    CachedDataService.invalidateDestinations();
    await loadDestinations();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al eliminar destino';
  }
}

function addPoint() {
  form.value.pointsOfInterest = [...(form.value.pointsOfInterest ?? []), ''];
}

function removePoint(index: number) {
  form.value.pointsOfInterest = (form.value.pointsOfInterest ?? []).filter((_, i) => i !== index);
}

function filterParams() {
  return {
    search: search.value || undefined,
    country: country.value || undefined,
    location: location.value || undefined,
    hasImage: imageFilter.value === 'all' ? undefined : imageFilter.value === 'true',
    minAverageCost: minAverageCost.value,
    maxAverageCost: maxAverageCost.value,
    hotelPresence: hotelPresence.value === 'ALL' ? undefined : hotelPresence.value,
    sortBy: sortBy.value,
  };
}

const activeFilters = computed(() => {
  const chips: Array<{ key: string; label: string }> = [];
  if (search.value) chips.push({ key: 'search', label: search.value });
  if (country.value) chips.push({ key: 'country', label: country.value });
  if (location.value) chips.push({ key: 'location', label: location.value });
  if (minAverageCost.value != null || maxAverageCost.value != null) chips.push({ key: 'cost', label: `${minAverageCost.value ?? '0'}€ - ${maxAverageCost.value ?? '∞'}€` });
  if (imageFilter.value !== 'all') chips.push({ key: 'image', label: imageFilter.value === 'true' ? 'Con imagen' : 'Sin imagen' });
  if (hotelPresence.value !== 'ALL') chips.push({ key: 'hotels', label: hotelPresence.value === 'WITH_HOTELS' ? 'Con hoteles' : 'Sin hoteles' });
  if (sortBy.value !== 'NAME_ASC') chips.push({ key: 'sort', label: sortOptions.find((o) => o.value === sortBy.value)?.title ?? sortBy.value });
  return chips;
});

const rangeStart = computed(() => (totalElements.value ? page.value * pageSize.value + 1 : 0));
const rangeEnd = computed(() => Math.min((page.value + 1) * pageSize.value, totalElements.value));

function clearFilter(key: string) {
  if (key === 'search') search.value = '';
  if (key === 'country') country.value = '';
  if (key === 'location') location.value = '';
  if (key === 'cost') { minAverageCost.value = undefined; maxAverageCost.value = undefined; }
  if (key === 'image') imageFilter.value = 'all';
  if (key === 'hotels') hotelPresence.value = 'ALL';
  if (key === 'sort') sortBy.value = 'NAME_ASC';
  applyFilters();
}

function clearAllFilters() {
  search.value = '';
  country.value = '';
  location.value = '';
  minAverageCost.value = undefined;
  maxAverageCost.value = undefined;
  imageFilter.value = 'all';
  hotelPresence.value = 'ALL';
  sortBy.value = 'NAME_ASC';
  applyFilters();
}

function syncQuery() {
  router.replace({
    query: {
      ...route.query,
      d_search: search.value || undefined,
      d_country: country.value || undefined,
      d_location: location.value || undefined,
      d_minCost: minAverageCost.value ?? undefined,
      d_maxCost: maxAverageCost.value ?? undefined,
      d_image: imageFilter.value !== 'all' ? imageFilter.value : undefined,
      d_hotels: hotelPresence.value !== 'ALL' ? hotelPresence.value : undefined,
      d_sort: sortBy.value !== 'NAME_ASC' ? sortBy.value : undefined,
      d_page: page.value || undefined,
      d_size: pageSize.value !== 20 ? pageSize.value : undefined,
    },
  });
}

function stringQuery(key: string, fallback = ''): string {
  const value = route.query[key];
  return typeof value === 'string' ? value : fallback;
}

function numberQuery(key: string, fallback: number | undefined): number | undefined {
  const value = route.query[key];
  if (typeof value !== 'string' || value === '') return fallback;
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
}
</script>

<style scoped>
.actions {
  white-space: nowrap;
}

.manager-title,
.pager {
  display: flex;
  gap: 12px;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
}

.table-scroll {
  width: 100%;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.table-scroll :deep(.v-table) {
  min-width: 720px;
}

.page-size {
  max-width: 132px;
}

.interest-row {
  display: flex;
  gap: 8px;
  align-items: center;
}

@media (max-width: 600px) {
  .manager-title :deep(.v-btn),
  .pager :deep(.v-btn) {
    width: 100%;
    min-height: 42px;
  }
}
</style>
