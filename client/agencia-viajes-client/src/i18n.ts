import { computed, ref } from 'vue';

export type Language = 'en' | 'es' | 'fr' | 'it';

const stored = localStorage.getItem('language') as Language | null;
export const language = ref<Language>(stored || 'en');

export const languageItems = [
  { title: 'EN', value: 'en' },
  { title: 'ES', value: 'es' },
  { title: 'FR', value: 'fr' },
  { title: 'IT', value: 'it' },
];

const messages: Record<Language, Record<string, string>> = {
  en: {
    home: 'Home', aiSearch: 'AI Search', destinations: 'Destinations', hotels: 'Hotels', findHotels: 'Find hotels by destination', account: 'Account', admin: 'Admin Panel', superadmin: 'SuperAdmin Panel', users: 'User management', logout: 'Logout', login: 'Login', register: 'Register', menu: 'Menu', session: 'Signed in', hello: 'Hello', explore: 'Explore', company: 'Company', welcome: 'Welcome to our Trip and Tour Agency', footerText: 'Find your travel deal with us.',
  },
  es: {
    home: 'Inicio', aiSearch: 'Búsqueda IA', destinations: 'Destinos', hotels: 'Hoteles', findHotels: 'Buscar hoteles por destino', account: 'Cuenta', admin: 'Panel Admin', superadmin: 'Panel SuperAdmin', users: 'Gestión de usuarios', logout: 'Cerrar sesión', login: 'Login', register: 'Registro', menu: 'Menú', session: 'Sesión iniciada', hello: 'Hola', explore: 'Explorar', company: 'Compañía', welcome: 'Welcome to our Trip and Tour Agency', footerText: 'Encuentra tu chollo de viaje con nosotros.',
  },
  fr: {
    home: 'Accueil', aiSearch: 'Recherche IA', destinations: 'Destinations', hotels: 'Hôtels', findHotels: 'Trouver des hôtels par destination', account: 'Compte', admin: 'Panneau Admin', superadmin: 'Panneau SuperAdmin', users: 'Gestion des utilisateurs', logout: 'Déconnexion', login: 'Connexion', register: 'Inscription', menu: 'Menu', session: 'Session ouverte', hello: 'Bonjour', explore: 'Explorer', company: 'Entreprise', welcome: 'Bienvenue dans notre agence de voyages', footerText: 'Trouvez votre offre de voyage avec nous.',
  },
  it: {
    home: 'Home', aiSearch: 'Ricerca IA', destinations: 'Destinazioni', hotels: 'Hotel', findHotels: 'Trova hotel per destinazione', account: 'Account', admin: 'Pannello Admin', superadmin: 'Pannello SuperAdmin', users: 'Gestione utenti', logout: 'Logout', login: 'Login', register: 'Registrati', menu: 'Menu', session: 'Sessione attiva', hello: 'Ciao', explore: 'Esplora', company: 'Azienda', welcome: 'Benvenuto nella nostra agenzia viaggi', footerText: 'Trova la tua offerta di viaggio con noi.',
  },
};

export function setLanguage(value: Language) {
  language.value = value;
  localStorage.setItem('language', value);
}

export function useI18n() {
  const t = computed(() => (key: string) => messages[language.value][key] || messages.en[key] || key);
  return { language, languageItems, setLanguage, t };
}
