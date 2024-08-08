# **Тестовое задание для Effective Mobile**

### **Инструкция по запуску**

Должен быть установлен Git и Docker. 
Сперва нужно склонировать репозиторий, перейдя в командной строке в папку, где будет располагаться проект и введя в командной строке следующие команды:

`git clone https://github.com/sbnetaa/EffectiveMobileTestTask.git
cd EffectiveMobileTestTask`

Дальше варианты действий разнятся в зависимости от способа запуска - в контейнерах Docker или без них.

##### **А) В контейнерах**

Находясь в основной папке приложения выполните команду для очистки и сборки приложения: 
`mvn clean package`
А затем для сборки образов и запуска контейнеров:
`docker-compose up --build`
Для последующей остановки контейнеров без их удаления:
`docker-compose stop`:
Или для остановки с удалением контейнеров:
`docker-compose down`

##### **Б) Без контейнеров**

Необходимо импортировать проект в IDE (зависит от конкретной IDE)
Для Eclipse:

*File>Import...>Maven>Existing Maven Project*
нажать *Next* и выбрать основную папку приложения.


Далее необходимо в файле *pom.xml* закомментировать зависимость (приношу свои извинения - не успел справиться с проблемой) например следующим образом:
`<!--<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-docker-compose</artifactId>
	<scope>runtime</scope>
	<optional>true</optional>
</dependency>-->`

Затем необходимо создать по одной базе данных PostgreSQL для тестов и для основной логики и прописать параметры подключения к ней в *src/main/resources/application.properties* и *src/test/resources/application-test.properties*

Затем выполнить *Project>Clean* и затем в *Project Explorer* или *Package Explorer* щелкнуть правой кнопкой мыши на проекте и выбрать *Maven>Update Project*. Далее запустить как любое другое приложение

### **Использование**

Для тестирования можно использовать Postman. Подробное описание API можно посмотреть по адресу *http://localhost:8080/swagger-ui/index.html*

При успешном запуске любым способом приложение будет доступно по адресу *http://localhost:8080/*. 
Сперва необходимо зарегистрироваться: 
По адресу *http://localhost:8080/auth/register/* отправляется POST запрос следующего вида:
`{
	"email" : "testuser@mail.ru",
	"password" : "qwerty",
	"passwordConfirm" : "qwerty"
}
`

В ответ должно прийти сообщение
`Пользователь успешно зарегистрирован`
либо информация об ошибке.
Далее необходимо отправить POST запрос по адресу *http://localhost:8080/auth/login* следующего вида:

`{
	"email" : "testuser@mail.ru",
	"password" : "qwerty",
}
`
В ответ придет сообщение с токеном, который необходимо вставить в качестве значения заголовка для ключа `Authorization`, перед самим токеном добавив "*Bearer *" без кавычек и с пробелом. Должен получиться заголовок следующего вида:
`Authorization : Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaXJzdHBlcnNvbjJAbWFpbC5ydSIsImlhdCI6MTcyMjk2Mjg3MCwiZXhwIjoxNzIzMDQ5MjcwfQ.nsFEzxUVuG4smPzNQlddjUQkB7abuw0AVe_ZvOPEN9k`
Аутентификация завершена и можно приступать к использованию.

 
