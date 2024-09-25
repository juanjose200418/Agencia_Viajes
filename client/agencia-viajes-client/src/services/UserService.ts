/*import axios, { AxiosError } from 'axios';
import { User } from '../models/User'; // Asegúrate de ajustar la ruta según la estructura de tu proyecto

// Define la interfaz para la respuesta de error del backend
interface ErrorResponse {
  message: string; // Ajusta según la estructura real de tu respuesta de error
}

// Base URL del backend
const API_URL = 'http://localhost:8080/users';

class UserService {
  
  // Crear un nuevo usuario
  async create(user: User): Promise<User> {
    try {
      const response = await axios.post<User>(API_URL, user);
      return response.data; // Devuelve el usuario creado
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>; // Usa el tipo de error definido
      throw new Error(`Error al crear usuario: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Obtener todos los usuarios
  async getAll(): Promise<User[]> {
    try {
      const response = await axios.get<User[]>(API_URL);
      return response.data; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener usuarios: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Obtener un usuario por ID
  async getById(id: number): Promise<User> {
    try {
      const response = await axios.get<User>(`${API_URL}/${id}`);
      return response.data; // Devuelve el usuario
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Actualizar un usuario
  async update(id: number, user: User): Promise<User> {
    try {
      const response = await axios.put<User>(`${API_URL}/${id}`, user);
      return response.data ; // Devuelve el usuario actualizado
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al actualizar usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  async delete(id: number): Promise<{ message: string }> {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return { message: 'Usuario eliminado con éxito' }; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al eliminar usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }
}

export default new UserService();
*/