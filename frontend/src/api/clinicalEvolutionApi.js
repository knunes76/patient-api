import api from './axiosConfig';

const API_BASE_URL = '/clinical-evolution';

const clinicalEvolutionApi = {
  getClinicalEvolutionsByPatient: async (patientId) => {
    const response = await api.get(`${API_BASE_URL}/patient/${patientId}`);
    return response.data;
  },

  getClinicalEvolutionById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createClinicalEvolution: async (evolutionData) => {
    const response = await api.post(API_BASE_URL, evolutionData);
    return response.data;
  },

  updateClinicalEvolution: async (id, evolutionData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, evolutionData);
    return response.data;
  },

  deleteClinicalEvolution: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default clinicalEvolutionApi;