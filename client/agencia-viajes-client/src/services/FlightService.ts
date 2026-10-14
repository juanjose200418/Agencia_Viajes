import { AxiosError } from 'axios';
import api from './api';
import { Flight } from '../models/Flight';

interface ErrorResponse {
    message: string;
}

class FlightService {
    static async create(flight: Flight): Promise<Flight> {
        try {
            const response = await api.post<Flight>('/flights', flight);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al crear vuelo: ${axiosError.response?.data?.message || axiosError.message}.`);
        }
    }

    static async getAll(): Promise<Flight[]> {
        try {
            const response = await api.get<Flight[]>('/flights');
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener vuelos: ${axiosError.response?.data?.message || axiosError.message}.`);
        }
    }

    static async getById(id: number): Promise<Flight> {
        try {
            const response = await api.get<Flight>(`/flights/${id}`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener vuelo con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}.`);
        }
    }

    static async update(id: number, flight: Flight): Promise<Flight> {
        try {
            const response = await api.put<Flight>(`/flights/${id}`, flight);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al actualizar el vuelo con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}.`);
        }
    }

    static async deleteById(id: number): Promise<{ message: string }> {
        try {
            await api.delete(`/flights/${id}`);
            return { message: 'Vuelo eliminado con éxito.' };
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al eliminar el vuelo con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}.`);
        }
    }

    static async getByDestinationId(destinationId: number): Promise<Flight[]> {
        try {
            const response = await api.get<Flight[]>(`/destination/${destinationId}/flights`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener vuelos por destino con ID ${destinationId}: ${axiosError.response?.data?.message || axiosError.message}.`);
        }
    }
}

export default FlightService;
