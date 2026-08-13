import api from './axiosConfig';

const API_BASE_URL = '/medications';

const medicationApi = {
  getAllMedications: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getMedicationById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createMedication: async (medicationData) => {
    const response = await api.post(API_BASE_URL, medicationData);
    return response.data;
  },

  updateMedication: async (id, medicationData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, medicationData);
    return response.data;
  },

  deleteMedication: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default medicationApi;