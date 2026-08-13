import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { UnityProvider } from './context/UnityContext';
import Login from './components/Login';
import Layout from './components/Layout';
import PatientList from './components/PatientList';
import Tests from './components/Tests';
import Exams from './components/modules/patients/Exams';
import Medications from './components/modules/patients/Medications';
import ClinicalEvolution from './components/modules/patients/ClinicalEvolution';

function AppContent() {
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return <Login />;
  }

  const getTitle = () => {
    const path = location.pathname;
    if (path === '/patients') return 'Pacientes';
    if (path === '/tests') return 'Testes';
    if (path.includes('/exams')) return 'Exames';
    if (path.includes('/medications')) return 'Medicamentos';
    if (path.includes('/clinical-evolution')) return 'Evolução Clínica';
    return 'Sistema';
  };

  return (
    <Layout title={getTitle()}>
      <Routes>
        <Route path="/patients" element={<PatientList />} />
        <Route path="/patients/:patientId/exams" element={<Exams />} />
        <Route path="/patients/:patientId/medications" element={<Medications />} />
        <Route path="/patients/:patientId/clinical-evolution" element={<ClinicalEvolution />} />
        <Route path="/tests" element={<Tests />} />
        <Route path="/" element={<PatientList />} />
      </Routes>
    </Layout>
  );
}

function App() {
  return (
    <AuthProvider>
      <UnityProvider>
        <Router>
          <AppContent />
        </Router>
      </UnityProvider>
    </AuthProvider>
  );
}

export default App;
