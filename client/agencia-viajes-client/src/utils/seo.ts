import type { RouteLocationNormalizedLoaded, Router } from 'vue-router';

const SITE_NAME = 'EncuentraTuChollo.com';
const DEFAULT_TITLE = `${SITE_NAME} | Hoteles, vuelos y destinos`;
const DEFAULT_DESCRIPTION = 'Encuentra hoteles, vuelos y destinos de viaje al mejor precio con recomendaciones inteligentes para planificar tu próxima escapada.';
const PUBLIC_SITE_URL = (import.meta.env.VITE_PUBLIC_SITE_URL || 'https://www.encuentratuchollo.com').replace(/\/$/, '');

type StructuredData = Record<string, unknown> | Record<string, unknown>[];

type SeoConfig = {
  title?: string;
  description?: string;
  path?: string;
  robots?: string;
  image?: string;
  structuredData?: StructuredData;
};

type RouteSeo = Required<Pick<SeoConfig, 'title' | 'description'>> & Pick<SeoConfig, 'robots'>;

const PRIVATE_PREFIXES = ['/admin', '/superadmin', '/users', '/destinations'];
const NOINDEX_PATHS = new Set(['/login', '/register', '/search']);

export function installSeo(router: Router) {
  router.afterEach((to) => {
    setSeo({
      ...seoForRoute(to),
      path: to.path,
    });
  });
}

export function setSeo(config: SeoConfig) {
  const title = config.title ? withSiteName(config.title) : DEFAULT_TITLE;
  const description = config.description || DEFAULT_DESCRIPTION;
  const canonical = canonicalUrl(config.path);
  const robots = config.robots || 'index, follow, max-image-preview:large';

  document.title = title;
  setMeta('name', 'description', description);
  setMeta('name', 'robots', robots);
  setMeta('property', 'og:title', title);
  setMeta('property', 'og:description', description);
  setMeta('property', 'og:url', canonical);
  setMeta('property', 'og:site_name', SITE_NAME);
  setMeta('property', 'og:type', 'website');
  setMeta('name', 'twitter:title', title);
  setMeta('name', 'twitter:description', description);
  if (config.image) {
    setMeta('property', 'og:image', absoluteUrl(config.image));
    setMeta('name', 'twitter:image', absoluteUrl(config.image));
  }
  setCanonical(canonical);
  setRouteJsonLd(config.structuredData);
}

export function absoluteUrl(pathOrUrl: string) {
  if (/^https?:\/\//i.test(pathOrUrl)) return pathOrUrl;
  const path = pathOrUrl.startsWith('/') ? pathOrUrl : `/${pathOrUrl}`;
  if (typeof window !== 'undefined') return `${window.location.origin}${path}`;
  return `${PUBLIC_SITE_URL}${path}`;
}

export function canonicalUrl(path?: string) {
  const cleanPath = path || (typeof window !== 'undefined' ? window.location.pathname : '/');
  return absoluteUrl(cleanPath === '/' ? '/' : cleanPath.replace(/\/$/, ''));
}

function seoForRoute(to: RouteLocationNormalizedLoaded): RouteSeo {
  const path = to.path;
  const noindex = NOINDEX_PATHS.has(path) || PRIVATE_PREFIXES.some((prefix) => path.startsWith(prefix));
  const robots = noindex ? 'noindex, nofollow' : undefined;

  if (path === '/') {
    return {
      title: 'Chollos de viaje, hoteles y vuelos baratos',
      description: DEFAULT_DESCRIPTION,
      robots,
    };
  }

  if (path === '/destinos') {
    return {
      title: 'Destinos de viaje recomendados',
      description: 'Explora destinos por país y encuentra hoteles, vuelos y puntos de interés para organizar tu próxima escapada.',
      robots,
    };
  }

  if (/^\/destinos\/[^/]+\/hoteles\/[^/]+$/.test(path)) {
    return {
      title: 'Detalle de hotel y vuelos disponibles',
      description: 'Consulta servicios, precios, ubicación y vuelos disponibles para completar tu viaje.',
      robots,
    };
  }

  if (/^\/destinos\/[^/]+\/hoteles$/.test(path)) {
    return {
      title: `Hoteles en ${humanizeParam(String(to.params.slug || 'tu destino'))}`,
      description: 'Compara alojamientos disponibles por destino y accede a opciones de reserva externas.',
      robots,
    };
  }

  if (path === '/hotels') {
    return {
      title: 'Hoteles disponibles para tus viajes',
      description: 'Consulta hoteles destacados con precios por noche, valoraciones, servicios y enlaces de acceso.',
      robots,
    };
  }

  if (path === '/listHotel') {
    return {
      title: 'Buscar hoteles por destino',
      description: 'Filtra hoteles por destino, precio, valoración y servicios para encontrar el alojamiento ideal.',
      robots,
    };
  }

  if (path === '/search') {
    return {
      title: 'Buscador inteligente de destinos',
      description: 'Busca destinos de viaje con recomendaciones inteligentes según presupuesto, estilo y preferencias.',
      robots,
    };
  }

  if (path === '/login') {
    return {
      title: 'Iniciar sesión',
      description: 'Accede a tu cuenta de EncuentraTuChollo.com.',
      robots,
    };
  }

  if (path === '/register') {
    return {
      title: 'Crear cuenta',
      description: 'Crea tu cuenta para descubrir destinos, hoteles y vuelos recomendados.',
      robots,
    };
  }

  return {
    title: DEFAULT_TITLE,
    description: DEFAULT_DESCRIPTION,
    robots,
  };
}

function withSiteName(title: string) {
  return title.includes(SITE_NAME) ? title : `${title} | ${SITE_NAME}`;
}

function humanizeParam(value: string) {
  return value
    .replace(/-/g, ' ')
    .replace(/\b\w/g, (letter) => letter.toUpperCase());
}

function setMeta(attribute: 'name' | 'property', key: string, content: string) {
  let element = document.head.querySelector<HTMLMetaElement>(`meta[${attribute}="${key}"]`);
  if (!element) {
    element = document.createElement('meta');
    element.setAttribute(attribute, key);
    document.head.appendChild(element);
  }
  element.content = content;
}

function setCanonical(href: string) {
  let element = document.head.querySelector<HTMLLinkElement>('link[rel="canonical"]');
  if (!element) {
    element = document.createElement('link');
    element.rel = 'canonical';
    document.head.appendChild(element);
  }
  element.href = href;
}

function setRouteJsonLd(data?: StructuredData) {
  const id = 'route-jsonld';
  const current = document.getElementById(id) as HTMLScriptElement | null;
  if (!data) {
    current?.remove();
    return;
  }

  const element = current || document.createElement('script');
  element.id = id;
  element.type = 'application/ld+json';
  element.textContent = JSON.stringify(data);
  if (!current) document.head.appendChild(element);
}
