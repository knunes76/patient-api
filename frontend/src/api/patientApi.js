import axios from 'axios';

const API_BASE_URL = '/api/patients';

const patientApi = {
  getAllPatients: async (page = 0, size = 10, sort = 'name', direction = 'asc') => {
    const response = await axios.get(API_BASE_URL, {
      params: { page, size, sort, direction }
    });
    return response.data;
  },

  getPatientById: async (id) => {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  getPatientByCpf: async (cpf) => {
    const response = await axios.get(`${API_BASE_URL}/cpf/${cpf}`);
    return response.data;
  },

  searchPatients: async (name) => {
    const response = await axios.get(`${API_BASE_URL}/search`, {
      params: { name }
    });
    return response.data;
  },

  createPatient: async (patientData) => {
    const response = await axios.post(API_BASE_URL, patientData);
    return response.data;
  },

  updatePatient: async (id, patientData) => {
    const response = await axios.put(`${API_BASE_URL}/${id}`, patientData);
    return response.data;
  },

  deletePatient: async (id) => {
    await axios.delete(`${API_BASE_URL}/${id}`);
  }
};

export default patientApi;
