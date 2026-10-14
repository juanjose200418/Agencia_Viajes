import { AxiosError } from 'axios';
import api from './api';
import { RecommendationSearchResponse } from '../models/Recommendation';

interface ErrorResponse {
  message: string;
}

class RecommendationService {
  static async search(query: string): Promise<RecommendationSearchResponse> {
    try {
      const response = await api.get<RecommendationSearchResponse>('/recommendations/search', {
        params: { query },
      });
      return response.data;
    } catch (error) {
      const axiosError = error as AxiosError<ErrorResponse>;
      throw new Error(
        `Error en la búsqueda inteligente: ${axiosError.response?.data?.message || axiosError.message}.`,
      );
    }
  }
}

export default RecommendationService;