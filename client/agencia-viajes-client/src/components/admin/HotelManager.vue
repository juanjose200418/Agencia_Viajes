<template>
  <v-card rounded="xl" class="mb-6">
    <v-card-title class="manager-title">
      <span>Gestión de hoteles</span>
      <v-btn color="primary" prepend-icon="mdi-plus" @click="openCreate">Nuevo hotel</v-btn>
    </v-card-title>
    <v-card-text>
      <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>
      <div class="table-scroll">
      <v-table>
        <thead>
          <tr>
            <th>Hotel</th>
            <th>Destino</th>
            <th>Precio</th>
            <th>Rating</th>
            <th>Acceso</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="hotel in hotels" :key="hotel.id">
            <td>{{ hotel.name }}</td>
            <td>{{ destinationName(hotel.destinationId) }}</td>
            <td>{{ hotel.pricePerNight ? `${hotel.pricePerNight} €` : '—' }}</td>
            <td>{{ hotel.rating ?? '—' }}</td>
            <td>{{ hotel.externalUrl || '—' }}</td>
            <td class="actions">
              <v-btn size="small" variant="text" @click="openEdit(hotel)">Editar</v-btn>
              <v-btn size="small" variant="text" color="error" @click="deleteHotel(hotel)">Eliminar</v-btn>
            </td>
          </tr>
        </tbody>
      </v-table>
      </div>
      <div class="pager">
        <span class="text-caption">{{ totalElements }} hoteles</span>
        <v-btn size="small" variant="tonal" :disabled="page === 0" @click="changePage(page - 1)">Anterior</v-btn>
        <span class="text-caption">Página {{ page + 1 }} / {{ Math.max(totalPages, 1) }}</span>
        <v-btn size="small" variant="tonal" :disabled="page + 1 >= totalPages" @click="changePage(page + 1)">Siguiente</v-btn>
      </div>
    </v-card-text>
  </v-card>

  <v-dialog v-model="dialogOpen" max-width="900" :fullscreen="mobile">
    <v-card>
      <v-card-title>{{ editingId ? 'Editar hotel' : 'Nuevo hotel' }}</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.name" label="Nombre" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-select
              v-model="form.destinationId"
              :items="destinations"
              item-title="name"
              item-value="id"
              label="Destino"
              variant="outlined"
              :rules="[(v) => !!v || 'El destino es obligatorio']"
            />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model.number="form.pricePerNight" label="Precio por noche" type="number" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model.number="form.rating" label="Valoración" type="number" step="0.1" min="0" max="5" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.address" label="Dirección" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.location" label="Ubicación" variant="outlined" />
          </v-col>
          <v-col cols="12">
            <ImageUploader v-model="form.imageUrl" title="Imagen del hotel" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.externalUrl" label="URL botón Acceder" variant="outlined" placeholder="/ruta-interna o https://..." />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.email" label="Email" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.phone" label="Teléfono" variant="outlined" />
          </v-col>
          <v-col cols="12">
            <v-textarea v-model="form.description" label="Descripción" rows="3" variant="outlined" />
          </v-col>
          <v-col cols="12">
            <v-textarea v-model="form.cancellationPolicy" label="Política de cancelación" rows="2" variant="outlined" />
          </v-col>
          <v-col cols="12">
            <div class="text-subtitle-2 mb-2">Servicios</div>
            <v-checkbox v-model="form.freeWifi" label="WiFi" density="compact" hide-details />
            <v-checkbox v-model="form.breakfastIncluded" label="Desayuno incluido" density="compact" hide-details />
            <v-checkbox v-model="form.pool" label="Piscina" density="compact" hide-details />
            <v-checkbox v-model="form.gym" label="Gimnasio" density="compact" hide-details />
            <v-checkbox v-model="form.spa" label="Spa" density="compact" hide-details />
            <v-checkbox v-model="form.restaurant" label="Restaurante" density="compact" hide-details />
            <v-checkbox v-model="form.parking" label="Parking" density="compact" hide-details />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="dialogOpen = false">Cancelar</v-btn>
        <v-btn color="primary" :loading="saving" @click="saveHotel">Guardar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref } from 'vue';
import { useDisplay } from 'vuetify';
import HotelService from '../../services/HotelService';
import CachedDataService from '../../services/CachedDataService';
import { Destination } from '../../models/Destination';
import { Hotel } from '../../models/Hotel';
import ImageUploader from './ImageUploader.vue';

const destinations = ref<Destination[]>([]);
const hotels = ref<Hotel[]>([]);
const form = ref<Hotel>(emptyForm());
const editingId = ref<number | null>(null);
const dialogOpen = ref(false);
const saving = ref(false);
const error = ref<string | null>(null);
const page = ref(0);
const pageSize = 50;
const totalPages = ref(0);
const totalElements = ref(0);
const { smAndDown } = useDisplay();
const mobile = computed(() => smAndDown.value);

onMounted(loadData);

function emptyForm(): Hotel {
  return {
    name: '',
    destinationId: undefined,
    description: '',
    pricePerNight: undefined,
    rating: undefined,
    imageUrl: '',
    externalUrl: '',
    address: '',
    location: '',
    email: '',
    phone: '',
    cancellationPolicy: '',
    freeWifi: true,
    breakfastIncluded: false,
    pool: false,
    gym: false,
    spa: false,
    restaurant: false,
    parking: false,
    deleted: false,
  };
}

async function loadData() {
  try {
    const [dests, list] = await Promise.all([
      CachedDataService.getDestinations(),
      HotelService.getPage(page.value, pageSize),
    ]);
    destinations.value = dests;
    hotels.value = list.content;
    totalPages.value = list.totalPages;
    totalElements.value = list.totalElements;
    error.value = null;
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar hoteles';
  }
}

async function loadHotelsPage() {
  const list = await HotelService.getPage(page.value, pageSize);
  hotels.value = list.content;
  totalPages.value = list.totalPages;
  totalElements.value = list.totalElements;
}

function changePage(nextPage: number) {
  page.value = nextPage;
  loadHotelsPage().catch((err) => {
    error.value = err instanceof Error ? err.message : 'Error al cargar hoteles';
  });
}

function destinationName(id?: number) {
  const destination = destinations.value.find((d) => d.id === id);
  return destination ? `${destination.country || ''} → ${destination.name}` : '—';
}

function openCreate() {
  editingId.value = null;
  form.value = emptyForm();
  dialogOpen.value = true;
}

function openEdit(hotel: Hotel) {
  editingId.value = hotel.id ?? null;
  form.value = { ...hotel };
  dialogOpen.value = true;
}

async function saveHotel() {
  if (!form.value.destinationId) {
    error.value = 'Debes seleccionar un destino para el hotel';
    return;
  }
  saving.value = true;
  try {
    if (editingId.value) {
      await HotelService.update(editingId.value, { ...form.value, id: editingId.value });
    } else {
      await HotelService.create(form.value);
    }
    CachedDataService.invalidateHotels();
    dialogOpen.value = false;
    await loadData();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al guardar hotel';
  } finally {
    saving.value = false;
  }
}

async function deleteHotel(hotel: Hotel) {
  if (!hotel.id) return;
  if (!confirm(`¿Eliminar hotel ${hotel.name}?`)) return;
  try {
    await HotelService.deleteById(hotel.id);
    CachedDataService.invalidateHotels();
    await loadData();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al eliminar hotel';
  }
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
  min-width: 840px;
}

@media (max-width: 600px) {
  .manager-title :deep(.v-btn),
  .pager :deep(.v-btn) {
    width: 100%;
    min-height: 42px;
  }
}
</style>
