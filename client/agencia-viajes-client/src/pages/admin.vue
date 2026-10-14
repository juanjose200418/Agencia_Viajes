<template>
  <div class="admin-page">
    <v-sheet class="panel-hero mb-6" rounded="xl">
      <v-chip color="primary" class="mb-3">Panel Admin</v-chip>
      <h1 class="panel-title font-weight-bold">Administración</h1>
      <p class="text-subtitle-1 mb-0">
        Gestiona destinos, hoteles y usuarios. Los administradores no pueden editar SUPER_ADMIN, eliminar usuarios ni asignar SUPER_ADMIN.
      </p>
    </v-sheet>

    <v-tabs v-model="tab" color="primary" class="mb-4" show-arrows>
      <v-tab value="destinations">Destinos</v-tab>
      <v-tab value="hotels">Hoteles</v-tab>
      <v-tab value="users">Usuarios</v-tab>
    </v-tabs>

    <DestinationManager v-if="tab === 'destinations'" />
    <HotelManager v-if="tab === 'hotels'" />
    <UserManager v-if="tab === 'users'" :can-delete-users="false" :can-assign-super-admin="false" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import DestinationManager from '../components/admin/DestinationManager.vue';
import HotelManager from '../components/admin/HotelManager.vue';
import UserManager from '../components/admin/UserManager.vue';

const tab = ref('destinations');
</script>

<style scoped>
.admin-page {
  max-width: 1280px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}

.panel-hero {
  padding: clamp(18px, 5vw, 32px);
  background: linear-gradient(135deg, rgba(var(--v-theme-primary), 0.14), rgba(var(--v-theme-secondary), 0.08));
}

.panel-title {
  font-size: clamp(1.8rem, 8vw, 3rem);
  line-height: 1.1;
}
</style>
