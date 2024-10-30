import axios, { AxiosError } from 'axios';
import { API_URL } from '../config/Config';
import { Booking } from '../models/Booking';

interface ErrorResponse {
    message: string;
}

class BookingService{
    
    static async create(boolean: Booking): Promise <Booking>{
        try{
            const response = await axios.post<Booking>(`${API_URL}/bookings`, Booking)
            return response.data;
        }catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al crear Bookings: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getAll(): Promise <Booking[]>{
        try{
            const response = await axios.get<Booking[]>(`${API_URL}/bookings`)
            return response.data
        }catch (error){
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener todos los bookings: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }

    static async getById(id: number): Promise <Booking>{
        try{
            const response = await axios.get <Booking>(`${API_URL}/bookings/${id}`)
            return response.data
        }catch (error){
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al obtener el booking por su ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
    static async update(id: number, booking: Booking):Promise <Booking>{
        try{
            const response = await axios.put <Booking>(`${API_URL}/bookings/${id}`, booking)
            return response.data 
        }catch (error){
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al actualizar el bookig ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
    static async deleteById(id: number): Promise<{ message: string }> {
        try {
            await axios.delete(`${API_URL}/bookings/${id}`);
            return { message: 'booking eliminado con éxito' };
        } catch (error) {
            const axiosError = error as AxiosError<ErrorResponse>;
            throw new Error(`Error al eliminar el booking con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
        }
    }
}    

export default BookingService;