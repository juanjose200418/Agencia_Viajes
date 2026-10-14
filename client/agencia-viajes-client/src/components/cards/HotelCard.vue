<template>
  <v-card class="hotel-card" rounded="xl" elevation="4" @click="$emit('select', hotel)">
    <v-img :src="hotelImage(hotel)" :alt="hotelAlt" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" cover class="card-img">
      <template v-slot:error>
        <div class="image-fallback">Imagen no disponible</div>
      </template>
      <div class="img-overlay" />
      <v-chip v-if="hotel.rating" color="accent" size="small" class="rating-chip">
        <v-icon icon="mdi-star" size="small" /> {{ formatRating(hotel.rating) }}
      </v-chip>
    </v-img>
    <v-card-title class="font-weight-bold pb-0">{{ hotel.name }}</v-card-title>
    <v-card-subtitle v-if="hotel.location">
      <v-icon icon="mdi-map-marker" size="small" /> {{ hotel.location }}
    </v-card-subtitle>
    <v-card-text>
      <p class="text-body-2 text-medium-emphasis description">{{ shortDescription }}</p>
      <p class="mb-1">
        <span class="text-h6 font-weight-bold text-primary">{{ formatPrice(hotel.pricePerNight) }}</span>
        <span class="text-caption text-medium-emphasis"> / noche</span>
      </p>
      <div class="mt-1">
        <v-chip v-if="hotel.freeWifi" size="x-small" class="mr-1 mb-1">WiFi</v-chip>
        <v-chip v-if="hotel.breakfastIncluded" size="x-small" class="mr-1 mb-1">Desayuno</v-chip>
        <v-chip v-if="hotel.pool" size="x-small" class="mr-1 mb-1">Piscina</v-chip>
        <v-chip v-if="hotel.parking" size="x-small" class="mr-1 mb-1">Parking</v-chip>
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn
        v-if="hotel.externalUrl"
        class="access-btn"
        color="secondary"
        variant="tonal"
        prepend-icon="mdi-open-in-new"
        @click.stop="openAccess"
      >
        Acceder
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script lang="ts" setup>
import { Hotel } from '../../models/Hotel';
import { hotelImage, formatPrice, formatRating } from '../../utils/images';
import { useRouter } from 'vue-router';
import { computed } from 'vue';

const props = defineProps<{
  hotel: Hotel;
}>();

defineEmits<{
  (e: 'select', hotel: Hotel): void;
}>();

const router = useRouter();

function openAccess() {
  const url = props.hotel.externalUrl;
  if (!url) return;
  if (url.startsWith('/')) {
    router.push(url);
    return;
  }
  window.open(url, '_blank', 'noopener,noreferrer');
}

const shortDescription = computed(() => {
  const text = props.hotel.description?.trim() ?? '';
  return text.length > 120 ? `${text.slice(0, 117)}...` : text;
});

const hotelAlt = computed(() => {
  const name = props.hotel.name || 'hotel';
  const location = props.hotel.location;
  return location ? `Imagen de ${name} en ${location}` : `Imagen de ${name}`;
});
</script>

<style scoped>
.hotel-card {
  cursor: pointer;
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
  position: relative;
  width: 100%;
  height: clamp(190px, 48vw, 220px);
}

.hotel-card :deep(.v-card-text) {
  flex: 1;
}

.hotel-card :deep(.v-card-actions) {
  padding: 12px 16px 16px;
}

.access-btn {
  min-height: 44px;
  width: 100%;
}

.img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.3), transparent);
}

.rating-chip {
  position: absolute;
  top: 12px;
  right: 12px;
  z-index: 1;
}

.image-fallback {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(60, 60, 60, .95), rgba(24, 24, 24, .95));
  color: #fff;
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
