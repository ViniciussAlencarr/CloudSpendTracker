# 💸 CloudSpend Tracker: Enterprise-Grade FinOps Architecture

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

Um projeto desenvolvido ponta a ponta para demonstrar a implementação de uma arquitetura resiliente, focada em **Offline-First**, **Segurança de API** e **Integração Contínua (CI/CD)**. 

## 🎥 Demonstração e UI Reativa

O aplicativo reage dinamicamente aos dados do banco local. Abaixo, a representação do estado da UI quando o gasto ultrapassa o limite orçamentário (vermelho) versus dentro do limite (azul).
![emulador](https://github.com/user-attachments/assets/eda7be7e-9a87-432f-b31b-533244aa9b56)

## 🏗️ Arquitetura e Padrões (Clean Architecture)

O projeto segue estritamente a Clean Architecture e o padrão MVVM, com separação clara entre as camadas de Data, Domain e Presentation.
<img width="1875" height="1041" alt="android_studio" src="https://github.com/user-attachments/assets/b0976793-a7a0-4636-a1bc-5fc3dedea0e8" />

### 1. Camada de Rede Segura (AWS + Retrofit)
A rota da API na AWS é protegida por Chave de API. Abaixo, a validação do endpoint negando requisições não autenticadas (403) e permitindo o tráfego seguro (200 OK).
![reqbin](https://github.com/user-attachments/assets/f207eca8-33f6-490e-9a74-c4b22835b80d)

O app Android utiliza um `Interceptor` customizado injetado via Hilt para enviar a *API Key* de forma invisível.

### 2. Estratégia Offline-First (Single Source of Truth)
O **Repository** busca os dados assincronamente da AWS, utiliza `TypeConverters` e atualiza o banco local (**Room Database**). A UI apenas observa as mutações do banco através de `StateFlow`.

## ⚙️ Testes e CI/CD

A confiabilidade do código é garantida por testes unitários (`JUnit`, `MockK`, `kotlinx-coroutines-test`) e uma esteira de automação no **GitHub Actions**, que compila o projeto e roda os testes a cada novo push na branch `master`.
<img width="1864" height="944" alt="workspaces" src="https://github.com/user-attachments/assets/e5013265-f9e2-4fd4-8353-d6d78f56608e" />

## 🚀 Como Executar
1. Clone o repositório.
2. Abra no Android Studio.
3. No arquivo `AppModule.kt`, insira sua API Key da AWS no Interceptor do OkHttp.
4. Execute o app no emulador ou dispositivo físico.
