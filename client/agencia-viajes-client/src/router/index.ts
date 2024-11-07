import { createRouter, createWebHistory } from 'vue-router/auto';
import { routes } from 'vue-router/auto-routes';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

const protectedRoutes = ['/hotels', '/listHotel', '/destinations']; 

protectedRoutes.forEach(route => {
  const routeConfig = router.getRoutes().find(r => r.path === route);
  if (routeConfig) {
    routeConfig.meta = { requiresAuth: true };
  }
});

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken');

  // Verifico si la ruta requiere autenticación
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login'); // Redirigo a login si no está autenticado
  } else {
    next(); 
  }
});

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

export default router;
