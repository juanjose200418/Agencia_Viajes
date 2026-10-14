import { AxiosError } from 'axios';
import api from './api';
import { User } from '../models/User';
import { PageResponse } from '../models/PageResponse';

interface ErrorResponse {
  message: string;
}

export interface UserFilterParams {
  page?: number;
  size?: number;
  search?: string;
  role?: string;
  createdFrom?: string;
  createdTo?: string;
  sortBy?: string;
}

export interface UserUpdatePayload {
  firstname?: string;
  lastname?: string;
  email?: string;
  phone?: string;
  address?: string;
  currentPassword?: string;
}

function errorMessage(error: unknown): string {
  const axiosError = error as AxiosError<ErrorResponse | string>;
  const data = axiosError.response?.data;
  if (typeof data === 'string') return data;
  return data?.message || axiosError.message;
}

class UserService {

  static async create(user: User, useRegister: boolean = true): Promise<User> {
    try {
      const endpoint = useRegister ? '/auth/register' : '/users';
      const response = await api.post<User>(endpoint, user);
      return response.data;
    } catch (error) {
      throw new Error(`Error al crear usuario: ${errorMessage(error)}`);
    }
  }

  static async getAll(): Promise<User[]> {
    try {
      const response = await api.get<User[]>('/users');
      return response.data;
    } catch (error) {
      throw new Error(`Error al obtener usuarios: ${errorMessage(error)}`);
    }
  }

  static async getPage(page = 0, size = 50, filters: UserFilterParams = {}): Promise<PageResponse<User>> {
    try {
      const response = await api.get<PageResponse<User>>('/users', { params: { ...filters, page, size } });
      return response.data;
    } catch (error) {
      throw new Error(`Error al paginar usuarios: ${errorMessage(error)}`);
    }
  }

  static async getById(id: number): Promise<User> {
    try {
      const response = await api.get<User>(`/users/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(`Error al obtener usuario con ID ${id}: ${errorMessage(error)}`);
    }
  }

  static async update(id: number, user: UserUpdatePayload): Promise<User> {
    try {
      const response = await api.put<User>(`/users/${id}`, user);
      return response.data;
    } catch (error) {
      throw new Error(`Error al actualizar usuario con ID ${id}: ${errorMessage(error)}`);
    }
  }

  static async delete(id: number): Promise<{ message: string }> {
    try {
      await api.delete(`/users/${id}`);
      return { message: 'Usuario eliminado con éxito' };
    } catch (error) {
      throw new Error(`Error al eliminar usuario con ID ${id}: ${errorMessage(error)}`);
    }
  }

  static async deleteSecure(id: number, confirmationEmail: string, currentPassword: string): Promise<{ message: string }> {
    try {
      await api.delete(`/users/${id}`, { data: { confirmationEmail, currentPassword } });
      return { message: 'Usuario eliminado con éxito' };
    } catch (error) {
      throw new Error(`Error al eliminar usuario con ID ${id}: ${errorMessage(error)}`);
    }
  }

  static async changeRole(id: number, role: string, currentPassword: string): Promise<User> {
    try {
      const response = await api.put<User>(`/users/${id}/role`, { role, currentPassword });
      return response.data;
    } catch (error) {
      throw new Error(`Error al cambiar rol: ${errorMessage(error)}`);
    }
  }

  static async requestPasswordReset(id: number, currentPassword: string): Promise<{ mustChangePassword: boolean }> {
    try {
      const response = await api.post<{ mustChangePassword: boolean }>(`/users/${id}/password-reset`, { currentPassword });
      return response.data;
    } catch (error) {
      throw new Error(`Error al solicitar restablecimiento de contraseña: ${errorMessage(error)}`);
    }
  }

  static async setBlocked(id: number, blocked: boolean, currentPassword?: string): Promise<User> {
    try {
      const response = await api.put<User>(`/users/${id}/blocked`, { blocked, currentPassword });
      return response.data;
    } catch (error) {
      throw new Error(`Error al actualizar bloqueo: ${errorMessage(error)}`);
    }
  }

  static async login(Credential: { email: string; password: string }): Promise<{ token: string; user: User }> {
    try {
      const response = await api.post<{ token: string; user: User }>('/auth/login', Credential);
      return response.data;
    } catch (error) {
      throw new Error(`Error al iniciar sesión: ${errorMessage(error)}`);
    }
  }
}

export default UserService;
