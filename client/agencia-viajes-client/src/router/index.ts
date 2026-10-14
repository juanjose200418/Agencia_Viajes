import { createRouter, createWebHistory } from 'vue-router/auto';
import { routes } from 'vue-router/auto-routes';
import { installSeo } from '../utils/seo';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

const protectedRoutes = ['/hotels', '/listHotel', '/destinations', '/destinations/:id', '/users', '/search', '/admin', '/superadmin'];

protectedRoutes.forEach(route => {
  const routeConfig = router.getRoutes().find(r => r.path === route);
  if (routeConfig) {
    routeConfig.meta = { requiresAuth: true };
  }
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken');
  const role = currentRole();

  const requiresAuth =
    to.meta.requiresAuth === true ||
    protectedRoutes.includes(to.path) ||
    to.path.startsWith('/destinos');

  // Verifico si la ruta requiere autenticación
  if (requiresAuth && !isAuthenticated) {
    next('/login'); // Redirigo a login si no está autenticado
  } else if (to.path.startsWith('/superadmin') && role !== 'SUPER_ADMIN') {
    next('/');
  } else if (to.path.startsWith('/admin') && role !== 'ADMIN' && role !== 'SUPER_ADMIN') {
    next('/');
  } else {
    next(); 
  }
});

function currentRole(): string | undefined {
  const raw = localStorage.getItem('user');
  if (!raw) return undefined;
  try {
    return JSON.parse(raw)?.role;
  } catch {
    return undefined;
  }
}

// Manejo de errores
router.onError((err, to) => {
  if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
    if (!localStorage.getItem('vuetify:dynamic-reload')) {
      console.log('Reloading page to fix dynamic import error');
      localStorage.setItem('vuetify:dynamic-reload', 'true');
      location.assign(to.fullPath);
    } else {
      console.error('Dynamic import error, reloading page did not fix it', err);
    }
  } else {
    console.error(err);
  }
});

// Limpiar localStorage al estar listo
router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload');
});

installSeo(router);

export default router;
