<template>
  <v-card rounded="xl" elevation="4" color="surface-variant">
    <v-card-title class="font-weight-bold d-flex align-center">
      <v-icon icon="mdi-calculator" class="mr-2" /> Resumen del viaje
    </v-card-title>
    <v-card-text>
      <v-table density="comfortable">
        <tbody>
          <tr>
            <td class="text-body-2">Hotel ({{ nights }} {{ nights === 1 ? 'noche' : 'noches' }})</td>
            <td class="text-right font-weight-medium">
              {{ formatPrice(hotelPricePerNight) }} × {{ nights }} = {{ formatPrice(hotelTotal) }}
            </td>
          </tr>
          <tr>
            <td class="text-body-2">Vuelo ida y vuelta ({{ originShort }})</td>
            <td class="text-right font-weight-medium">
              {{ formatPrice(flightPrice) }} × 2 = {{ formatPrice(roundTripTotal) }}
            </td>
          </tr>
          <tr>
            <td class="text-h6 font-weight-bold">Total estimado</td>
            <td class="text-right text-h6 font-weight-bold text-primary">{{ formatPrice(total) }}</td>
          </tr>
        </tbody>
      </v-table>
      <p class="text-caption text-medium-emphasis mt-2 mb-0">
        * Precio aproximado. La reserva final puede variar según disponibilidad.
      </p>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { formatPrice } from '../../utils/images';

const props = defineProps<{
  hotelPricePerNight?: number;
  flightPrice?: number;
  nights: number;
  originShort?: string;
}>();

const hotelTotal = computed(() => (props.hotelPricePerNight ?? 0) * props.nights);
const roundTripTotal = computed(() => (props.flightPrice ?? 0) * 2);
const total = computed(() => hotelTotal.value + roundTripTotal.value);
</script>