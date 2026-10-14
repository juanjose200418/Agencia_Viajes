// src/services/DestinationService.ts
import api from './api'; // importamos la instancia con token
import { Destination } from '../models/Destination';
import { AxiosError } from 'axios';
import { PageResponse } from '../models/PageResponse';

interface ErrorResponse {
  message: string;
}

export interface DestinationFilterParams {
  page?: number;
  size?: number;
  search?: string;
  country?: string;
  location?: string;
  hasImage?: boolean;
  minAverageCost?: number;
  maxAverageCost?: number;
  hotelPresence?: string;
  sortBy?: string;
}

class DestinationService {

  static async create(destination: Destination): Promise<Destination> {
    try {
      const response = await api.post<Destination>('/destinations', destination);
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al crear destinos: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async getAll(): Promise<Destination[]> {
    try {
      const response = await api.get<Destination[]>('/destinations');
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al ver todos los destinos: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async getPage(page = 0, size = 50, filters: DestinationFilterParams = {}): Promise<PageResponse<Destination>> {
    try {
      const params = { ...filters, page, size };
      const response = await api.get<PageResponse<Destination>>('/destinations', { params });
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al paginar destinos: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async getFilterOptions(): Promise<{ countries: string[]; locations: string[] }> {
    try {
      const response = await api.get<{ countries: string[]; locations: string[] }>('/destinations/filter-options');
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al cargar filtros de destinos: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async getById(id: number): Promise<Destination> {
    try {
      const response = await api.get<Destination>(`/destinations/${id}`);
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener el destino por su ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async getBySlug(slug: string): Promise<Destination> {
    try {
      const response = await api.get<Destination>(`/destinations/slug/${slug}`);
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener el destino por su slug ${slug}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async update(id: number, destination: Destination): Promise<Destination> {
    try {
      const response = await api.put<Destination>(`/destinations/${id}`, destination);
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al actualizar el destino con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async deleteById(id: number): Promise<{ message: string }> {
    try {
      await api.delete(`/destinations/${id}`);
      return { message: 'destino eliminado con éxito' };
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al eliminar el destino con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }
}

export default DestinationService;
