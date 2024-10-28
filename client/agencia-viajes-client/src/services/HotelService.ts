import axios, { AxiosError } from 'axios';
import { API_URL } from '../config/Config';
import { Hotel } from '../models/Hotel';

interface ErrorResponse {
    message: string;
}

class HotelService {
    static async create(hotel: Hotel): Promise<Hotel> {
        try {
            const response = await axios.post<Hotel>(`${API_URL}/hotels`, hotel);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al crear hotel: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getAll(): Promise<Hotel[]> {
        try {
            const response = await axios.get<Hotel[]>(`${API_URL}/hotels`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener hoteles: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getById(id: number): Promise<Hotel> {
        try {
            const response = await axios.get<Hotel>(`${API_URL}/hotels/${id}`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener el hotel por su ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async update(id: number, hotel: Hotel): Promise<Hotel> {
        try {
            const response = await axios.put<Hotel>(`${API_URL}/hotels/${id}`, hotel);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al actualizar el hotel con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async deleteById(id: number): Promise<{ message: string }> {
        try {
            await axios.delete(`${API_URL}/hotels/${id}`);
            return { message: 'Hotel eliminado con éxito' };
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al eliminar el hotel con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getByDestinationId(destinationId: number): Promise<Hotel[]> {
        try {
            const response = await axios.get<Hotel[]>(`${API_URL}/destination/${destinationId}/hotels`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener hoteles por destino con ID ${destinationId}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
}

export default HotelService;
