![alt text](image.png)
🔬 Automação de testes de APIs REST com Java, JUnit 4 e RestAssured

## 📚 Material de Apoio

Este repositório foi desenvolvido como material complementar ao curso:

> **[Testando API REST com REST Assured](https://uciandt.udemy.com/course/testando-api-rest-com-rest-assured/)**  
> Disponível na plataforma **Udemy**.

### 🗂️ Estrutura do Projeto

```
CourseRestAssured/
│
├── src/
│   ├── main/java/br/ce/wcaquino/
│   │   ├── Main.java
│   │   └── OlaMundo.java
│   └── test/java/br/ce/wcaquino/
│       ├── OlaMundoTest.java
│       ├── UserJsonTest.java
│       └── UserXMLTest.java
│
├── .vscode/        # Configurações do VS Code (ignorado no Git)
├── .idea/          # Configurações do IntelliJ (ignorado no Git)
├── target/         # Arquivos gerados pelo Maven (ignorado no Git)
├── pom.xml         # Gerenciador de dependências Maven
└── README.md
```

## 🚀 Como Executar os Testes

✅ Pré-requisitos

☕ Java 17+

🧰 Maven 3.6+

🧪 (Opcional) VS Code com a extensão Java Test Runner

## ▶️ Via Maven
```
mvn test
```
▶️ Via VS Code
Abra o arquivo de teste desejado em src/test/java/br/ce/wcaquino/

Clique no botão "Run Test" acima do método ou da classe de teste


## 🧪 Principais Classes de Teste

| Classe              | Descrição                                                                 |
|---------------------|---------------------------------------------------------------------------|
| `OlaMundoTest.java` | Testes básicos de requisições `GET`, validação de status code, corpo da resposta e uso de matchers. |
| `UserJsonTest.java` | Validações em respostas `JSON`: campos, listas e cenários de erro.        |
| `UserXMLTest.java`  | Validações em respostas `XML`: nós, atributos e listas.                   |


✔️ Exemplos de Validações

✅ Status code da resposta

✅ Conteúdo do corpo da resposta

✅ Uso de Hamcrest Matchers

✅ Validação de listas e coleções

✅ Mensagens de erro e cenários negativos

```
Biblioteca	Versão
RestAssured	5.4.0
JUnit	4.13.2
Hamcrest	2.2
```

💡 Observações
🌐 Os testes utilizam endpoints públicos do domínio: restapi.wcaquino.me

📘 Projeto didático, ideal para estudos e práticas de automação

🧹 Pastas como .vscode/ .idea/ e target/ estão no .gitignore
