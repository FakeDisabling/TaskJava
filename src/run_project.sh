#!/bin/bash

if ! command -v mvn &> /dev/null
then
    echo "Maven не установлен или не найден в PATH. Установите Maven и добавьте его в PATH."
    exit 1
fi

mvn clean install

if [ $? -ne 0 ]; then
  echo "Ошибка сборки Maven!"
  exit 1
fi

mvn exec:java -Dexec.mainClass="org.example.Main"  # Убедитесь, что путь к Main классу указан правильно
