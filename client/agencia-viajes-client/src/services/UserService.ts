import axios, { AxiosError } from 'axios';
import { User } from '../models/User';

// Define la interfaz para la respuesta de error del backend
interface ErrorResponse {
  message: string; 
}

const API_URL = 'http://localhost:8080';

class UserService {
  
  // Crear un nuevo usuario
  static async create(user: User, useRegister: boolean = true): Promise<User> {
  try {
    const endpoint = useRegister ? '/auth/register' : '/users';  
    const response = await axios.post<User>(`${API_URL}${endpoint}`, user);
    return response.data; // Devuelve el usuario creado
  } catch (error) {
    const axiosError = error as AxiosError<ErrorResponse>; // Usa el tipo de error definido
    throw new Error(`Error al crear usuario: ${axiosError.response?.data?.message || axiosError.message}`);
  }
}

  // Obtener todos los usuarios
  static async getAll(): Promise<User[]> {
    try {
      const response = await axios.get<User[]>(API_URL);
      return response.data; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener usuarios: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Obtener un usuario por ID
  static async getById(id: number): Promise<User> {
    try {
      const response = await axios.get<User>(`${API_URL}/${id}`);
      return response.data; // Devuelve el usuario
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Actualizar un usuario
  static async update(id: number, user: User): Promise<User> {
    try {
      const response = await axios.put<User>(`${API_URL}/${id}`, user);
      return response.data; // Devuelve el usuario actualizado
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al actualizar usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Eliminar un usuario
  static async delete(id: number): Promise<{ message: string }> {
    try {
      await axios.delete(`${API_URL}/${id}`);
      return { message: 'Usuario eliminado con éxito' }; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al eliminar usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }
}

export default UserService;
