# PROJETO USANDO JWT COM REFRESH TOKEN.

Esse projeto foi desenvolvido em Java.

## ⚙️ Código fonte

* [ms-authenticator-security](source/ms-authenticator-security)

## 🚀 Começando

Esse projeto tem como principal objetivo usar o JWT no fluxo de segurança.

## 🛠 Tecnologias usadas no projeto

<ol>
  <li>Java</li>
  <li>Intellij</li>
  <li>Postman</li>
  <li>Mysql</li>
  <li>Mysql Workbench</li>
  <li>Spring Boot</li>
  <li>Gradle</li>
</ol>

## 📋 Pré-requisitos

<ol>
  <li>Java</li>
  <li>Intellij</li>
  <li>Postman</li>
  <li>Mysql</li>
  <li>Mysql Workbench</li>
</ol>

## ⚙️ Arquitetura do projeto

![architecture/architecture.png](architecture/architecture.png)

## ⚙️ Criando o Banco de Dados no MySQL.

~~~~sql
CREATE DATABASE `db_security`;
~~~~

Não precisamos criar as tabelas, por que as mesmas já vão ser criadas pelo JPA quando executarmos o projeto, mas depois de criadas precisamos executar os inserts abaixo.

~~~~sql
INSERT INTO tb_roles(name) VALUES('ROLE_USER');
INSERT INTO tb_roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO tb_roles(name) VALUES('ROLE_ADMIN');
~~~~

## ⚙️ Realizando os cadastros de usuários. 

Deixe o projeto executando na sua IDE, e depois vamos fazer as chamadas no Postman.


#### 🔩️ Cadastrando usuário com a Role ROLE_USER. 

~~~~shell
curl --location --request POST 'http://localhost:8080/api/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "cicero_user",
    "email": "ciceroednilson_user@gmail.com",
    "password": "drte45@123",
    "role": [
        "ROLE_USER"
    ]
}'
~~~~

![imagens/user.png](imagens/user.png)

#### 🔩 Cadastrando usuário com a Role ROLE_MODERATOR. 

~~~~shell
curl --location --request POST 'http://localhost:8080/api/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "cicero_moderator",
    "email": "ciceroednilson_moderator@gmail.com",
    "password": "drte45@123",
    "role": [
        "ROLE_MODERATOR"
    ]
}'
~~~~

![imagens/moderator.png](imagens/moderator.png)

#### 🔩 Cadastrando usuário com a Role ROLE_ADMIN. 

~~~~shell
curl --location --request POST 'http://localhost:8080/api/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
"username": "cicero_admin",
"email": "ciceroednilson_admin@gmail.com",
"password": "drte45@123",
"role": [
"ROLE_ADMIN"
]
}'
~~~~

![imagens/admin.png](imagens/admin.png)

#### 🔩️ Validando os cadastros no banco de dados. 

Depois de cadastrar os usuários no banco de dados, devemos ver os registros como mostra a imagem abaixo.

~~~~sql
    SELECT * 
      FROM tb_user user 
INNER JOIN tb_user_roles user_roles ON user_roles.user_id = user.id
INNER JOIN tb_roles roles ON roles.id = user_roles.role_id
 ~~~~

![imagens/sql.png](imagens/sql.png)

## ⚙️ Realizando o login para obter o token. 

~~~~shell
curl --location --request POST 'http://localhost:8080/api/auth/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
"username" : "cicero_admin",
"password": "drte45@123"
}'
~~~~

![login-token.png](imagens/login-token.png)


## ⚙️ Realizando o refresh do token. 

No arquivo application.yml tem o tempo de expiração do token, nesse exemplo eu coloquei 2 minutos para poder testar o refresh.

~~~~shell
curl --location --request POST 'http://localhost:8080/api/auth/refreshtoken' \
--header 'Content-Type: application/json' \
--data-raw '{
"refreshToken": "ab1c4b7e-1488-4e34-a791-a32a5457153d"
}'
~~~~

![refresh-token.png](imagens/refresh-token.png)

## ⚙️ Tabela que guarda o refresh token. 

![tb_refresh_token.png](imagens/tb_refresh_token.png)

## ⚙️ Testando as rotas protegidas pelas roles.

Abaixo vamos ver os exemplos de rotas protegidas e de uma rota aberta.

#### 🔩️ Testando rota aberta.

~~~~shell
curl --location --request GET 'http://localhost:8080/api/test/all' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "cicero_moderator",
    "email": "ciceroednilson_moderator@gmail.com",
    "password": "drte45@123",
    "role": [
        "ROLE_MODERATOR"
    ]
}'
~~~~

![open-route.png](imagens/open-route.png)

#### 🔩️ Testando rota com role ROLE_ADMIN.

~~~~shell
curl --location --request GET 'http://localhost:8080/api/test/admin' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjaWNlcm9fYWRtaW4iLCJpYXQiOjE2Nzk5NjQ3NjEsImV4cCI6MTY3OTk2NDg4MX0.rzvjlZNZfKrP7dc_CCUVvxCVPcIGRSB1cuKiAtxTae8Al-SGUOJHQA5m7gJboQRmPF8o1ggxqMiiuJSZZMtl1g' \
--data-raw ''
~~~~

![route_admin.png](imagens/route_admin.png)

#### 🔩️ Testando outras rotas com outras roles.

Nesse projeto tem uma pasta com uma [collection](collection) para todos os testes no Postman, basta obter um token e realizar os testes para as rotas protegidas.

![collection.png](imagens/collection.png)


## 📌 Versão

1.0

## ✒️ Autor

Cícero Machado - ciceroednilson@gmail.com

