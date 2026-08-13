import React, { useState } from 'react';
import Card from './ui/Card';
import { CardHeader, CardTitle, CardContent } from './ui/Card';
import Button from './ui/Button';
import { FlaskConical, CheckCircle, XCircle, Loader2 } from 'lucide-react';

const Tests = () => {
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState([]);

  const runTests = async () => {
    setLoading(true);
    setResults([]);

    // Simula testes
    setTimeout(() => {
      setResults([
        { name: 'GET /api/patients', status: 'success', time: '45ms' },
        { name: 'POST /api/auth/login', status: 'success', time: '120ms' },
        { name: 'GET /api/patients/1', status: 'success', time: '32ms' },
        { name: 'POST /api/patients', status: 'success', time: '156ms' },
        { name: 'PUT /api/patients/1', status: 'success', time: '89ms' },
        { name: 'DELETE /api/patients/5', status: 'success', time: '67ms' },
      ]);
      setLoading(false);
    }, 2000);
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-800">Testes da API</h2>
        <Button 
          onClick={runTests} 
          disabled={loading}
          className="flex items-center space-x-2"
        >
          {loading ? (
            <>
              <Loader2 className="w-4 h-4 animate-spin" />
              <span>Executando...</span>
            </>
          ) : (
            <>
              <FlaskConical className="w-4 h-4" />
              <span>Executar Testes</span>
            </>
          )}
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Resultados dos Testes</CardTitle>
        </CardHeader>
        <CardContent>
          {results.length === 0 && !loading && (
            <div className="text-center py-12 text-gray-500">
              <FlaskConical className="w-16 h-16 mx-auto mb-4 text-gray-300" />
              <p>Nenhum teste executado ainda</p>
              <p className="text-sm mt-2">Clique em "Executar Testes" para iniciar</p>
            </div>
          )}

          {loading && (
            <div className="text-center py-12">
              <Loader2 className="w-16 h-16 mx-auto mb-4 text-indigo-600 animate-spin" />
              <p className="text-gray-600">Executando testes...</p>
            </div>
          )}

          {results.length > 0 && (
            <div className="space-y-3">
              {results.map((result, index) => (
                <div
                  key={index}
                  className="flex items-center justify-between p-4 bg-gray-50 rounded-lg"
                >
                  <div className="flex items-center space-x-3">
                    {result.status === 'success' ? (
                      <CheckCircle className="w-5 h-5 text-green-600" />
                    ) : (
                      <XCircle className="w-5 h-5 text-red-600" />
                    )}
                    <span className="font-medium text-gray-800">{result.name}</span>
                  </div>
                  <div className="flex items-center space-x-4">
                    <span className="text-sm text-gray-500">{result.time}</span>
                    <span className={`px-2 py-1 rounded text-xs font-medium ${
                      result.status === 'success' 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-red-100 text-red-800'
                    }`}>
                      {result.status === 'success' ? 'PASS' : 'FAIL'}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Informações dos Testes</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-4 text-sm text-gray-600">
            <p><strong>Endpoints testados:</strong></p>
            <ul className="list-disc list-inside space-y-1 ml-4">
              <li>GET /api/patients - Listar todos os pacientes</li>
              <li>POST /api/auth/login - Autenticação de usuário</li>
              <li>GET /api/patients/:id - Buscar paciente específico</li>
              <li>POST /api/patients - Criar novo paciente</li>
              <li>PUT /api/patients/:id - Atualizar paciente</li>
              <li>DELETE /api/patients/:id - Remover paciente</li>
            </ul>
            <p className="mt-4"><strong>Nota:</strong> Estes são testes simulados. Para testes reais, acesse a documentação Swagger.</p>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default Tests;