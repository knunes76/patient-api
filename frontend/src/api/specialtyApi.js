import api from './axiosConfig';

const API_BASE_URL = '/specialty';

const specialtyApi = {
  getAllSpecialties: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getSpecialtyById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createSpecialty: async (specialtyData) => {
    const response = await api.post(API_BASE_URL, specialtyData);
    return response.data;
  },

  updateSpecialty: async (id, specialtyData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, specialtyData);
    return response.data;
  },

  deleteSpecialty: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default specialtyApi;