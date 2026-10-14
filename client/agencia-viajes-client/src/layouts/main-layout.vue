<template>
  <div>
    <v-app-bar color="primary" density="comfortable">
      <v-app-bar-nav-icon
        class="d-md-none"
        aria-label="Abrir menú de navegación"
        @click="mobileMenu = true"
      />
      <v-app-bar-title class="header-title" @click="home">
        EncuentraTuChollo.com
      </v-app-bar-title>

      <template v-slot:append>
        <div class="desktop-nav d-none d-md-flex align-center">
        <v-btn variant="text" @click="home">{{ t('home') }}</v-btn>

        <v-btn variant="text" prepend-icon="mdi-auto-fix" @click="goToSearch">{{ t('aiSearch') }}</v-btn>

        <v-menu open-on-hover>
          <template v-slot:activator="{ props }">
            <v-btn v-bind="props" variant="text">{{ t('destinations') }}</v-btn>
          </template>
          <v-list>
            <v-list-item @click="goTodestinations">{{ t('destinations') }}</v-list-item>
          </v-list>
        </v-menu>

        <v-menu open-on-hover>
          <template v-slot:activator="{ props }">
            <v-btn v-bind="props" variant="text">{{ t('hotels') }}</v-btn>
          </template>
          <v-list>
            <v-list-item @click="goToHotels">{{ t('hotels') }}</v-list-item>
            <v-list-item @click="goToHotelsList">{{ t('findHotels') }}</v-list-item>
          </v-list>
        </v-menu>

        <v-menu open-on-hover>
          <template v-slot:activator="{ props }">
            <v-btn v-bind="props" variant="text" prepend-icon="mdi-account-circle">{{ accountLabel }}</v-btn>
          </template>
          <v-list>
            <template v-if="isAuthenticated">
              <v-list-item v-if="isAdmin" @click="goToAdmin">{{ t('admin') }}</v-list-item>
              <v-list-item v-if="isSuperAdmin" @click="goToSuperAdmin">{{ t('superadmin') }}</v-list-item>
              <v-list-item v-if="isAdmin" @click="goToUsers">{{ t('users') }}</v-list-item>
              <v-list-item @click="logout">{{ t('logout') }}</v-list-item>
            </template>
            <template v-else>
              <v-list-item @click="login">{{ t('login') }}</v-list-item>
              <v-list-item @click="register">{{ t('register') }}</v-list-item>
            </template>
          </v-list>
        </v-menu>
        <v-select
          :model-value="language"
          :items="languageItems"
          density="compact"
          hide-details
          variant="outlined"
          class="language-select"
          @update:model-value="setLanguage"
        />
        </div>
      </template>
    </v-app-bar>

    <v-navigation-drawer
      v-model="mobileMenu"
      temporary
      location="left"
      color="surface"
      class="mobile-drawer d-md-none"
      width="300"
    >
        <v-list nav density="comfortable">
        <v-list-item :title="drawerTitle" :subtitle="drawerSubtitle" class="mb-2" />
        <v-divider class="mb-2" />
        <v-list-item prepend-icon="mdi-home" :title="t('home')" @click="navigate(home)" />
        <v-list-item prepend-icon="mdi-auto-fix" :title="t('aiSearch')" @click="navigate(goToSearch)" />
        <v-list-item prepend-icon="mdi-map-marker" :title="t('destinations')" @click="navigate(goTodestinations)" />
        <v-list-item prepend-icon="mdi-bed" :title="t('hotels')" @click="navigate(goToHotels)" />
        <v-list-item prepend-icon="mdi-format-list-bulleted" :title="t('findHotels')" @click="navigate(goToHotelsList)" />
        <template v-if="isAuthenticated">
          <v-divider class="my-2" />
          <v-list-item v-if="isAdmin" prepend-icon="mdi-shield-account" :title="t('admin')" @click="navigate(goToAdmin)" />
          <v-list-item v-if="isSuperAdmin" prepend-icon="mdi-shield-crown" :title="t('superadmin')" @click="navigate(goToSuperAdmin)" />
          <v-list-item v-if="isAdmin" prepend-icon="mdi-account-group" :title="t('users')" @click="navigate(goToUsers)" />
          <v-list-item prepend-icon="mdi-logout" :title="t('logout')" @click="navigate(logout)" />
        </template>
        <template v-else>
          <v-divider class="my-2" />
          <v-list-item prepend-icon="mdi-login" :title="t('login')" @click="navigate(login)" />
          <v-list-item prepend-icon="mdi-account-plus" :title="t('register')" @click="navigate(register)" />
        </template>
        <v-select :model-value="language" :items="languageItems" label="Language" class="mx-4 mb-3" density="compact" variant="outlined" @update:model-value="setLanguage" />
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-container fluid class="app-container">
        <slot></slot>
      </v-container>
    </v-main>

    <v-footer color="grey lighten-2" class="app-footer">
      <v-container fluid class="app-container">
        <v-row>
          <v-col cols="12" md="4">
            <h4>{{ t('welcome') }}</h4>
            <p>{{ t('footerText') }}</p>
            <p><strong>Tel:</strong> + xx xxx xxx xxxx</p>
            <p><strong>Email:</strong> ejemplo@company.com</p>
          </v-col>
          <v-col cols="12" md="4">
            <h4>Company</h4>
            <ul class="footer-links">
              <li><v-btn variant="text" @click="goToAboutUs">About Us</v-btn></li>
              <li><v-btn variant="text" @click="goToContacts">Contact</v-btn></li>
            </ul>
          </v-col>
          <v-col cols="12" md="4">
            <h4>Explore</h4>
            <ul class="footer-links">
              <li><v-btn variant="text" @click="goTodestinations">Destinations</v-btn></li>
              <li><v-btn variant="text" @click="goToHotels">Hotels</v-btn></li>
            </ul>
          </v-col>
        </v-row>
        <v-divider></v-divider>
        <v-row>
          <v-col cols="12" md="6" class="text-center mx-auto">
            <p class="footer-copy">© All Copyright 2024, EncuentraTuChollo.com</p>
          </v-col>
        </v-row>
      </v-container>
    </v-footer>
  </div>
</template>

<script lang="ts" setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import router from '../router';
import { User } from '../models/User';
import { useI18n } from '../i18n';

const mobileMenu = ref(false);
const currentUser = ref<User | null>(readStoredUser());
const { language, languageItems, setLanguage, t } = useI18n();

const isAuthenticated = computed(() => !!localStorage.getItem('authToken') && !!currentUser.value);

function readStoredUser(): User | null {
  const raw = localStorage.getItem('user');
  if (!raw) return null;
  try {
    return JSON.parse(raw) as User;
  } catch {
    return null;
  }
}

function refreshCurrentUser() {
  currentUser.value = readStoredUser();
}

onMounted(() => {
  refreshCurrentUser();
  window.addEventListener('auth-changed', refreshCurrentUser);
  window.addEventListener('storage', refreshCurrentUser);
});

onBeforeUnmount(() => {
  window.removeEventListener('auth-changed', refreshCurrentUser);
  window.removeEventListener('storage', refreshCurrentUser);
});

const isAdmin = computed(() => {
  const role = currentUser.value?.role;
  return role === 'ADMIN' || role === 'SUPER_ADMIN';
});

const isSuperAdmin = computed(() => currentUser.value?.role === 'SUPER_ADMIN');
const displayName = computed(() => currentUser.value?.firstname?.trim() || currentUser.value?.email || 'Cuenta');
const accountLabel = computed(() => (isAuthenticated.value ? displayName.value : 'Cuenta'));
const drawerTitle = computed(() => (isAuthenticated.value ? `${t.value('hello')}, ${displayName.value}` : 'EncuentraTuChollo.com'));
const drawerSubtitle = computed(() => (isAuthenticated.value ? currentUser.value?.email || t.value('session') : t.value('menu')));

const login = () => router.push('/login');
const register = () => router.push('/register');
const home = () => router.push('/');
const goToSearch = () => router.push('/search');
const goTodestinations = () => router.push('/destinos');
const goToHotels = () => router.push('/hotels');
const goToHotelsList = () => router.push('/listHotel');
const goToUsers = () => router.push('/users');
const goToAdmin = () => router.push('/admin');
const goToSuperAdmin = () => router.push('/superadmin');

const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('user');
  refreshCurrentUser();
  window.dispatchEvent(new Event('auth-changed'));
  router.push('/');
};

const goToAboutUs = () => router.push('/');
const goToContacts = () => router.push('/');

function navigate(action: () => void) {
  action();
  mobileMenu.value = false;
}
</script>

<style scoped>
.header-title {
  cursor: pointer;
  font-size: clamp(0.95rem, 3.8vw, 1.25rem);
  min-width: 0;
  white-space: nowrap;
}

.desktop-nav {
  gap: 4px;
}

.language-select {
  width: 84px;
}

.app-container {
  width: 100%;
  max-width: 1440px;
  margin-inline: auto;
  padding-inline: clamp(12px, 3vw, 24px);
}

.mobile-drawer :deep(.v-list-item-title) {
  white-space: normal;
}

.app-footer {
  margin-top: 32px;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin: 5px 0;
}

.footer-copy {
  margin-top: 20px;
  text-align: center;
  font-size: 12px;
}

@media (max-width: 600px) {
  .app-container {
    padding-inline: 12px;
  }

  .footer-links li :deep(.v-btn) {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>
