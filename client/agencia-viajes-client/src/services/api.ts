import axios from 'axios';
import { API_URL } from '../config/Config';

const api = axios.create({
  baseURL: API_URL,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const token = localStorage.getItem('authToken');
    const sessionExpired = error.response?.status === 401 || (error.response?.status === 403 && token && !error.response?.data);
    if (sessionExpired) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      sessionStorage.setItem('authMessage', 'Tu sesión ha caducado. Inicia sesión de nuevo.');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
