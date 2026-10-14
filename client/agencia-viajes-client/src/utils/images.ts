import { API_URL } from '../config/Config';

export function slugify(value: string): string {
  return value
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/ñ/g, 'n')
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

export function normalizeImageUrl(url?: string): string {
  if (!url) return '';
  if (url.startsWith('/')) return `${API_URL}${url}`;
  return url;
}

type DestinationLike = {
  id?: number;
  name?: string;
  slug?: string;
  country?: string;
  imageUrl?: string;
  pointsOfInterest?: string[];
};

type HotelLike = {
  id?: number;
  name?: string;
  imageUrl?: string;
};

const destinationQueries: Record<string, string> = {
  amsterdam: 'amsterdam canals netherlands',
  athens: 'athens acropolis greece',
  bali: 'bali beach temple indonesia',
  bangkok: 'bangkok grand palace thailand',
  barcelona: 'barcelona sagrada familia spain',
  berlin: 'berlin brandenburg gate germany',
  bogota: 'bogota monserrate colombia',
  'buenos-aires': 'buenos aires obelisco argentina',
  cairo: 'cairo pyramids egypt',
  cancun: 'cancun beach mexico',
  'cape-coast': 'cape coast ghana castle',
  'cape-town': 'cape town table mountain south africa',
  cartagena: 'cartagena colombia old town',
  'ciudad-de-mexico': 'mexico city zocalo mexico',
  delhi: 'delhi india red fort',
  dublin: 'dublin temple bar ireland',
  dubrovnik: 'dubrovnik croatia old town',
  dubai: 'dubai burj khalifa',
  'dubai-marina': 'dubai marina skyline',
  edinburgh: 'edinburgh castle scotland',
  florence: 'florence duomo italy',
  goa: 'goa beach india',
  hanoi: 'hanoi vietnam old quarter',
  havana: 'havana cuba old cars',
  helsinki: 'helsinki cathedral finland',
  istanbul: 'istanbul hagia sophia turkey',
  kingston: 'kingston jamaica beach',
  kyoto: 'kyoto fushimi inari japan',
  lagos: 'lagos nigeria skyline',
  lima: 'lima peru miraflores',
  lisbon: 'lisbon portugal tram',
  london: 'london tower bridge',
  madrid: 'madrid gran via spain',
  male: 'maldives beach overwater villas',
  marrakech: 'marrakech morocco souk',
  melbourne: 'melbourne australia skyline',
  moscow: 'moscow red square russia',
  munich: 'munich marienplatz germany',
  murcia: 'murcia cathedral spain',
  mykonos: 'mykonos greece windmills',
  nagoya: 'nagoya castle japan',
  nairobi: 'nairobi kenya safari',
  oporto: 'porto portugal ribeira bridge',
  osaka: 'osaka castle japan',
  oslo: 'oslo norway opera house',
  paris: 'paris eiffel tower france',
  pekin: 'beijing forbidden city china',
  phuket: 'phuket thailand beach',
  prague: 'prague charles bridge czech republic',
  quito: 'quito ecuador historic center',
  reykjavik: 'reykjavik iceland northern lights',
  'reykjavik-south': 'iceland black sand beach waterfall',
  'reunion-island': 'reunion island volcano',
  'rio-de-janeiro': 'rio de janeiro christ redeemer brazil',
  rome: 'rome colosseum italy',
  'san-francisco': 'san francisco golden gate bridge',
  santiago: 'santiago chile andes',
  santorini: 'santorini greece blue domes',
  'sao-paulo': 'sao paulo brazil skyline',
  seoul: 'seoul gyeongbokgung palace korea',
  sevilla: 'seville plaza de espana spain',
  shanghai: 'shanghai skyline bund china',
  singapore: 'singapore marina bay sands',
  sidney: 'sydney opera house australia',
  toronto: 'toronto cn tower canada',
  valparaiso: 'valparaiso chile colorful houses',
  vancouver: 'vancouver canada mountains skyline',
  venice: 'venice grand canal italy',
  vienna: 'vienna opera austria',
  zurich: 'zurich switzerland old town',
};

export function destinationImage(destination?: DestinationLike | string | number | null, width = 800, height = 600): string {
  if (typeof destination === 'object' && destination?.imageUrl) return normalizeImageUrl(destination.imageUrl);
  const slug = typeof destination === 'object' && destination
    ? destination.slug || (destination.name ? slugify(destination.name) : undefined)
    : typeof destination === 'string'
      ? slugify(destination)
      : undefined;
  const query = slug
    ? destinationQueries[slug] || [slug.replace(/-/g, ' '), 'travel landmark'].join(' ')
    : 'travel landmark city';
  const tags = query.split(/\s+/).filter(Boolean).join(',');
  const lock = hashCode(slug || query);

  return `https://loremflickr.com/${width}/${height}/${encodeURIComponent(tags)}?lock=${lock}`;
}

function hashCode(value: string): number {
  return Math.abs(
    value.split('').reduce((hash, char) => ((hash << 5) - hash + char.charCodeAt(0)) | 0, 0),
  );
}

export function countryImage(slug?: string, width = 600, height = 400): string {
  return `https://picsum.photos/seed/country-${slug ?? 'x'}/${width}/${height}`;
}

export function hotelImage(hotel?: HotelLike | number | null, width = 600, height = 400): string {
  if (typeof hotel === 'object' && hotel?.imageUrl) return normalizeImageUrl(hotel.imageUrl);
  const id = typeof hotel === 'object' ? hotel?.id : hotel;
  return `https://loremflickr.com/${width}/${height}/hotel,resort,travel?lock=${id ?? 'x'}`;
}

export function hotelGallery(hotel?: HotelLike | number | null, count = 4): string[] {
  const imageUrl = typeof hotel === 'object' ? normalizeImageUrl(hotel?.imageUrl) : undefined;
  const id = typeof hotel === 'object' ? hotel?.id : hotel;
  const generated = Array.from({ length: count }, (_, i) =>
    `https://loremflickr.com/800/500/hotel,resort,room?lock=${id ?? 'x'}${i + 1}`,
  );
  return imageUrl ? [imageUrl, ...generated].slice(0, count) : generated;
}

export function flightImage(id?: number, width = 600, height = 400): string {
  return `https://picsum.photos/seed/flight-${id ?? 'x'}/${width}/${height}`;
}

export function formatPrice(value?: number): string {
  if (value == null) return '—';
  return `${value.toLocaleString('es-ES', { maximumFractionDigits: 0 })} €`;
}

export function formatRating(value?: number): string {
  if (value == null) return '—';
  return value.toFixed(1);
}

export function formatDuration(minutes?: number): string {
  if (minutes == null) return '—';
  const h = Math.floor(minutes / 60);
  const m = Math.round(minutes % 60);
  return h > 0 ? `${h}h ${m}m` : `${m}m`;
}

export function formatFlightDate(value?: string): string {
  if (!value) return '—';
  const d = new Date(value);
  if (isNaN(d.getTime())) return value;
  return d.toLocaleString('es-ES', {
    day: '2-digit',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit',
  });
}
