# 📧 RunMail - Backend
Bem-vindo ao RunMail Backend, o servidor que alimenta o aplicativo de email RunMail, combinando simplicidade, eficiência e uma abordagem moderna para gerenciar sua caixa de entrada! 🚀

Este repositório contém o backend do aplicativo, desenvolvido com Java 22 e Spring Boot, responsável por gerenciar o envio, recebimento e controle de spam de emails, além de fornecer funcionalidades de configuração do usuário.

## 🎨 Tecnologias Principais
- **Java 22** - Linguagem principal para desenvolvimento.
- **Spring Boot** - Framework utilizado para criar a API RESTful.
- **MongoDB** - Banco de dados NoSQL para armazenamento de emails e configurações de usuário.
- **Maven** - Ferramenta para gerenciamento de dependências.

## 🌟 Funcionalidades
- **📩 Envio e recebimento de emails**: API eficiente para gerenciar a comunicação entre usuários.
- **🔍 Filtro de spam**: Algoritmo integrado para detectar e bloquear emails indesejados.
- **⚙️ Gerenciamento de usuários**: Cadastro e Consulta de preferências de usuários.

## 📲 Como Rodar o Projeto**

Clone o repositório:
```bash
git clone https://github.com/InimigosDoPrazo/runmailBackend.git
```    

Navegue até o diretório do projeto:
```bash
cd runmailBackend
``` 

Compile e rode a aplicação executando a classe RunmailApplication:

```bash
@SpringBootApplication
public class RunmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunmailApplication.class, args);
    }
}
```

O servidor estará disponível em http://localhost:8080.

📂 Estrutura do Projeto
plaintext
Copiar código
- ├── src/               # Código fonte
- │  ** ├── main/          # Aplicação principal
- │  ** └── test/          # Testes unitários
- ├── pom.xml            # Configurações de dependências do Maven
- └── README.md          # Documentação do projeto

- src/main: Contém o código Java para APIs e serviços.
- src/test: Contém os testes da aplicação.
- pom.xml: Arquivo de configuração do Maven para gerenciamento de dependências.

🚧 Próximos Passos
Estamos continuamente melhorando o RunMail Backend. Algumas das próximas features incluem:

Implementação de notificações em tempo real.
Suporte a múltiplas contas de email.
Melhorias no sistema de detecção de spam com algoritmos de aprendizado de máquina.


