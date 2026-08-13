import api from './axiosConfig';

const API_BASE_URL = '/cid10';

const cid10Api = {
  getAllCID10: async () => {
    const response = await api.get(API_BASE_URL);
    return response.data;
  },

  getCID10ById: async (id) => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
  },

  createCID10: async (cid10Data) => {
    const response = await api.post(API_BASE_URL, cid10Data);
    return response.data;
  },

  updateCID10: async (id, cid10Data) => {
    const response = await api.put(`${API_BASE_URL}/${id}`, cid10Data);
    return response.data;
  },

  deleteCID10: async (id) => {
    await api.delete(`${API_BASE_URL}/${id}`);
  }
};

export default cid10Api;