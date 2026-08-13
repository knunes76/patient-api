import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import patientApi from '../api/patientApi';
import PatientForm from './PatientForm';
import { useUnity } from '../context/UnityContext';

const PatientList = () => {
  const navigate = useNavigate();
  const { selectedUnity } = useUnity();
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingPatient, setEditingPatient] = useState(null);
  const [viewingPatient, setViewingPatient] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchPatients();
  }, [selectedUnity]);

  const fetchPatients = async (page = 0) => {
    try {
      setLoading(true);
      let data;
      if (selectedUnity) {
        data = await patientApi.getPatientsByUnity(selectedUnity.id);
      } else {
        data = await patientApi.getAllPatients();
      }
      setPatients(data);
      setError(null);
    } catch (err) {
      setError('Falha ao buscar pacientes');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    if (searchTerm.trim()) {
      try {
        setLoading(true);
        const data = await patientApi.searchPatients(searchTerm);
        setPatients(data);
        setError(null);
      } catch (err) {
        setError('Falha ao buscar pacientes');
        console.error(err);
      } finally {
        setLoading(false);
      }
    } else {
      fetchPatients();
    }
  };

  const handleCreate = () => {
    setEditingPatient(null);
    setShowForm(true);
  };

  const handleEdit = (patient) => {
    setEditingPatient(patient);
    setShowForm(true);
  };

  const handleView = (patient) => {
    setViewingPatient(patient);
  };

  const handleExams = (patient) => {
    navigate(`/patients/${patient.id}/exams`);
  };

  const handleMedications = (patient) => {
    navigate(`/patients/${patient.id}/medications`);
  };

  const handleClinicalEvolution = (patient) => {
    navigate(`/patients/${patient.id}/clinical-evolution`);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este paciente?')) {
      try {
        await patientApi.deletePatient(id);
        fetchPatients();
      } catch (err) {
        setError('Falha ao excluir paciente');
        console.error(err);
      }
    }
  };

  const handleFormClose = () => {
    setShowForm(false);
    setEditingPatient(null);
    fetchPatients();
  };

  const handleFormSubmit = async (patientData) => {
    try {
      if (editingPatient) {
        await patientApi.updatePatient(editingPatient.id, patientData);
      } else {
        await patientApi.createPatient(patientData);
      }
      handleFormClose();
    } catch (err) {
      setError('Falha ao salvar paciente');
      console.error(err);
    }
  };

  useEffect(() => {
    fetchPatients();
  }, []);

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('pt-BR');
  };

  const formatCPF = (cpf) => {
    if (!cpf || cpf.length !== 11) return cpf;
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  };

  const formatPhone = (phone) => {
    if (!phone) return 'N/A';
    if (phone.length === 11) {
      return phone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (phone.length === 10) {
      return phone.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    }
    return phone;
  };

  if (showForm) {
    return (
      <PatientForm
        patient={editingPatient}
        onSubmit={handleFormSubmit}
        onCancel={handleFormClose}
      />
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">Gerenciamento de Pacientes</h1>
        <button
          onClick={handleCreate}
          className="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700 transition font-medium"
        >
          Adicionar Novo Paciente
        </button>
      </div>

      <div className="flex gap-4">
        <input
          type="text"
          placeholder="Buscar por nome..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
          className="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
        />
        <button
          onClick={handleSearch}
          className="bg-gray-600 text-white px-6 py-3 rounded-lg hover:bg-gray-700 transition font-medium"
        >
          Buscar
        </button>
        {searchTerm && (
          <button
            onClick={() => {
              setSearchTerm('');
              fetchPatients();
            }}
            className="bg-gray-400 text-white px-6 py-3 rounded-lg hover:bg-gray-500 transition font-medium"
          >
            Limpar
          </button>
        )}
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
          {error}
        </div>
      )}

      {loading ? (
        <div className="text-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Carregando pacientes...</p>
        </div>
      ) : (
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Nome
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  CPF
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Email
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Telefone
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Data de Nascimento
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Ações
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {patients.length === 0 ? (
                <tr>
                  <td colSpan="6" className="px-6 py-12 text-center text-gray-500">
                    Nenhum paciente encontrado
                  </td>
                </tr>
              ) : (
                patients.map((patient) => (
                  <tr key={patient.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {patient.name}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {formatCPF(patient.cpf)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {patient.email}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {formatPhone(patient.phone)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      {formatDate(patient.birthDate)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                      <div className="flex gap-2">
                        <button
                          onClick={() => handleView(patient)}
                          className="text-blue-600 hover:text-blue-800 font-medium"
                        >
                          Visualizar
                        </button>
                        <button
                          onClick={() => handleEdit(patient)}
                          className="text-green-600 hover:text-green-800 font-medium"
                        >
                          Editar
                        </button>
                        <button
                          onClick={() => handleExams(patient)}
                          className="text-purple-600 hover:text-purple-800 font-medium"
                        >
                          Exames
                        </button>
                        <button
                          onClick={() => handleMedications(patient)}
                          className="text-orange-600 hover:text-orange-800 font-medium"
                        >
                          Medicamentos
                        </button>
                        <button
                          onClick={() => handleClinicalEvolution(patient)}
                          className="text-teal-600 hover:text-teal-800 font-medium"
                        >
                          Evolução
                        </button>
                        <button
                          onClick={() => handleDelete(patient.id)}
                          className="text-red-600 hover:text-red-800 font-medium"
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}

      {viewingPatient && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4" onClick={() => setViewingPatient(null)}>
          <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
            <div className="p-6 border-b border-gray-200 flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Detalhes do Paciente</h2>
              <button
                onClick={() => setViewingPatient(null)}
                className="text-gray-400 hover:text-gray-600 text-2xl"
              >
                ×
              </button>
            </div>
            <div className="p-6 space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <strong className="text-gray-700">Nome:</strong>
                  <p className="text-gray-900">{viewingPatient.name}</p>
                </div>
                <div>
                  <strong className="text-gray-700">CPF:</strong>
                  <p className="text-gray-900">{formatCPF(viewingPatient.cpf)}</p>
                </div>
                <div>
                  <strong className="text-gray-700">Email:</strong>
                  <p className="text-gray-900">{viewingPatient.email}</p>
                </div>
                <div>
                  <strong className="text-gray-700">Telefone:</strong>
                  <p className="text-gray-900">{formatPhone(viewingPatient.phone)}</p>
                </div>
                <div>
                  <strong className="text-gray-700">Data de Nascimento:</strong>
                  <p className="text-gray-900">{formatDate(viewingPatient.birthDate)}</p>
                </div>
                {viewingPatient.gender && (
                  <div>
                    <strong className="text-gray-700">Gênero:</strong>
                    <p className="text-gray-900">{viewingPatient.gender}</p>
                  </div>
                )}
                {viewingPatient.address && (
                  <div className="col-span-2">
                    <strong className="text-gray-700">Endereço:</strong>
                    <p className="text-gray-900">{viewingPatient.address}</p>
                  </div>
                )}
                {viewingPatient.city && (
                  <div>
                    <strong className="text-gray-700">Cidade:</strong>
                    <p className="text-gray-900">{viewingPatient.city}</p>
                  </div>
                )}
                {viewingPatient.state && (
                  <div>
                    <strong className="text-gray-700">Estado:</strong>
                    <p className="text-gray-900">{viewingPatient.state}</p>
                  </div>
                )}
                {viewingPatient.bloodType && (
                  <div>
                    <strong className="text-gray-700">Tipo Sanguíneo:</strong>
                    <p className="text-gray-900">{viewingPatient.bloodType}</p>
                  </div>
                )}
                {viewingPatient.allergies && (
                  <div className="col-span-2">
                    <strong className="text-gray-700">Alergias:</strong>
                    <p className="text-gray-900">{viewingPatient.allergies}</p>
                  </div>
                )}
                {viewingPatient.medicalHistory && (
                  <div className="col-span-2">
                    <strong className="text-gray-700">Histórico Médico:</strong>
                    <p className="text-gray-900">{viewingPatient.medicalHistory}</p>
                  </div>
                )}
                {viewingPatient.emergencyContact && (
                  <div>
                    <strong className="text-gray-700">Contato de Emergência:</strong>
                    <p className="text-gray-900">{viewingPatient.emergencyContact}</p>
                  </div>
                )}
                {viewingPatient.emergencyPhone && (
                  <div>
                    <strong className="text-gray-700">Telefone de Emergência:</strong>
                    <p className="text-gray-900">{formatPhone(viewingPatient.emergencyPhone)}</p>
                  </div>
                )}
                <div>
                  <strong className="text-gray-700">Criado:</strong>
                  <p className="text-gray-900">{formatDate(viewingPatient.createdAt)}</p>
                </div>
                <div>
                  <strong className="text-gray-700">Atualizado:</strong>
                  <p className="text-gray-900">{formatDate(viewingPatient.updatedAt)}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientList;
