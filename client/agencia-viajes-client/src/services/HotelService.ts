import { AxiosError } from 'axios';
import api from './api';
import { Hotel } from '../models/Hotel';
import { PageResponse } from '../models/PageResponse';

interface ErrorResponse {
    message: string;
}

class HotelService {
    static async create(hotel: Hotel): Promise<Hotel> {
        try {
            const response = await api.post<Hotel>('/hotels', hotel);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al crear hotel: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getAll(): Promise<Hotel[]> {
        try {
            const response = await api.get<Hotel[]>('/hotels');
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener hoteles: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getPage(page = 0, size = 50): Promise<PageResponse<Hotel>> {
        try {
            const response = await api.get<PageResponse<Hotel>>('/hotels', { params: { page, size } });
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al paginar hoteles: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getById(id: number): Promise<Hotel> {
        try {
            const response = await api.get<Hotel>(`/hotels/${id}`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener el hotel por su ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async update(id: number, hotel: Hotel): Promise<Hotel> {
        try {
            const response = await api.put<Hotel>(`/hotels/${id}`, hotel);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al actualizar el hotel con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async deleteById(id: number): Promise<{ message: string }> {
        try {
            await api.delete(`/hotels/${id}`);
            return { message: 'Hotel eliminado con éxito' };
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al eliminar el hotel con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getByDestinationId(destinationId: number): Promise<Hotel[]> {
        try {
            const response = await api.get<Hotel[]>(`/destination/${destinationId}/hotels`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener hoteles por destino con ID ${destinationId}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
}

export default HotelService;
