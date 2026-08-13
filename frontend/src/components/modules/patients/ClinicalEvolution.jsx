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
  FileText, 
  Calendar,
  X,
  CheckCircle,
  AlertCircle,
  User,
  Stethoscope,
  Activity
} from 'lucide-react';
import cid10Api from '../../../api/cid10Api';
import specialtyApi from '../../../api/specialtyApi';
import clinicalEvolutionApi from '../../../api/clinicalEvolutionApi';

const ClinicalEvolution = () => {
  const { patientId } = useParams();
  const navigate = useNavigate();
  const [cid10List, setCid10List] = useState([]);
  const [specialties, setSpecialties] = useState([]);
  const [evolutions, setEvolutions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingEvolution, setEditingEvolution] = useState(null);
  const [formData, setFormData] = useState({
    doctorId: '',
    cid10Id: '',
    specialtyId: '',
    appointmentDate: '',
    complaint: '',
    diagnosis: '',
    consultationType: '',
    subject: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadCID10();
    loadSpecialties();
    loadEvolutions();
  }, [patientId]);

  const loadCID10 = async () => {
    try {
      const data = await cid10Api.getAllCID10();
      setCid10List(data);
    } catch (err) {
      setError('Erro ao carregar CID10');
    }
  };

  const loadSpecialties = async () => {
    try {
      const data = await specialtyApi.getAllSpecialties();
      setSpecialties(data);
    } catch (err) {
      setError('Erro ao carregar especialidades');
    }
  };

  const loadEvolutions = async () => {
    try {
      const data = await clinicalEvolutionApi.getClinicalEvolutionsByPatient(patientId);
      setEvolutions(data);
    } catch (err) {
      setError('Erro ao carregar evoluções clínicas');
    } finally {
      setLoading(false);
    }
  };

  const handleAddEvolution = () => {
    setEditingEvolution(null);
    setFormData({
      doctorId: '',
      cid10Id: '',
      specialtyId: '',
      appointmentDate: '',
      complaint: '',
      diagnosis: '',
      consultationType: '',
      subject: ''
    });
    setShowForm(true);
  };

  const handleEditEvolution = (evolution) => {
    setEditingEvolution(evolution);
    setFormData({
      doctorId: evolution.doctorId || '',
      cid10Id: evolution.cid10Id || '',
      specialtyId: evolution.specialtyId || '',
      appointmentDate: evolution.appointmentDate,
      complaint: evolution.complaint,
      diagnosis: evolution.diagnosis || '',
      consultationType: evolution.consultationType || '',
      subject: evolution.subject || ''
    });
    setShowForm(true);
  };

  const handleDeleteEvolution = async (id) => {
    if (!confirm('Tem certeza que deseja excluir esta evolução clínica?')) return;
    
    try {
      await clinicalEvolutionApi.deleteClinicalEvolution(id);
      setSuccess('Evolução clínica excluída com sucesso!');
      loadEvolutions();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Erro ao excluir evolução clínica');
    }
  };

  const handleSubmitEvolution = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const payload = {
        patientId: parseInt(patientId),
        ...formData,
        doctorId: formData.doctorId ? parseInt(formData.doctorId) : null,
        cid10Id: formData.cid10Id ? parseInt(formData.cid10Id) : null,
        specialtyId: formData.specialtyId ? parseInt(formData.specialtyId) : null
      };

      if (editingEvolution) {
        await clinicalEvolutionApi.updateClinicalEvolution(editingEvolution.id, payload);
        setSuccess('Evolução clínica atualizada com sucesso!');
      } else {
        await clinicalEvolutionApi.createClinicalEvolution(payload);
        setSuccess('Evolução clínica cadastrada com sucesso!');
      }

      setShowForm(false);
      loadEvolutions();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Erro ao salvar evolução clínica. Verifique os dados e tente novamente.');
    }
  };

  const handleCancel = () => {
    setShowForm(false);
    setEditingEvolution(null);
    setFormData({
      doctorId: '',
      cid10Id: '',
      specialtyId: '',
      appointmentDate: '',
      complaint: '',
      diagnosis: '',
      consultationType: '',
      subject: ''
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
          <h2 className="text-2xl font-bold text-gray-800">Evolução Clínica</h2>
          <p className="text-gray-600 text-sm mt-1">Gerencie as evoluções clínicas do paciente</p>
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

      {/* Clinical Evolution Table */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <div className="flex items-center">
              <Activity className="w-5 h-5 mr-2 text-indigo-600" />
              Evoluções Clínicas
            </div>
            <Button onClick={handleAddEvolution} className="flex items-center space-x-2">
              <Plus className="w-4 h-4" />
              <span>Nova Evolução</span>
            </Button>
          </CardTitle>
        </CardHeader>
        <CardContent>
          {showForm ? (
            <div className="border border-gray-200 rounded-lg p-6 mb-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold text-gray-800">
                  {editingEvolution ? 'Editar Evolução' : 'Nova Evolução'}
                </h3>
                <Button onClick={handleCancel} variant="ghost" size="sm">
                  <X className="w-4 h-4" />
                </Button>
              </div>
              
              <form onSubmit={handleSubmitEvolution} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Data de Atendimento *
                  </label>
                  <input
                    type="date"
                    value={formData.appointmentDate}
                    onChange={(e) => setFormData({...formData, appointmentDate: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Queixa *
                  </label>
                  <textarea
                    value={formData.complaint}
                    onChange={(e) => setFormData({...formData, complaint: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    rows="3"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Diagnóstico
                  </label>
                  <textarea
                    value={formData.diagnosis}
                    onChange={(e) => setFormData({...formData, diagnosis: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    rows="3"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Tipo de Consulta
                  </label>
                  <select
                    value={formData.consultationType}
                    onChange={(e) => setFormData({...formData, consultationType: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                  >
                    <option value="">Selecione o tipo</option>
                    <option value="Primeiro Atendimento">Primeiro Atendimento</option>
                    <option value="Atendimento Normal">Atendimento Normal</option>
                    <option value="Retorno">Retorno</option>
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Assunto
                  </label>
                  <input
                    type="text"
                    value={formData.subject}
                    onChange={(e) => setFormData({...formData, subject: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Especialidade
                  </label>
                  <select
                    value={formData.specialtyId}
                    onChange={(e) => setFormData({...formData, specialtyId: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                  >
                    <option value="">Selecione uma especialidade</option>
                    {specialties.map((spec) => (
                      <option key={spec.id} value={spec.id}>
                        {spec.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    CID10
                  </label>
                  <select
                    value={formData.cid10Id}
                    onChange={(e) => setFormData({...formData, cid10Id: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                  >
                    <option value="">Selecione um CID10</option>
                    {cid10List.map((cid) => (
                      <option key={cid.id} value={cid.id}>
                        {cid.code} - {cid.description}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="flex space-x-3">
                  <Button type="submit">
                    {editingEvolution ? 'Atualizar' : 'Salvar'}
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
                  <TableHead>Data</TableHead>
                  <TableHead>Queixa</TableHead>
                  <TableHead>Diagnóstico</TableHead>
                  <TableHead>Tipo</TableHead>
                  <TableHead>Assunto</TableHead>
                  <TableHead>Especialidade</TableHead>
                  <TableHead>CID10</TableHead>
                  <TableHead>Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {evolutions.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={8} className="text-center py-8 text-gray-500">
                      Nenhuma evolução clínica cadastrada
                    </TableCell>
                  </TableRow>
                ) : (
                  evolutions.map((evolution) => (
                    <TableRow key={evolution.id}>
                      <TableCell>
                        <div className="flex items-center">
                          <Calendar className="w-4 h-4 mr-2 text-gray-400" />
                          {new Date(evolution.appointmentDate).toLocaleDateString('pt-BR')}
                        </div>
                      </TableCell>
                      <TableCell>{evolution.complaint}</TableCell>
                      <TableCell>{evolution.diagnosis || '-'}</TableCell>
                      <TableCell>
                        <span className="px-2 py-1 bg-indigo-100 text-indigo-800 rounded text-xs">
                          {evolution.consultationType || '-'}
                        </span>
                      </TableCell>
                      <TableCell>{evolution.subject || '-'}</TableCell>
                      <TableCell>{evolution.specialtyName || '-'}</TableCell>
                      <TableCell>
                        {evolution.cid10Code ? (
                          <div>
                            <span className="font-medium">{evolution.cid10Code}</span>
                            <div className="text-xs text-gray-500">{evolution.cid10Description}</div>
                          </div>
                        ) : (
                          <span className="text-gray-400">-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            onClick={() => handleEditEvolution(evolution)}
                            variant="ghost"
                            size="sm"
                          >
                            <Pencil className="w-4 h-4" />
                          </Button>
                          <Button
                            onClick={() => handleDeleteEvolution(evolution.id)}
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

export default ClinicalEvolution;