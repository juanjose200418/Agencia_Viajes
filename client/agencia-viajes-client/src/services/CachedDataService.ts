import DestinationService from './DestinationService';
import HotelService from './HotelService';
import { Destination } from '../models/Destination';
import { Hotel } from '../models/Hotel';

const TTL = 5 * 60 * 1000;

let destinationsCache: { data: Destination[]; timestamp: number } | null = null;
let hotelsCache: { data: Hotel[]; timestamp: number } | null = null;

function isFresh(cache: { timestamp: number } | null) {
  return !!cache && Date.now() - cache.timestamp < TTL;
}

class CachedDataService {
  static async getDestinations(force = false): Promise<Destination[]> {
    if (!force && isFresh(destinationsCache)) return destinationsCache!.data;
    const data = await DestinationService.getAll();
    destinationsCache = { data, timestamp: Date.now() };
    return data;
  }

  static async getHotels(force = false): Promise<Hotel[]> {
    if (!force && isFresh(hotelsCache)) return hotelsCache!.data;
    const data = await HotelService.getAll();
    hotelsCache = { data, timestamp: Date.now() };
    return data;
  }

  static invalidateDestinations() {
    destinationsCache = null;
  }

  static invalidateHotels() {
    hotelsCache = null;
  }

  static invalidateAll() {
    destinationsCache = null;
    hotelsCache = null;
  }
}

export default CachedDataService;
