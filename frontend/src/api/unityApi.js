import api from './axiosConfig';

const API_BASE_URL = '/unity';

const unityApi = {
  getAllUnities: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getUnityById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createUnity: async (unityData) => {
    const response = await api.post(API_BASE_URL, unityData);
    return response.data;
  },

  updateUnity: async (id, unityData) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, unityData);
    return response.data;
  },

  deleteUnity: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default unityApi;