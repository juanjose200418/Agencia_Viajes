import { AxiosError } from 'axios';
import api from './api';

interface ErrorResponse {
  message: string;
}

class UploadService {
  static async uploadImage(file: File): Promise<string> {
    try {
      const formData = new FormData();
      formData.append('file', file);
      const response = await api.post<{ url: string }>('/uploads/images', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      return response.data.url;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(`Error al subir imagen: ${axiosError.response?.data?.message || axiosError.message}`);
    }
  }
}

export default UploadService;
