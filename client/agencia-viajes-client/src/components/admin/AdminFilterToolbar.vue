<template>
  <v-card class="filter-toolbar mb-4" rounded="xl" variant="tonal">
    <v-card-text>
      <div class="filter-main">
        <v-text-field
          :model-value="search"
          :placeholder="placeholder"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="comfortable"
          hide-details
          clearable
          class="filter-search"
          @update:model-value="$emit('update:search', String($event ?? ''))"
        />
        <v-btn variant="tonal" prepend-icon="mdi-tune" class="filter-button" @click="dialogOpen = true">
          Filtros<span v-if="activeFilters.length"> ({{ activeFilters.length }})</span>
        </v-btn>
      </div>

      <div class="filter-summary mt-3">
        <span class="text-caption text-medium-emphasis">{{ resultsText }}</span>
        <v-btn v-if="activeFilters.length" size="small" variant="text" @click="$emit('clear-all')">
          Limpiar todos
        </v-btn>
      </div>

      <div v-if="activeFilters.length" class="active-filters mt-3">
        <span class="text-caption text-medium-emphasis">Filtros activos:</span>
        <v-chip
          v-for="filter in activeFilters"
          :key="filter.key"
          closable
          size="small"
          color="primary"
          variant="tonal"
          @click:close="$emit('clear-filter', filter.key)"
        >
          {{ filter.label }}
        </v-chip>
      </div>
    </v-card-text>
  </v-card>

  <v-dialog v-model="dialogOpen" max-width="720" :fullscreen="mobile">
    <v-card>
      <v-card-title class="d-flex align-center justify-space-between">
        <span>Filtros</span>
        <v-btn icon="mdi-close" variant="text" @click="dialogOpen = false" />
      </v-card-title>
      <v-divider />
      <v-card-text>
        <slot />
      </v-card-text>
      <v-card-actions class="filter-actions">
        <v-btn variant="text" @click="$emit('clear-all')">Limpiar</v-btn>
        <v-spacer />
        <v-btn color="primary" @click="dialogOpen = false">Aplicar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue';
import { useDisplay } from 'vuetify';

defineProps<{
  search: string;
  placeholder: string;
  resultsText: string;
  activeFilters: Array<{ key: string; label: string }>;
}>();

defineEmits<{
  (e: 'update:search', value: string): void;
  (e: 'clear-filter', key: string): void;
  (e: 'clear-all'): void;
}>();

const dialogOpen = ref(false);
const { smAndDown } = useDisplay();
const mobile = computed(() => smAndDown.value);
</script>

<style scoped>
.filter-toolbar {
  border: 1px solid rgba(var(--v-theme-primary), 0.16);
}

.filter-main,
.filter-summary,
.active-filters {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}

.filter-search {
  flex: 1 1 260px;
  min-width: 0;
}

.filter-button {
  min-height: 44px;
}

.active-filters {
  gap: 8px;
}

.filter-actions {
  flex-wrap: wrap;
}

@media (max-width: 600px) {
  .filter-main {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-button {
    width: 100%;
  }
}
</style>
