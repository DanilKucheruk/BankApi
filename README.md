# BankApi

## Описание


## Установка
Чобы запустить приложение следуйте этим инструкциям:

1. Скачайте репозиторий с GitHub, содержащий файлы Dockerfile и docker-compose.yml.

2. Убедитесь, что у вас установлен Docker на вашем компьютере. Если Docker не установлен, вы можете скачать его с официального сайта Docker.

3. Откройте терминал или командную строку и перейдите в папку скачанного репозитория.

4. Выполните команду `$ docker-compose up --build` , чтобы создать образы сервисов на основе Dockerfile и запустить сервисы.

! В случае ошибки при первом запуске серисов выполните команду `$ docker-compose up` !

После запуска сервисы будут доступны и готовы к использованию.


Примечание: Если вы хотите остановить и удалить контейнеры, выполните команду `$ docker-compose down`.


## Использование

Для просмотра Swagger документации перейдите по пути 'http://localhost:8080/swagger-ui/index.html#/'

Для использования развернутого REST Api достаточно совершать HTTP запросы к нему(например, через Postman).

Регистрация пользователя: POST запрос по адерсу http://localhost:8085/api/registration. В теле запроса указать:

`
{
  "fullName": "John Doe",
  "login": "john.doe",
  "birthDate": "1990-01-01",
  "phones": [
    {
      "phoneNumber":"1234567890"
    }
  ],
  "emails": [
    {
      "emailAddress": "john.doe@example.com"
    }
  ],
  "password": "secret",
  "initialDeposit": 1000.0
}
`

Авторизация пользователя: POST запрос по адерсу 'http://localhost:8085/api/auth'. В теле запроса указать:
`{
  "login": "john.doe",
  "password": "secret"
}
`

В случае успешного исхода в ответ придет token.

Получение всех клиентов: GET запрос по адерсу 'http://localhost:8085/api/clients'.

Получение всех клиентов по фильтрам и с соритрокой:
	Пример 1: 'http://localhost:8085/api/clients/search?email=john.doe@example.com'
	Пример 2: 'http://localhost:8085/api/clients/search?phone=777777'


Удаление клиента: DELETE запрос по адерсу 'http://localhost:8085/api/clients/1'.


Добавление мобильного телфона :  POST запрос по адерсу 'http://localhost:8085/api/clients/1/phones'.

В теле запроса указать:
'
{
  "phoneNumber": "88888888888"
  "clientId": 2
  }
'


Удаление мобильного телфона :  DELETE запрос по адерсу 'http://localhost:8085/api/clients/1/phones/2'.


Добавление email:  POST запрос по адерсу 'http://localhost:8085/api/clients/1/emails'.
В теле запроса указать:
'
{
  "emailAddress":88888888888"
  "clientId": 2
  }
'


Удаление email:  DELETE запрос по адерсу 'http://localhost:8085/api/clients/1/emails/2'.


Получить список аккаунтов: GET запрос по адерсу 'http://localhost:8085/api/accounts'.


Перевод денег: POST запрос по адерсу 'http://localhost:8085/api/accounts/transfer?fromId={client1Id}&toId={client2Id}&amount={amount}'.


Получить аккаунт по id: GET запрос по адерсу 'http://localhost:8085/api/accounts/{Id}'.

