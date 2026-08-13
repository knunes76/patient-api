import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Card from '../../../components/ui/Card';
import { CardHeader, CardTitle, CardContent } from '../../../components/ui/Card';
import Button from '../../../components/ui/Button';
import Badge from '../../../components/ui/Badge';
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
  ClipboardList,
  X,
  Calendar,
  CheckCircle,
  AlertCircle
} from 'lucide-react';
import examApi from '../../../api/examApi';
import examResultApi from '../../../api/examResultApi';

const Exams = () => {
  const { patientId } = useParams();
  const navigate = useNavigate();
  const [examResults, setExamResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [exams, setExams] = useState([]);
  const [showResultForm, setShowResultForm] = useState(false);
  const [editingResult, setEditingResult] = useState(null);
  const [formData, setFormData] = useState({
    examId: '',
    resultDate: '',
    resultValue: '',
    notes: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    loadExams();
    loadExamResults();
  }, [patientId]);

  const loadExams = async () => {
    try {
      const data = await examApi.getAllExams();
      setExams(data);
    } catch (err) {
      setError('Erro ao carregar exames');
    }
  };

  const loadExamResults = async () => {
    try {
      const data = await examResultApi.getExamResultsByPatient(patientId);
      setExamResults(data);
    } catch (err) {
      setError('Erro ao carregar resultados de exames');
    } finally {
      setLoading(false);
    }
  };

  const handleAddResult = () => {
    setEditingResult(null);
    setFormData({
      examId: '',
      resultDate: '',
      resultValue: '',
      notes: ''
    });
    setShowResultForm(true);
  };

  const handleEditResult = (result) => {
    setEditingResult(result);
    setFormData({
      examId: result.examId,
      resultDate: result.resultDate,
      resultValue: result.resultValue,
      notes: result.notes || ''
    });
    setShowResultForm(true);
  };

  const handleDeleteResult = async (id) => {
    if (!confirm('Tem certeza que deseja excluir este resultado?')) return;
    
    try {
      await examResultApi.deleteExamResult(id);
      setSuccess('Resultado excluído com sucesso!');
      loadExamResults();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Erro ao excluir resultado');
    }
  };

  const handleSubmitResult = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const payload = {
        patientId: parseInt(patientId),
        ...formData
      };

      if (editingResult) {
        await examResultApi.updateExamResult(editingResult.id, payload);
        setSuccess('Resultado atualizado com sucesso!');
      } else {
        await examResultApi.createExamResult(payload);
        setSuccess('Resultado cadastrado com sucesso!');
      }

      setShowResultForm(false);
      loadExamResults();
      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError('Erro ao salvar resultado. Verifique os dados e tente novamente.');
    }
  };

  const handleCancel = () => {
    setShowResultForm(false);
    setEditingResult(null);
    setFormData({
      examId: '',
      resultDate: '',
      resultValue: '',
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
          <h2 className="text-2xl font-bold text-gray-800">Exames do Paciente</h2>
          <p className="text-gray-600 text-sm mt-1">Gerencie e visualize resultados de exames</p>
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

      {/* Exam Results Table */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center justify-between">
            <div className="flex items-center">
              <ClipboardList className="w-5 h-5 mr-2 text-indigo-600" />
              Resultados de Exames
            </div>
            <Button onClick={handleAddResult} className="flex items-center space-x-2">
              <Plus className="w-4 h-4" />
              <span>Novo Resultado</span>
            </Button>
          </CardTitle>
        </CardHeader>
        <CardContent>
          {showResultForm ? (
            <div className="border border-gray-200 rounded-lg p-6 mb-6">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold text-gray-800">
                  {editingResult ? 'Editar Resultado' : 'Novo Resultado'}
                </h3>
                <Button onClick={handleCancel} variant="ghost" size="sm">
                  <X className="w-4 h-4" />
                </Button>
              </div>
              
              <form onSubmit={handleSubmitResult} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Exame *
                  </label>
                  <select
                    value={formData.examId}
                    onChange={(e) => setFormData({...formData, examId: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    required
                  >
                    <option value="">Selecione um exame</option>
                    {exams.map((exam) => (
                      <option key={exam.id} value={exam.id}>{exam.name}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Data do Resultado *
                  </label>
                  <input
                    type="date"
                    value={formData.resultDate}
                    onChange={(e) => setFormData({...formData, resultDate: e.target.value})}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Valor do Resultado *
                  </label>
                  <textarea
                    value={formData.resultValue}
                    onChange={(e) => setFormData({...formData, resultValue: e.target.value})}
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
                    {editingResult ? 'Atualizar' : 'Salvar'}
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
                  <TableHead>Exame</TableHead>
                  <TableHead>Data</TableHead>
                  <TableHead>Resultado</TableHead>
                  <TableHead>Observações</TableHead>
                  <TableHead>Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {examResults.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={5} className="text-center py-8 text-gray-500">
                      Nenhum resultado de exame cadastrado
                    </TableCell>
                  </TableRow>
                ) : (
                  examResults.map((result) => (
                    <TableRow key={result.id}>
                      <TableCell>{result.examName}</TableCell>
                      <TableCell>
                        <div className="flex items-center">
                          <Calendar className="w-4 h-4 mr-2 text-gray-400" />
                          {new Date(result.resultDate).toLocaleDateString('pt-BR')}
                        </div>
                      </TableCell>
                      <TableCell>{result.resultValue}</TableCell>
                      <TableCell>{result.notes || '-'}</TableCell>
                      <TableCell>
                        <div className="flex space-x-2">
                          <Button
                            onClick={() => handleEditResult(result)}
                            variant="ghost"
                            size="sm"
                          >
                            <Pencil className="w-4 h-4" />
                          </Button>
                          <Button
                            onClick={() => handleDeleteResult(result.id)}
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

export default Exams;