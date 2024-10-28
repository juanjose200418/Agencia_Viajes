import axios, { AxiosError } from 'axios';
import { API_URL } from '../config/Config';
import { Destination } from '../models/Destination';

interface ErrorResponse {
    message: string;
}
class DestinationService { 
    static async create(destination: Destination): Promise<Destination>{
        try{
            const response = await axios.post<Destination>(`${API_URL}/destinations`, destination);
            return response.data;
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al crear destinion: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
}