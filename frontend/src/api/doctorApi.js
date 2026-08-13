import api from './axiosConfig';

const API_BASE_URL = '/doctors';

const doctorApi = {
  getAllDoctors: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getDoctorById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createDoctor: async (doctorData) => {
    const response = await api.post(API_BASE_URL, doctorData);
    return response.data;
  },

  updateDoctor: async (id, doctorData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, doctorData);
    return response.data;
  },

  deleteDoctor: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default doctorApi;