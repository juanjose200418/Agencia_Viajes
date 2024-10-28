import axios, { AxiosError } from 'axios';
import { API_URL } from '../config/Config';
import { Destination } from '../models/Destination';

interface ErrorResponse {
    message: string;
}

class DestinationService { 
    static async create(destination: Destination): Promise<Destination> {
        try {
            const response = await axios.post<Destination>(`${API_URL}/destinations`, destination);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al crear destinos: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getAll(): Promise<Destination[]> {
        try {
            const response = await axios.get<Destination[]>(`${API_URL}/destinations`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al ver todos los destinos: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getById(id: number): Promise<Destination> {
        try {
            const response = await axios.get<Destination>(`${API_URL}/destinations/${id}`);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener el destino por su ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async update(id: number, destination: Destination): Promise<Destination> {
        try {
            const response = await axios.put<Destination>(`${API_URL}/destinations/${id}`, destination);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al actualizar el destino con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async deleteById(id: number): Promise<{ message: string }> {
        try {
            await axios.delete(`${API_URL}/destinations/${id}`);
            return { message: 'destino eliminado con éxito' };
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al eliminar el destino con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
}    

export default DestinationService;
