<template>
  <v-card
    class="flight-card"
    rounded="xl"
    elevation="2"
    :variant="selected ? 'tonal' : 'elevated'"
    :color="selected ? 'primary' : undefined"
    @click="$emit('select', flight)"
  >
    <v-card-text class="d-flex align-center justify-space-between flex-wrap ga-2">
      <div class="d-flex align-center ga-3">
        <v-avatar color="primary" size="44" rounded="lg">
          <v-icon icon="mdi-airplane" color="white" />
        </v-avatar>
        <div>
          <p class="font-weight-bold mb-0">{{ flight.flightNumber }}</p>
          <p class="text-caption text-medium-emphasis mb-0">{{ flight.airline }}</p>
        </div>
      </div>

      <div class="d-flex align-center ga-2">
        <div class="text-center">
          <p class="text-h6 font-weight-bold mb-0">{{ timeOf(flight.departure) }}</p>
          <p class="text-caption text-medium-emphasis mb-0">{{ originShort(flight.origin) }}</p>
        </div>
        <v-icon icon="mdi-airplane" class="mx-2 text-medium-emphasis" />
        <div class="text-center">
          <p class="text-h6 font-weight-bold mb-0">{{ timeOf(flight.arrival) }}</p>
          <p class="text-caption text-medium-emphasis mb-0">{{ destinationName }}</p>
        </div>
      </div>

      <div class="text-center">
        <v-chip :color="flight.stops ? 'warning' : 'success'" size="small">
          {{ flight.stops ? `${flight.stops} escala${flight.stops > 1 ? 's' : ''}` : 'Directo' }}
        </v-chip>
        <p class="text-caption text-medium-emphasis mt-1 mb-0">{{ formatDuration(flight.duration) }}</p>
      </div>

      <div class="text-right">
        <p class="text-h6 font-weight-bold text-primary mb-0">{{ formatPrice(flight.price) }}</p>
        <p class="text-caption text-medium-emphasis mb-0">{{ formatFlightDate(flight.departure) }}</p>
      </div>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { Flight } from '../../models/Flight';
import { formatPrice, formatDuration, formatFlightDate } from '../../utils/images';

defineProps<{
  flight: Flight;
  destinationName?: string;
  selected?: boolean;
}>();

defineEmits<{
  (e: 'select', flight: Flight): void;
}>();

const timeOf = (value?: string) => {
  if (!value) return '—';
  const d = new Date(value);
  if (isNaN(d.getTime())) return value.slice(11, 16) || value;
  return d.toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' });
};

const originShort = (origin?: string) => {
  if (!origin) return 'Origen';
  const match = origin.match(/\(([^)]+)\)/);
  return match ? match[1] : origin.split(' ')[0];
};
</script>

<style scoped>
.flight-card {
  cursor: pointer;
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.flight-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2) !important;
}
</style>