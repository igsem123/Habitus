#!/bin/bash

echo "🔧 Limpando caches locais do Gradle..."
rm -rf ~/.gradle/
rm -rf .gradle/
rm -rf build/
rm -rf app/build/

echo "🧹 Limpando configurações antigas do Android Studio (opcional)..."
rm -rf .idea/
find . -name "*.iml" -type f -delete

echo "✅ Limpando workspace do Kotlin DSL corrompido..."
rm -rf ~/.android/
rm -rf ~/.cache/
rm -rf ~/.kotlin/

echo "📦 Reinstalando dependências em cache temporário..."
./gradlew clean build --refresh-dependencies --gradle-user-home=/tmp/gradle-temp-cache

BUILD_STATUS=$?

if [ $BUILD_STATUS -eq 0 ]; then
    echo "🎉 Build finalizado com sucesso!"
else
    echo "❌ Build falhou. Verifique os erros acima."
fi
