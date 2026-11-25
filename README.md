# 📚 Gerenciador de Autores e Obras - API REST

API RESTful completa desenvolvida em **Java 17** com **Spring Boot 3**, **Gradle**, **PostgreSQL** e **Spring Security com JWT**, desenvolvida como solução para o Desafio Técnico da **Javanauta**.

O sistema gerencia o cadastro e relacionamento de **Autores** e suas **Obras**, aplicando regras de negócio de integridade de dados, validação de documentos com Stella Caelum, relacionamento `@ManyToMany` com tabela associativa e segurança baseada em tokens JWT.

---

## 🛠️ Tecnologias e Ferramentas

- **Java 17**
- **Spring Boot 3.x**
- **Gradle** (Build e gerenciamento de dependências)
- **PostgreSQL** (Banco de dados relacional)
- **Spring Data JPA & Hibernate** (Persistência e ORM)
- **Spring Security & JJWT (io.jsonwebtoken)** (Autenticação e autorização Stateless com Bearer Token)
- **Bean Validation (Hibernate Validator)** (Validação de entrada de dados)
- **Caelum Stella Core** (Validação oficial de CPF brasileiro)
- **MapStruct** (Mapeamento de DTOs e atualização parcial de entidades)
- **Lombok** (Produtividade com redução de boilerplate)
- **OpenAPI 3 / Swagger UI** (Documentação interativa da API)

---

## 🏛️ Arquitetura do Projeto

O projeto foi estruturado seguindo uma arquitetura em camadas desacoplada e limpa:

```text
src/main/java/com/desafio/gerenciadorautoresobras
├── controller          # Controladores REST da aplicação
├── business            # Regras de negócio e casos de uso
│   ├── dto             # Objetos de transferência de dados (Request / Response)
│   ├── mapper          # Interfaces do MapStruct
│   └── service         # Serviços com a lógica de negócio
└── infrastructure      # Detalhes de infraestrutura e persistência
    ├── entities        # Entidades JPA (Autor, Obra, Usuario)
    ├── enums           # Enums de domínio (SexoEnum)
    ├── exceptions      # Tratamento global de exceções (@RestControllerAdvice)
    ├── repository      # Interfaces Spring Data JPA
    └── security        # Configurações de segurança, filtros e utilitários JWT
```

---

## 📋 Regras de Negócio Implementadas

### 👤 Autor
1. **Nome**: Obrigatório.
2. **Sexo**: Opcional (`MASCULINO`, `FEMININO`, `OUTRO`).
3. **E-mail**: Obrigatório, formato de e-mail válido e **único** no sistema (validado no cadastro e na atualização).
4. **Data de Nascimento**: Obrigatória, formato `dd/MM/yyyy` e não pode ser uma data futura.
5. **País de Origem**: Obrigatório.
6. **Regra Condicional do CPF**:
   - Se o país de origem for **Brasil**: o CPF é **obrigatório**, deve ser matematicamente válido (calculado via Caelum Stella) e **único** no banco.
   - Se o país de origem **NÃO for Brasil**: o autor não deve possuir CPF cadastrado.
7. **Exclusão Segura**: Não é permitido excluir um autor que possua obras vinculadas.

### 📖 Obra
1. **Nome**: Obrigatório.
2. **Descrição**: Obrigatória, com limite máximo de 240 caracteres.
3. **Data de Publicação**: Obrigatória, formato `dd/MM/yyyy`, não pode ser uma data futura.
4. **Data de Exposição**: Opcional, formato `dd/MM/yyyy`, não pode ser uma data futura se informada.
5. **Vínculo com Autores**: Toda obra deve ter **pelo menos um autor** associado. Todos os IDs de autores informados devem existir no banco.
6. **Relacionamento `@ManyToMany`**: Associação mapeada através da tabela intermediária `autor_obra`.

### 🔄 Atualização Parcial (PATCH / PUT Inteligente)
Implementada via MapStruct com a estratégia `NullValuePropertyMappingStrategy.IGNORE`. Caso o cliente envie apenas determinados campos para atualização (ex: apenas o nome), os dados previamente gravados não são sobrescritos com `null`.

### 🔐 Segurança com Spring Security e JWT
- Autenticação **Stateless** via cabeçalho `Authorization: Bearer <TOKEN>`.
- Senhas criptografadas no banco utilizando o algoritmo **BCrypt**.
- Endpoints públicos: `/usuario` (POST para cadastro), `/usuario/login` (POST para autenticação) e documentação Swagger.
- Todos os endpoints de Autores e Obras são protegidos e exigem token JWT válido.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- **Java 17** instalado.
- **PostgreSQL** instalado e em execução na porta `5432`.
- Criar a base de dados no PostgreSQL:
  ```sql
  CREATE DATABASE gerenciador_obras;
  ```

### 1. Clonar o repositório
```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd gerenciador-autores-obras
```

### 2. Configurar o banco de dados
Verifique o arquivo `src/main/resources/application.properties` e ajuste seu usuário e senha do PostgreSQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gerenciador_obras
spring.datasource.username=postgres
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### 3. Executar a aplicação
No terminal, execute o wrapper do Gradle:
```bash
# Windows
.\gradlew.bat bootRun

# Linux / Mac
./gradlew bootRun
```
A aplicação iniciará na porta padrão `8080`.

---

## 📌 Principais Endpoints da API

### 🔐 Autenticação (`/usuario`)
| Método | Endpoint | Descrição | Autenticação |
| :--- | :--- | :--- | :---: |
| `POST` | `/usuario` | Cadastrar novo usuário | Pública |
| `POST` | `/usuario/login` | Realizar login e receber o Token JWT | Pública |

### 👤 Autores (`/autores`)
| Método | Endpoint | Descrição | Autenticação |
| :--- | :--- | :--- | :---: |
| `POST` | `/autores` | Cadastrar autor | `Bearer Token` |
| `GET` | `/autores` | Listar todos os autores | `Bearer Token` |
| `GET` | `/autores/{id}` | Buscar autor por ID | `Bearer Token` |
| `PUT` | `/autores/{id}` | Atualizar dados do autor | `Bearer Token` |
| `DELETE`| `/autores/{id}` | Excluir autor (se não tiver obras) | `Bearer Token` |

### 📖 Obras (`/obras`)
| Método | Endpoint | Descrição | Autenticação |
| :--- | :--- | :--- | :---: |
| `POST` | `/obras` | Cadastrar nova obra vinculada a autores | `Bearer Token` |
| `GET` | `/obras` | Listar todas as obras com seus autores | `Bearer Token` |
| `GET` | `/obras/{id}` | Buscar obra por ID | `Bearer Token` |
| `PUT` | `/obras/{id}` | Atualizar obra ou autores vinculados | `Bearer Token` |
| `DELETE`| `/obras/{id}` | Excluir obra | `Bearer Token` |

---

## 👨‍💻 Autor

Desenvolvido por **Marcos** como parte do Desafio Técnico da Formação **Javanauta**.
