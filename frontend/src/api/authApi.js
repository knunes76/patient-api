import api from './axiosConfig';

const API_BASE_URL = '/auth';

const authApi = {
  login: async (username, password) => {
    const response = await api.post(`${API_BASE_URL}/login`, { username, password });
    return response.data;
  },

  register: async (userData) => {
    const response = await api.post(`${API_BASE_URL}/register`, userData);
    return response.data;
  },

  getUser: async (username) => {
    const response = await api.get(`${API_BASE_URL}/user/${username}`);
    return response.data;
  }
};

export default authApi;