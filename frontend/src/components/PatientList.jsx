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
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchPatients = async (page = 0) => {
    try {
      setLoading(true);
      const data = await patientApi.getAllPatients(page, 10, 'name', 'asc');
      setPatients(data.content);
      setTotalPages(data.totalPages);
      setCurrentPage(data.number);
      setError(null);
    } catch (err) {
      setError('Failed to fetch patients');
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
        setTotalPages(1);
        setCurrentPage(0);
        setError(null);
      } catch (err) {
        setError('Failed to search patients');
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
    if (window.confirm('Are you sure you want to delete this patient?')) {
      try {
        await patientApi.deletePatient(id);
        fetchPatients(currentPage);
      } catch (err) {
        setError('Failed to delete patient');
        console.error(err);
      }
    }
  };

  const handleFormClose = () => {
    setShowForm(false);
    setEditingPatient(null);
    fetchPatients(currentPage);
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
      setError('Failed to save patient');
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
        <h1>Patient Management</h1>
        <button className="btn btn-primary" onClick={handleCreate}>
          Add New Patient
        </button>
      </div>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Search by name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <button className="btn btn-secondary" onClick={handleSearch}>
          Search
        </button>
        {searchTerm && (
          <button className="btn btn-secondary" onClick={() => {
            setSearchTerm('');
            fetchPatients();
          }}>
            Clear
          </button>
        )}
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <>
          <div className="patient-table-container">
            <table className="patient-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>CPF</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Birth Date</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {patients.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="no-data">
                      No patients found
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
                          View
                        </button>
                        <button
                          className="btn btn-small btn-edit"
                          onClick={() => handleEdit(patient)}
                        >
                          Edit
                        </button>
                        <button
                          className="btn btn-small btn-delete"
                          onClick={() => handleDelete(patient.id)}
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button
                className="btn btn-secondary"
                onClick={() => fetchPatients(currentPage - 1)}
                disabled={currentPage === 0}
              >
                Previous
              </button>
              <span>
                Page {currentPage + 1} of {totalPages}
              </span>
              <button
                className="btn btn-secondary"
                onClick={() => fetchPatients(currentPage + 1)}
                disabled={currentPage === totalPages - 1}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}

      {viewingPatient && (
        <div className="modal" onClick={() => setViewingPatient(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Patient Details</h2>
              <button className="btn-close" onClick={() => setViewingPatient(null)}>
                ×
              </button>
            </div>
            <div className="modal-body">
              <div className="patient-detail">
                <strong>Name:</strong> {viewingPatient.name}
              </div>
              <div className="patient-detail">
                <strong>CPF:</strong> {formatCPF(viewingPatient.cpf)}
              </div>
              <div className="patient-detail">
                <strong>Email:</strong> {viewingPatient.email}
              </div>
              <div className="patient-detail">
                <strong>Phone:</strong> {formatPhone(viewingPatient.phone)}
              </div>
              <div className="patient-detail">
                <strong>Birth Date:</strong> {formatDate(viewingPatient.birthDate)}
              </div>
              {viewingPatient.gender && (
                <div className="patient-detail">
                  <strong>Gender:</strong> {viewingPatient.gender}
                </div>
              )}
              {viewingPatient.address && (
                <div className="patient-detail">
                  <strong>Address:</strong> {viewingPatient.address}
                </div>
              )}
              {viewingPatient.city && (
                <div className="patient-detail">
                  <strong>City:</strong> {viewingPatient.city}
                </div>
              )}
              {viewingPatient.state && (
                <div className="patient-detail">
                  <strong>State:</strong> {viewingPatient.state}
                </div>
              )}
              {viewingPatient.bloodType && (
                <div className="patient-detail">
                  <strong>Blood Type:</strong> {viewingPatient.bloodType}
                </div>
              )}
              {viewingPatient.allergies && (
                <div className="patient-detail">
                  <strong>Allergies:</strong> {viewingPatient.allergies}
                </div>
              )}
              {viewingPatient.medicalHistory && (
                <div className="patient-detail">
                  <strong>Medical History:</strong> {viewingPatient.medicalHistory}
                </div>
              )}
              {viewingPatient.emergencyContact && (
                <div className="patient-detail">
                  <strong>Emergency Contact:</strong> {viewingPatient.emergencyContact}
                </div>
              )}
              {viewingPatient.emergencyPhone && (
                <div className="patient-detail">
                  <strong>Emergency Phone:</strong> {formatPhone(viewingPatient.emergencyPhone)}
                </div>
              )}
              <div className="patient-detail">
                <strong>Created:</strong> {formatDate(viewingPatient.createdAt)}
              </div>
              <div className="patient-detail">
                <strong>Updated:</strong> {formatDate(viewingPatient.updatedAt)}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientList;
