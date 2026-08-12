#!/bin/bash

# Script de deployment do Patient API no Kubernetes

set -e

echo "🚀 Iniciando deployment do Patient API no Kubernetes..."

# Verificar se kubectl está instalado
if ! command -v kubectl &> /dev/null; then
    echo "❌ kubectl não está instalado. Por favor, instale o kubectl primeiro."
    exit 1
fi

# Verificar se o cluster está acessível
if ! kubectl cluster-info &> /dev/null; then
    echo "❌ Não foi possível conectar ao cluster Kubernetes."
    exit 1
fi

echo "✅ Cluster Kubernetes conectado com sucesso"

# Criar namespace se não existir
kubectl create namespace default --dry-run=client -o yaml | kubectl apply -f -

# Deploy na ordem correta
echo "📦 Deployando ConfigMaps e Secrets..."
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

echo "🗄️  Deployando PostgreSQL..."
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/postgres-service.yaml

echo "⏳ Aguardando PostgreSQL ficar pronto..."
kubectl wait --for=condition=ready pod -l app=postgres,role=database --timeout=120s

echo "🔧 Deployando Backend..."
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/backend-service.yaml
kubectl apply -f k8s/backend-hpa.yaml

echo "⏳ Aguardando Backend ficar pronto..."
kubectl wait --for=condition=ready pod -l app=patient-api,component=backend --timeout=120s

echo "🎨 Deployando Frontend..."
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/frontend-service.yaml

echo "⏳ Aguardando Frontend ficar pronto..."
kubectl wait --for=condition=ready pod -l app=patient-api,component=frontend --timeout=120s

echo "🌐 Configurando Ingress..."
kubectl apply -f k8s/ingress.yaml

echo "✅ Deployment concluído com sucesso!"
echo ""
echo "📊 Status dos recursos:"
kubectl get all -l app=patient-api
kubectl get all -l app=postgres
kubectl get ingress

echo ""
echo "🎉 Patient API está rodando no Kubernetes!"
echo "🌐 Acesse: http://patient-api.local (configure seu /etc/hosts para apontar para o IP do Ingress)"
