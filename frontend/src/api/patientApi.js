import api from './axiosConfig';

const API_BASE_URL = '/patients';

const patientApi = {
  getAllPatients: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getPatientsByUnity: async (unityId) => {
    const response = await api.get(`${API_BASE_URL}/unity/${unityId}`);
    return response.data;
  },

  getPatientById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  getPatientByCpf: async (cpf) => {
    const response = await api.get(`${API_BASE_URL}/cpf/${cpf}`);
    return response.data;
  },

  searchPatients: async (name) => {
    const response = await api.get(`${API_BASE_URL}/search`, {
      params: { name }
    });
    return response.data;
  },

  createPatient: async (patientData) => {
    const response = await api.post(API_BASE_URL, patientData);
    return response.data;
  },

  updatePatient: async (id, patientData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, patientData);
    return response.data;
  },

  deletePatient: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default patientApi;
