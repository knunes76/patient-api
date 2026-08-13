import api from './axiosConfig';

const API_BASE_URL = '/exam-results';

const examResultApi = {
  getExamResultsByPatient: async (patientId) => {
    const response = await api.get(`${API_BASE_URL}/patient/${patientId}`);
    return response.data;
  },

  getExamResultById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createExamResult: async (examResultData) => {
    const response = await api.post(API_BASE_URL, examResultData);
    return response.data;
  },

  updateExamResult: async (id, examResultData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, examResultData);
    return response.data;
  },

  deleteExamResult: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default examResultApi;