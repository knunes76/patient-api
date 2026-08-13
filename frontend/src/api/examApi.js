import api from './axiosConfig';

const API_BASE_URL = '/exams';

const examApi = {
  getAllExams: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getExamById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createExam: async (examData) => {
    const response = await api.post(API_BASE_URL, examData);
    return response.data;
  },

  updateExam: async (id, examData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, examData);
    return response.data;
  },

  deleteExam: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default examApi;