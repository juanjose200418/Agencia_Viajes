<template>
  <div class="user-management">
    <UserManager :can-delete-users="isSuperAdmin" :can-assign-super-admin="isSuperAdmin" />
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import UserManager from '../components/admin/UserManager.vue';
import { User } from '../models/User';
import { Role } from '../models/Role';

const currentUser = computed<User | null>(() => {
  const raw = localStorage.getItem('user');
  if (!raw) return null;
  try {
    return JSON.parse(raw) as User;
  } catch {
    return null;
  }
});

const isSuperAdmin = computed(() => currentUser.value?.role === Role.SUPER_ADMIN);
</script>

<style scoped>
.user-management {
  max-width: 1280px;
  margin: 0 auto;
  padding: clamp(12px, 4vw, 20px);
}
</style>
