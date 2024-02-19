# MandaCaru Broker API

## Descrição
A Mandacaru Broker API é uma aplicação Spring Boot que fornece operações CRUD (Create, Read, Update, Delete) para gerenciar informações sobre ações (stocks). Com ela os usuários podem listar, criar, atualizar e excluir informações sobre ações disponíveis, permitindo uma experiência simplificada de negociação de ações.

## Recursos

### Listar Todas as Ações
Retorna uma lista de todas as ações disponíveis.

**Endpoint:**
```http
GET /stocks
```

### Obter uma Ação por ID

Retorna os detalhes de uma ação específica com base no ID.

**Endpoint:**
```http
GET /stocks/{id}
```

### Criar uma Nova Ação
Cria uma nova ação com base nos dados fornecidos.

**Endpoint:**
```http
POST /stocks
```
**Corpo da Solicitação (Request Body):**
A solicitação é composta por três campos: symbol, companyName e price.
* `symbol` deve ser composto exatamente por três letras seguidas de um número;
* `companyName` deve, obrigatoriamente, ser preenchido;
* `price` deve ser um valor positivo e não nulo.

Exemplo de solicitação:
```JSON
{
  "symbol": "BBR3",
  "companyName": "Banco do Brasil SA",
  "price": 56.97
}

```
### Atualizar uma Ação por ID
Atualiza os detalhes de uma ação específica com base no ID.

**Endpoint:**
```http
PUT /stocks/{id}
```
**Corpo da Solicitação (Request Body):**

```JSON
{
  "symbol": "BBR3",
  "companyName": "Banco do Brasil SA",
  "price": 59.97
}

```

### Excluir uma Ação por ID
Exclui uma ação específica com base no ID.

**Endpoint:**
```http
DELETE /stocks/{id}
```


## Uso
1. Clone o repositório: `git clone https://github.com/adrianwilker/mandacarubroker.git`
2. Importe o projeto em sua IDE preferida.
3. Configure o banco de dados e as propriedades de aplicação conforme necessário.
4. Execute o aplicativo Spring Boot.
5. Acesse a API em `http://localhost:8080`.

## Requisitos
- Java 11 ou superior
- Maven
- Banco de dados

## Tecnologias Utilizadas
- Spring Boot
- Spring Data JPA
- Maven
- PostgreSQL

## Contribuições
### Fork do Repositório:
1. Vá para a [página principal deste repositório](https://github.com/adrianwilker/mandacarubroker);
2. Clique em "Fork" no canto superior direito da tela ([mais informações aqui](https://help.github.com/articles/fork-a-repo));
3. Você será redirecionado para a sua própria cópia do projeto em https://github.com/seu-usuario/mandacarubroker

### Clone o Repositório
1. Abra o terminal no diretório onde o projeto será salvo;
2. Clone o repositório forkado usando o comando abaixo:
```git clone https://github.com/seu-usuario/mandacarubroker.git```
Não esqueça de substituir `seu-usuario` pelo seu nome de usuário do GitHub.

### Crie uma Branch
No terminal, dentro do diretório do projeto, crie uma nova branch:
```git checkout -b add/seu-usuario```

### Desenvolva a Funcionalidade ou Correção
Escreva seu código, seguindo as boas práticas de codificação e documentação conforme necessário.

### Commit e Push
Faça commit das suas alterações e envie a branch para o seu repositório forkado no GitHub.
```
git add .
git commit -m "Mensagem"
git push origin add/seu-usuario
```
Lembre-se de adicionar uma mensagem simples e direta que resuma as suas alterações no código.

### Envie um Pull Request (PR)
Quando estiver pronto para revisão, envie um PR do seu repositório forkado para o repositório principal do mandacarubroker. Certifique-se de descrever claramente o que sua contribuição faz e as principais alterações feitas.

### Merge
Se seu PR for aprovado ele será mesclado na branch principal.

## Licença
Este projeto está licenciado sob a [Licença MIT](LICENSE).

