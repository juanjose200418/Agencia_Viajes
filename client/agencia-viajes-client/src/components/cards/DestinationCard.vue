<template>
  <v-card
    class="destination-card"
    rounded="xl"
    elevation="4"
    @click="$emit('select', destination)"
  >
    <v-img :src="destinationImage(destination, 600, 400)" :alt="destinationAlt" lazy-src="data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==" cover class="destination-img">
      <template v-slot:error>
        <div class="image-fallback">Imagen no disponible</div>
      </template>
      <div class="img-overlay" />
      <div class="img-content">
        <v-chip v-if="destination.averageCost" color="accent" size="small" class="mb-2">
          {{ formatPrice(destination.averageCost) }}
        </v-chip>
        <h3 class="destination-title font-weight-bold text-white">{{ destination.name }}</h3>
        <p class="text-subtitle-2 text-white mt-1 mb-0">
          <v-icon icon="mdi-map-marker" size="small" /> {{ destination.country || destination.location }}
        </p>
      </div>
    </v-img>
  </v-card>
</template>

<script lang="ts" setup>
import { Destination } from '../../models/Destination';
import { destinationImage, formatPrice } from '../../utils/images';
import { computed } from 'vue';

const props = defineProps<{
  destination: Destination;
}>();

defineEmits<{
  (e: 'select', destination: Destination): void;
}>();

const destinationAlt = computed(() => {
  const name = props.destination.name || 'destino de viaje';
  const location = props.destination.country || props.destination.location;
  return location ? `Vista de ${name}, ${location}` : `Vista de ${name}`;
});
</script>

<style scoped>
.destination-card {
  cursor: pointer;
  overflow: hidden;
  height: 100%;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.destination-img {
  width: 100%;
  height: clamp(220px, 58vw, 270px);
}

.destination-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.28) !important;
}

.img-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.75), rgba(0, 0, 0, 0.1));
}

.img-content {
  position: absolute;
  left: 20px;
  right: 20px;
  bottom: 20px;
  z-index: 1;
}

.destination-title {
  font-size: clamp(1.2rem, 5vw, 1.5rem);
  line-height: 1.15;
  overflow-wrap: anywhere;
}

.image-fallback {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(36, 54, 78, 0.95), rgba(18, 26, 38, 0.95));
  color: #fff;
}

@media (min-width: 768px) {
  .destination-img {
    height: 240px;
  }
}
</style>
