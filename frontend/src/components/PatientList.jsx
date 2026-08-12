import { useState, useEffect } from 'react';
import patientApi from '../api/patientApi';
import PatientForm from './PatientForm';
import './PatientList.css';

const PatientList = () => {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingPatient, setEditingPatient] = useState(null);
  const [viewingPatient, setViewingPatient] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  const fetchPatients = async (page = 0) => {
    try {
      setLoading(true);
      const data = await patientApi.getAllPatients();
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
    <div className="patient-list">
      <div className="patient-list-header">
        <h1>Gerenciamento de Pacientes</h1>
        <button className="btn btn-primary" onClick={handleCreate}>
          Adicionar Novo Paciente
        </button>
      </div>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Buscar por nome..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <button className="btn btn-secondary" onClick={handleSearch}>
          Buscar
        </button>
        {searchTerm && (
          <button className="btn btn-secondary" onClick={() => {
            setSearchTerm('');
            fetchPatients();
          }}>
            Limpar
          </button>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">Carregando...</div>
      ) : (
        <>
          <div className="patient-table-container">
            <table className="patient-table">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>CPF</th>
                  <th>Email</th>
                  <th>Telefone</th>
                  <th>Data de Nascimento</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {patients.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="no-data">
                      Nenhum paciente encontrado
                    </td>
                  </tr>
                ) : (
                  patients.map((patient) => (
                    <tr key={patient.id}>
                      <td>{patient.name}</td>
                      <td>{formatCPF(patient.cpf)}</td>
                      <td>{patient.email}</td>
                      <td>{formatPhone(patient.phone)}</td>
                      <td>{formatDate(patient.birthDate)}</td>
                      <td className="actions">
                        <button
                          className="btn btn-small btn-view"
                          onClick={() => handleView(patient)}
                        >
                          Visualizar
                        </button>
                        <button
                          className="btn btn-small btn-edit"
                          onClick={() => handleEdit(patient)}
                        >
                          Editar
                        </button>
                        <button
                          className="btn btn-small btn-delete"
                          onClick={() => handleDelete(patient.id)}
                        >
                          Excluir
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </>
      )}

      {viewingPatient && (
        <div className="modal" onClick={() => setViewingPatient(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Detalhes do Paciente</h2>
              <button className="btn-close" onClick={() => setViewingPatient(null)}>
                ×
              </button>
            </div>
            <div className="modal-body">
              <div className="patient-detail">
                <strong>Nome:</strong> {viewingPatient.name}
              </div>
              <div className="patient-detail">
                <strong>CPF:</strong> {formatCPF(viewingPatient.cpf)}
              </div>
              <div className="patient-detail">
                <strong>Email:</strong> {viewingPatient.email}
              </div>
              <div className="patient-detail">
                <strong>Telefone:</strong> {formatPhone(viewingPatient.phone)}
              </div>
              <div className="patient-detail">
                <strong>Data de Nascimento:</strong> {formatDate(viewingPatient.birthDate)}
              </div>
              {viewingPatient.gender && (
                <div className="patient-detail">
                  <strong>Gênero:</strong> {viewingPatient.gender}
                </div>
              )}
              {viewingPatient.address && (
                <div className="patient-detail">
                  <strong>Endereço:</strong> {viewingPatient.address}
                </div>
              )}
              {viewingPatient.city && (
                <div className="patient-detail">
                  <strong>Cidade:</strong> {viewingPatient.city}
                </div>
              )}
              {viewingPatient.state && (
                <div className="patient-detail">
                  <strong>Estado:</strong> {viewingPatient.state}
                </div>
              )}
              {viewingPatient.bloodType && (
                <div className="patient-detail">
                  <strong>Tipo Sanguíneo:</strong> {viewingPatient.bloodType}
                </div>
              )}
              {viewingPatient.allergies && (
                <div className="patient-detail">
                  <strong>Alergias:</strong> {viewingPatient.allergies}
                </div>
              )}
              {viewingPatient.medicalHistory && (
                <div className="patient-detail">
                  <strong>Histórico Médico:</strong> {viewingPatient.medicalHistory}
                </div>
              )}
              {viewingPatient.emergencyContact && (
                <div className="patient-detail">
                  <strong>Contato de Emergência:</strong> {viewingPatient.emergencyContact}
                </div>
              )}
              {viewingPatient.emergencyPhone && (
                <div className="patient-detail">
                  <strong>Telefone de Emergência:</strong> {formatPhone(viewingPatient.emergencyPhone)}
                </div>
              )}
              <div className="patient-detail">
                <strong>Criado:</strong> {formatDate(viewingPatient.createdAt)}
              </div>
              <div className="patient-detail">
                <strong>Atualizado:</strong> {formatDate(viewingPatient.updatedAt)}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientList;
