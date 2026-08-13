import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Card from '../../../components/ui/Card';
import { CardHeader, CardTitle, CardContent } from '../../../components/ui/Card';
import Button from '../../../components/ui/Button';
import { 
  Table, 
  TableHeader, 
  TableBody, 
  TableRow, 
  TableHead, 
  TableCell 
} from '../../../components/ui/Table';
import { 
  Plus, 
  Pencil, 
  Trash2, 
  Pill, 
  ClipboardList,
  X,
  Calendar,
  CheckCircle,
  AlertCircle,
  User,
  Stethoscope
} from 'lucide-react';
import medicationApi from '../../../api/medicationApi';
import doctorApi from '../../../api/doctorApi';
import currentMedicationApi from '../../../api/currentMedicationApi';

const Medications = () => {
  const { patientId } = useParams();
  const navigate = useNavigate();
  const [medications, setMedications] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [currentMedications, setCurrentMedications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingMedication, setEditingMedication] = useState(null);
  const [formData, setFormData] = useState({
    medicationId: '',
    doctorId: '',
    startDate: '',
    dosage: '',
    notes: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadMedications();
    loadDoctors();
    loadCurrentMedications();
  }, [patientId]);

  const loadMedications = async () => {
    try {
      const data = await medicationApi.getAllMedications();
      setMedications(data);
    } catch (err) {
      setError('Erro ao carregar medicamentos');
    }
  };

  const loadDoctors = async () => {
    try {
      const data = await doctorApi.getAllDoctors();
      setDoctors(data);
    } catch (err) {
      setError('Erro ao carregar médicos');
    }
  };

  const loadCurrentMedications = async () => {
    try {
      const data = await currentMedicationApi.getCurrentMedicationsByPatient(patientId);
      setCurrentMedications(data);
    } catch (err) {
      setError('Erro ao carregar medicamentos em uso');
    } finally {
      setLoading(false);
    }
  };

  const handleAddMedication = () => {
    setEditingMedication(null);
    setFormData({
      medicationId: '',
      doctorId: '',
      startDate: '',
      dosage: '',
      notes: ''
    });
    setShowForm(true);
  };

  const handleEditMedication = (medication) => {
    setEditingMedication(medication);
    setFormData({
      medicationId: medication.medicationId,
      doctorId: medication.doctorId || '',
      startDate: medication.startDate,
      dosage: medication.dosage,
      notes: medication.notes || ''
    });
    setShowForm(true);
  };

  const handleDeleteMedication = async (id) => {
    if (!confirm('Tem certeza que deseja excluir este medicamento?')) return;
    
    try {
      await currentMedicationApi.deleteCurrentMedication(id);
      setSuccess('Medicamento excluído com sucesso!');
      loadCurrentMedications();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Erro ao excluir medicamento');
    }
  };

  const handleSubmitMedication = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const payload = {
        patientId: parseInt(patientId),
        ...formData,
        doctorId: formData.doctorId ? parseInt(formData.doctorId) : null
      };

      if (editingMedication) {
        await currentMedicationApi.updateCurrentMedication(editingMedication.id, payload);
        setSuccess('Medicamento atualizado com sucesso!');
      } else {
        await currentMedicationApi.createCurrentMedication(payload);
        setSuccess('Medicamento cadastrado com sucesso!');
      }

      setShowForm(false);
      loadCurrentMedications();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Erro ao salvar medicamento. Verifique os dados e tente novamente.');
    }
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingMedication(null);
    setFormData({
      medicationId: '',
      doctorId: '',
      startDate: '',
      dosage: '',
      notes: ''
    });
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
        <span className="ml-3 text-gray-600">Carregando...</span>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Medicamentos em Uso</h2>
          <p className="text-gray-600 text-sm mt-1">Gerencie medicamentos prescritos para o paciente</p>
        </div>
        <Button 
          onClick={() => navigate('/patients')}
          variant="outline"
        >
          Voltar
        </Button>
      </div>

      {/* Alerts */}
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg flex items-center">
          <AlertCircle className="w-5 h-5 mr-2" />
          {error}
        </div>
      )}

      {success && (
        <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg flex items-center">
          <CheckCircle className="w-5 h-5 mr-2" />
          {success}
        </div>
      )}

      {/* Current Medications Table */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <div className="flex items-center">
              <Pill className="w-5 h-5 mr-2 text-indigo-600" />
              Medicamentos em Uso
            </div>
            <Button onClick={handleAddMedication} className="flex items-center space-x-2">
              <Plus className="w-4 h-4" />
              <span>Novo Medicamento</span>
            </Button>
          </CardTitle>
        </CardHeader>
        <CardContent>
          {showForm ? (
            <div className="border border-gray-200 rounded-lg p-6 mb-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold text-gray-800">
                  {editingMedication ? 'Editar Medicamento' : 'Novo Medicamento'}
                </h3>
                <Button onClick={handleCancel} variant="ghost" size="sm">
                  <X className="w-4 h-4" />
                </Button>
              </div>
              
              <form onSubmit={handleSubmitMedication} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Medicamento *
                  </label>
                  <select
                    value={formData.medicationId}
                    onChange={(e) => setFormData({...formData, medicationId: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    required
                  >
                    <option value="">Selecione um medicamento</option>
                    {medications.map((med) => (
                      <option key={med.id} value={med.id}>
                        {med.name} ({med.dosageForm || 'N/A'})
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Médico Responsável
                  </label>
                  <select
                    value={formData.doctorId}
                    onChange={(e) => setFormData({...formData, doctorId: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                  >
                    <option value="">Selecione um médico (opcional)</option>
                    {doctors.map((doc) => (
                      <option key={doc.id} value={doc.id}>
                        {doc.name} - {doc.specialty || 'N/A'}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Data de Início *
                  </label>
                  <input
                    type="date"
                    value={formData.startDate}
                    onChange={(e) => setFormData({...formData, startDate: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Posologia *
                  </label>
                  <textarea
                    value={formData.dosage}
                    onChange={(e) => setFormData({...formData, dosage: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    rows="3"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Observações
                  </label>
                  <textarea
                    value={formData.notes}
                    onChange={(e) => setFormData({...formData, notes: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    rows="2"
                  />
                </div>

                <div className="flex space-x-3">
                  <Button type="submit">
                    {editingMedication ? 'Atualizar' : 'Salvar'}
                  </Button>
                  <Button type="button" onClick={handleCancel} variant="outline">
                    Cancelar
                  </Button>
                </div>
              </form>
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Medicamento</TableHead>
                  <TableHead>Data Início</TableHead>
                  <TableHead>Posologia</TableHead>
                  <TableHead>Médico</TableHead>
                  <TableHead>Observações</TableHead>
                  <TableHead>Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {currentMedications.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center py-8 text-gray-500">
                      Nenhum medicamento em uso cadastrado
                    </TableCell>
                  </TableRow>
                ) : (
                  currentMedications.map((med) => (
                    <TableRow key={med.id}>
                      <TableCell>
                        <div className="flex items-center">
                          <Pill className="w-4 h-4 mr-2 text-indigo-600" />
                          <div>
                            <div className="font-medium">{med.medicationName}</div>
                            <div className="text-xs text-gray-500">{med.medicationDosageForm || ''}</div>
                          </div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center">
                          <Calendar className="w-4 h-4 mr-2 text-gray-400" />
                          {new Date(med.startDate).toLocaleDateString('pt-BR')}
                        </div>
                      </TableCell>
                      <TableCell>{med.dosage}</TableCell>
                      <TableCell>
                        {med.doctorName ? (
                          <div className="flex items-center">
                            <Stethoscope className="w-4 h-4 mr-2 text-gray-400" />
                            <div>
                              <div className="font-medium">{med.doctorName}</div>
                              <div className="text-xs text-gray-500">{med.doctorCrm || ''}</div>
                            </div>
                          </div>
                        ) : (
                          <span className="text-gray-400">-</span>
                        )}
                      </TableCell>
                      <TableCell>{med.notes || '-'}</TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            onClick={() => handleEditMedication(med)}
                            variant="ghost"
                            size="sm"
                          >
                            <Pencil className="w-4 h-4" />
                          </Button>
                          <Button
                            onClick={() => handleDeleteMedication(med.id)}
                            variant="ghost"
                            size="sm"
                            className="text-red-600 hover:text-red-700"
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default Medications;