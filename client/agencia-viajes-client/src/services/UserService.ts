import axios, { AxiosError } from 'axios';
import { User } from '../models/User';
import { API_URL } from '../config/Config'; // Cambia la ruta si tu estructura es diferente

// Define la interfaz para la respuesta de error del backend
interface ErrorResponse {
  message: string; 
}

class UserService {

  // Crear un nuevo usuario
  static async create(user: User, useRegister: boolean = true): Promise<User> {
    try {
      const endpoint = useRegister ? '/auth/register' : '/users';  
      const response = await axios.post<User>(`${API_URL}${endpoint}`, user);
      return response.data; // Devuelve el usuario creado
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al crear usuario: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Obtener todos los usuarios
  static async getAll(): Promise<User[]> {
    try {
      const response = await axios.get<User[]>(`${API_URL}/users`);
      return response.data; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener usuarios: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Obtener un usuario por ID
  static async getById(id: number): Promise<User> {
    try {
      const response = await axios.get<User>(`${API_URL}/users/${id}`);
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al obtener usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Actualizar un usuario
  static async update(id: number, user: User): Promise<User> {
    try {
      const response = await axios.put<User>(`${API_URL}/users/${id}`, user);
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al actualizar usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  // Eliminar un usuario
  static async delete(id: number): Promise<{ message: string }> {
    try {
      await axios.delete(`${API_URL}/users/${id}`);
      return { message: 'Usuario eliminado con éxito' }; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al eliminar usuario con ID ${id}: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }

  static async login(Credential: { email: string; password: string }): Promise<{ token: string; user: User }> {
    try {
      const response = await axios.post<{ token: string; user: User }>(`${API_URL}/auth/login`, Credential);
      return response.data; 
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al iniciar sesión: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }
}

export default UserService;
