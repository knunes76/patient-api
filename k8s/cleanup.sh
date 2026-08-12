#!/bin/bash

# Script de cleanup do Patient API no Kubernetes

set -e

echo "🧹 Limpando recursos do Kubernetes..."

# Remover recursos na ordem inversa
echo "🌐 Removendo Ingress..."
kubectl delete -f k8s/ingress.yaml --ignore-not-found=true

echo "🎨 Removendo Frontend..."
kubectl delete -f k8s/frontend-service.yaml --ignore-not-found=true
kubectl delete -f k8s/frontend-deployment.yaml --ignore-not-found=true

echo "🔧 Removendo Backend..."
kubectl delete -f k8s/backend-hpa.yaml --ignore-not-found=true
kubectl delete -f k8s/backend-service.yaml --ignore-not-found=true
kubectl delete -f k8s/backend-deployment.yaml --ignore-not-found=true

echo "🗄️  Removendo PostgreSQL..."
kubectl delete -f k8s/postgres-service.yaml --ignore-not-found=true
kubectl delete -f k8s/postgres-deployment.yaml --ignore-not-found=true
kubectl delete -f k8s/postgres-pvc.yaml --ignore-not-found=true

echo "📦 Removendo ConfigMaps e Secrets..."
kubectl delete -f k8s/secret.yaml --ignore-not-found=true
kubectl delete -f k8s/configmap.yaml --ignore-not-found=true

echo "✅ Cleanup concluído com sucesso!"
