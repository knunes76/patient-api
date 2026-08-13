import React, { createContext, useContext, useState, useEffect } from 'react';
import unityApi from '../api/unityApi';

const UnityContext = createContext();

export const UnityProvider = ({ children }) => {
  const [unities, setUnities] = useState([]);
  const [selectedUnity, setSelectedUnity] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUnities();
  }, []);

  const loadUnities = async () => {
    try {
      const data = await unityApi.getAllUnities();
      setUnities(data);
      // Selecionar a primeira unidade por padrão (Hospital Univ. de BH)
      if (data.length > 0 && !selectedUnity) {
        setSelectedUnity(data[0]);
      }
    } catch (err) {
      console.error('Erro ao carregar unidades:', err);
    } finally {
      setLoading(false);
    }
  };

  const selectUnity = (unity) => {
    setSelectedUnity(unity);
  };

  return (
    <UnityContext.Provider value={{ unities, selectedUnity, selectUnity, loading }}>
      {children}
    </UnityContext.Provider>
  );
};

export const useUnity = () => {
  const context = useContext(UnityContext);
  if (!context) {
    throw new Error('useUnity must be used within a UnityProvider');
  }
  return context;
};