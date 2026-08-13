import api from './axiosConfig';

const API_BASE_URL = '/current-medication';

const currentMedicationApi = {
  getCurrentMedicationsByPatient: async (patientId) => {
    const response = await api.get(`${API_BASE_URL}/patient/${patientId}`);
    return response.data;
  },

  getCurrentMedicationById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createCurrentMedication: async (medicationData) => {
    const response = await api.post(API_BASE_URL, medicationData);
    return response.data;
  },

  updateCurrentMedication: async (id, medicationData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, medicationData);
    return response.data;
  },

  deleteCurrentMedication: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default currentMedicationApi;