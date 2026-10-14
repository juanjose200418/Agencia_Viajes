<template>
  <v-card rounded="xl" class="mb-6">
    <v-card-title class="manager-title">
      <span>Gestión de usuarios</span>
      <v-btn color="primary" prepend-icon="mdi-account-plus" @click="openCreate">Nuevo usuario</v-btn>
    </v-card-title>
    <v-card-text>
      <v-alert v-if="error" type="error" class="mb-4">{{ error }}</v-alert>

      <AdminFilterToolbar
        v-model:search="search"
        placeholder="Buscar por nombre, apellidos o email..."
        :results-text="`${totalElements} usuarios encontrados`"
        :active-filters="activeFilters"
        @clear-filter="clearFilter"
        @clear-all="clearAllFilters"
      >
        <v-row>
          <v-col cols="12" md="4">
            <v-select v-model="roleFilter" :items="roleOptions" label="Rol" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="4">
            <v-text-field v-model="createdFrom" label="Fecha desde" type="date" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12" md="4">
            <v-text-field v-model="createdTo" label="Fecha hasta" type="date" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
          <v-col cols="12">
            <v-select v-model="sortBy" :items="sortOptions" label="Ordenar por" variant="outlined" @update:model-value="applyFilters" />
          </v-col>
        </v-row>
      </AdminFilterToolbar>

      <v-alert v-if="!users.length && !error" type="info" variant="tonal" class="mb-4">
        No se encontraron usuarios que coincidan con la búsqueda.
        <v-btn class="mt-2" variant="text" @click="clearAllFilters">Limpiar filtros</v-btn>
      </v-alert>

      <div v-if="users.length" class="table-scroll">
      <v-table>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Teléfono</th>
            <th>Dirección</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.firstname }} {{ user.lastname }}</td>
            <td>{{ user.email }}</td>
            <td>
              <v-chip size="small">{{ user.role }}</v-chip>
              <v-chip v-if="user.blocked" size="small" color="error" class="ml-1">Blocked</v-chip>
              <v-chip v-if="user.mustChangePassword" size="small" color="warning" class="ml-1">Password reset</v-chip>
            </td>
            <td>{{ user.phone }}</td>
            <td>{{ user.address }}</td>
            <td class="actions">
              <v-btn v-if="canEdit(user)" size="small" variant="text" @click="openEdit(user)">Editar</v-btn>
              <v-btn v-if="canChangeRole(user)" size="small" variant="text" @click="openRoleDialog(user)">Rol</v-btn>
              <v-btn v-if="canResetPassword(user)" size="small" variant="text" @click="openPasswordDialog(user)">Restablecer contraseña</v-btn>
              <v-btn v-if="canBlock(user)" size="small" variant="text" @click="openBlockDialog(user)">{{ user.blocked ? 'Desbloquear' : 'Bloquear' }}</v-btn>
              <v-btn
                v-if="canDelete(user)"
                size="small"
                variant="text"
                color="error"
                @click="openDeleteDialog(user)"
              >
                Eliminar
              </v-btn>
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

  <v-dialog v-model="dialogOpen" max-width="620" :fullscreen="mobile">
    <v-card>
      <v-card-title>{{ editingId ? 'Editar usuario' : 'Nuevo usuario' }}</v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12" md="6">
              <v-text-field v-model="form.firstname" label="Nombre de usuario" variant="outlined" required />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.lastname" label="Apellido" variant="outlined" />
          </v-col>
          <v-col cols="12">
              <v-text-field v-model="form.email" label="Email" variant="outlined" required />
          </v-col>
          <v-col v-if="!editingId" cols="12" md="6">
            <v-select v-model="form.role" :items="roles" label="Rol" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.phone" label="Teléfono" variant="outlined" />
          </v-col>
          <v-col cols="12" md="6">
            <v-text-field v-model="form.address" label="Dirección" variant="outlined" />
          </v-col>
          <v-col v-if="editingRequiresPassword" cols="12">
            <v-alert type="info" variant="tonal" class="mb-3">Changing sensitive administrator data requires your current password.</v-alert>
            <v-text-field v-model="editCurrentPassword" label="Your current password" type="password" variant="outlined" />
          </v-col>
        </v-row>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="dialogOpen = false">Cancelar</v-btn>
        <v-btn color="primary" :loading="saving" @click="saveUser">Guardar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="deleteDialogOpen" max-width="560" :fullscreen="mobile">
    <v-card>
      <v-card-title class="text-error">Eliminar usuario administrativo</v-card-title>
      <v-card-text>
        <p><strong>Nombre:</strong> {{ selectedUser?.firstname }} {{ selectedUser?.lastname }}</p>
        <p><strong>Email:</strong> {{ selectedUser?.email }}</p>
        <v-alert type="warning" variant="tonal" class="my-4">Esta acción es sensible y requiere confirmación explícita y reautenticación.</v-alert>
        <v-text-field v-model="confirmationEmail" label="Escribe exactamente el email del usuario" variant="outlined" />
        <v-text-field v-model="adminPassword" label="Tu contraseña actual" type="password" variant="outlined" />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="deleteDialogOpen = false">Cancelar</v-btn>
        <v-btn color="error" :disabled="confirmationEmail !== selectedUser?.email" :loading="saving" @click="confirmDelete">Eliminar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <v-dialog v-model="sensitiveDialogOpen" max-width="520" :fullscreen="mobile">
    <v-card>
      <v-card-title>{{ sensitiveTitle }}</v-card-title>
      <v-card-text>
        <v-select v-if="sensitiveAction === 'role'" v-model="newRole" :items="roles" label="Nuevo rol" variant="outlined" />
        <v-alert type="info" variant="tonal" class="mb-4">Introduce tu contraseña actual para confirmar la acción.</v-alert>
        <v-text-field v-model="adminPassword" label="Tu contraseña actual" type="password" variant="outlined" />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" @click="sensitiveDialogOpen = false">Cancelar</v-btn>
        <v-btn color="primary" :loading="saving" @click="confirmSensitiveAction">Confirmar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useDisplay } from 'vuetify';
import UserService from '../../services/UserService';
import { User } from '../../models/User';
import { Role } from '../../models/Role';
import AdminFilterToolbar from './AdminFilterToolbar.vue';

const props = defineProps<{
  canDeleteUsers: boolean;
  canAssignSuperAdmin: boolean;
}>();

const route = useRoute();
const router = useRouter();
const users = ref<User[]>([]);
const form = ref<User>(emptyForm());
const editingId = ref<number | null>(null);
const dialogOpen = ref(false);
const deleteDialogOpen = ref(false);
const sensitiveDialogOpen = ref(false);
const selectedUser = ref<User | null>(null);
const confirmationEmail = ref('');
const adminPassword = ref('');
const editCurrentPassword = ref('');
const sensitiveAction = ref<'role' | 'password' | 'block' | null>(null);
const newRole = ref<Role>(Role.USER);
const saving = ref(false);
const error = ref<string | null>(null);
const page = ref(numberQuery('u_page', 0));
const pageSize = ref(numberQuery('u_size', 20));
const totalPages = ref(0);
const totalElements = ref(0);
const { smAndDown } = useDisplay();
const mobile = computed(() => smAndDown.value);
const currentUser = computed<User | null>(() => {
  const raw = localStorage.getItem('user');
  if (!raw) return null;
  try {
    return JSON.parse(raw) as User;
  } catch {
    return null;
  }
});

const roles = computed(() =>
  props.canAssignSuperAdmin ? [Role.USER, Role.ADMIN, Role.SUPER_ADMIN] : [Role.USER],
);
const sensitiveTitle = computed(() => {
  if (sensitiveAction.value === 'role') return 'Cambiar rol';
  if (sensitiveAction.value === 'password') return 'Restablecer contraseña';
  if (sensitiveAction.value === 'block') return selectedUser.value?.blocked ? 'Desbloquear usuario' : 'Bloquear usuario';
  return 'Confirmar acción';
});
const editingRequiresPassword = computed(() => editingId.value != null && (form.value.role === Role.ADMIN || form.value.role === Role.SUPER_ADMIN));
const search = ref(stringQuery('u_search'));
const roleFilter = ref(stringQuery('u_role', 'ALL'));
const createdFrom = ref(stringQuery('u_from'));
const createdTo = ref(stringQuery('u_to'));
const sortBy = ref(stringQuery('u_sort', 'EMAIL_ASC'));
const pageSizeOptions = [20, 50, 100];
const roleOptions = computed(() => [{ title: 'Todos', value: 'ALL' }, ...roles.value.map((role) => ({ title: role, value: role }))]);
const sortOptions = [
  { title: 'Nombre A-Z', value: 'NAME_ASC' },
  { title: 'Nombre Z-A', value: 'NAME_DESC' },
  { title: 'Email', value: 'EMAIL_ASC' },
  { title: 'Más reciente', value: 'CREATED_DESC' },
  { title: 'Más antiguo', value: 'CREATED_ASC' },
];
let searchTimer: ReturnType<typeof setTimeout> | undefined;

onMounted(loadUsers);

watch(search, () => {
  if (searchTimer) clearTimeout(searchTimer);
  searchTimer = setTimeout(() => applyFilters(), 300);
});

function emptyForm(): User {
  return {
    firstname: '',
    lastname: '',
    email: '',
    phone: '',
    address: '',
    role: Role.USER,
  };
}

async function loadUsers() {
  try {
    syncQuery();
    const result = await UserService.getPage(page.value, pageSize.value, filterParams());
    users.value = result.content;
    totalPages.value = result.totalPages;
    totalElements.value = result.totalElements;
    error.value = null;
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al cargar usuarios';
  }
}

function changePage(nextPage: number) {
  page.value = nextPage;
  loadUsers();
}

function changePageSize() {
  page.value = 0;
  loadUsers();
}

function applyFilters() {
  page.value = 0;
  loadUsers();
}

function canEdit(user: User) {
  return props.canAssignSuperAdmin || user.role === Role.USER;
}

function canDelete(user: User) {
  if (user.id === currentUser.value?.id) return false;
  if (currentUser.value?.role === Role.ADMIN) return user.role === Role.USER;
  return props.canDeleteUsers;
}

function canChangeRole(user: User) {
  return props.canAssignSuperAdmin && user.id !== currentUser.value?.id;
}

function canResetPassword(user: User) {
  return currentUser.value?.role === Role.SUPER_ADMIN || user.role === Role.USER;
}

function canBlock(user: User) {
  if (user.id === currentUser.value?.id) return false;
  return currentUser.value?.role === Role.SUPER_ADMIN || user.role === Role.USER;
}

function openCreate() {
  editingId.value = null;
  form.value = emptyForm();
  editCurrentPassword.value = '';
  dialogOpen.value = true;
}

function openEdit(user: User) {
  if (!canEdit(user)) return;
  editingId.value = user.id ?? null;
  form.value = { ...user };
  editCurrentPassword.value = '';
  dialogOpen.value = true;
}

async function saveUser() {
  error.value = validateForm();
  if (error.value) return;
  saving.value = true;
  try {
    const payload = { ...form.value };
    if (editingId.value) {
      await UserService.update(editingId.value, {
        firstname: payload.firstname,
        lastname: payload.lastname,
        email: payload.email,
        phone: payload.phone,
        address: payload.address,
        currentPassword: editingRequiresPassword.value ? editCurrentPassword.value : undefined,
      });
    } else {
      await UserService.create(payload, false);
    }
    dialogOpen.value = false;
    await loadUsers();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al guardar usuario';
  } finally {
    saving.value = false;
  }
}

function openDeleteDialog(user: User) {
  selectedUser.value = user;
  confirmationEmail.value = '';
  adminPassword.value = '';
  deleteDialogOpen.value = true;
}

async function confirmDelete() {
  const user = selectedUser.value;
  if (!user?.id || !canDelete(user)) return;
  try {
    await UserService.deleteSecure(user.id, confirmationEmail.value, adminPassword.value);
    deleteDialogOpen.value = false;
    await loadUsers();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al eliminar usuario';
  }
}

function openRoleDialog(user: User) {
  selectedUser.value = user;
  sensitiveAction.value = 'role';
  newRole.value = user.role ?? Role.USER;
  adminPassword.value = '';
  sensitiveDialogOpen.value = true;
}

function openPasswordDialog(user: User) {
  selectedUser.value = user;
  sensitiveAction.value = 'password';
  adminPassword.value = '';
  sensitiveDialogOpen.value = true;
}

function openBlockDialog(user: User) {
  selectedUser.value = user;
  sensitiveAction.value = 'block';
  adminPassword.value = '';
  sensitiveDialogOpen.value = true;
}

async function confirmSensitiveAction() {
  const user = selectedUser.value;
  if (!user?.id || !sensitiveAction.value) return;
  saving.value = true;
  try {
    if (sensitiveAction.value === 'role') await UserService.changeRole(user.id, newRole.value, adminPassword.value);
    if (sensitiveAction.value === 'password') await UserService.requestPasswordReset(user.id, adminPassword.value);
    if (sensitiveAction.value === 'block') await UserService.setBlocked(user.id, !user.blocked, adminPassword.value);
    sensitiveDialogOpen.value = false;
    await loadUsers();
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Error al confirmar la acción';
  } finally {
    saving.value = false;
  }
}

function validateForm() {
  const firstname = form.value.firstname?.trim();
  const email = form.value.email?.trim();
  if (!firstname) return 'El nombre de usuario es obligatorio';
  if (!email) return 'El email es obligatorio';
  const duplicatedName = users.value.some((user) =>
    user.id !== editingId.value && user.firstname?.trim().toLowerCase() === firstname.toLowerCase(),
  );
  if (duplicatedName) return 'Ese nombre de usuario ya está en uso';
  return null;
}

function filterParams() {
  return {
    search: search.value || undefined,
    role: roleFilter.value === 'ALL' ? undefined : roleFilter.value,
    createdFrom: createdFrom.value || undefined,
    createdTo: createdTo.value || undefined,
    sortBy: sortBy.value,
  };
}

const activeFilters = computed(() => {
  const chips: Array<{ key: string; label: string }> = [];
  if (search.value) chips.push({ key: 'search', label: search.value });
  if (roleFilter.value !== 'ALL') chips.push({ key: 'role', label: roleFilter.value });
  if (createdFrom.value || createdTo.value) chips.push({ key: 'date', label: `${createdFrom.value || 'inicio'} - ${createdTo.value || 'hoy'}` });
  if (sortBy.value !== 'EMAIL_ASC') chips.push({ key: 'sort', label: sortOptions.find((o) => o.value === sortBy.value)?.title ?? sortBy.value });
  return chips;
});

const rangeStart = computed(() => (totalElements.value ? page.value * pageSize.value + 1 : 0));
const rangeEnd = computed(() => Math.min((page.value + 1) * pageSize.value, totalElements.value));

function clearFilter(key: string) {
  if (key === 'search') search.value = '';
  if (key === 'role') roleFilter.value = 'ALL';
  if (key === 'date') { createdFrom.value = ''; createdTo.value = ''; }
  if (key === 'sort') sortBy.value = 'EMAIL_ASC';
  applyFilters();
}

function clearAllFilters() {
  search.value = '';
  roleFilter.value = 'ALL';
  createdFrom.value = '';
  createdTo.value = '';
  sortBy.value = 'EMAIL_ASC';
  applyFilters();
}

function syncQuery() {
  router.replace({
    query: {
      ...route.query,
      u_search: search.value || undefined,
      u_role: roleFilter.value !== 'ALL' ? roleFilter.value : undefined,
      u_from: createdFrom.value || undefined,
      u_to: createdTo.value || undefined,
      u_sort: sortBy.value !== 'EMAIL_ASC' ? sortBy.value : undefined,
      u_page: page.value || undefined,
      u_size: pageSize.value !== 20 ? pageSize.value : undefined,
    },
  });
}

function stringQuery(key: string, fallback = ''): string {
  const value = route.query[key];
  return typeof value === 'string' ? value : fallback;
}

function numberQuery(key: string, fallback: number): number {
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
  min-width: 820px;
}

.page-size {
  max-width: 132px;
}

@media (max-width: 600px) {
  .manager-title :deep(.v-btn),
  .pager :deep(.v-btn) {
    width: 100%;
    min-height: 42px;
  }
}
</style>
