# Rangiffler

Приветствую тебя, мой дорогой друг!
Если ты это читаешь - то ты собираешься сделать первый шаг в написании диплома QA.GURU Advanced.
Далее я опишу основные направения работы, но помни, что этот диплом - не шаблонная работа, а место
для творчества - прояви себя! Be like Rangiffler!

Кстати, Rangiffler - произошло от названия северных оленей - Rangifer. Мы выбрали именно такое
название для этого проекта - потому, что он про путешествия, а северный олень - рекордсмен по
преодолеваемым расстояниям на суше. Путешествуй, будь как Rangiffler!

# Что будет являться готовым дипломом?

Тут все просто, диплом глоабльно требует от тебя реализовать три вещи:

- Реализовать бэкенд на микросервисах (Spring boot)
- Реализовать полноценное покрытие тестами микросервисов и фронтенда (если будут какие-то
  unit-тесты - это большой плюс!)
- Красиво оформить репозиторий на гихабе, что бы любой, кто зайдет на твою страничку, смог понять,
  как все запустить, как прогнать тесты

# С чего начать?

Мы подготовили для тебя полностью рабочий front-end, а так же страницы регистрации и логина. Кроме
того, у тебя есть и простой бэкенд - по сути своей, мок. В этом бекенде есть контроллеры, по которым
можно понять, какие микросервисы вам предстоит реализовать. И самое главное - у тебя есть проект
rangiffler, который будет выступать образцом для подражания в разработке микросервисов. Тестовое
покрытие rangiffler, однако, является явно слабым - учтите это при написании тестов на Rangiffler - это
все-таки диплом для SDET / QA Automation и падать в грязь лицом с десятком тестов на весь сервис
точно не стоит. Итак, приступим!

#### 1. Запусти фронт Rangiffler, для этого перейти в соответсвующий каталог

```posh
Dmitriis-MacBook-Pro rangiffler % cd rangiffler-client
```

#### 2. Обнови зависимости и запускай фронт:

```posh
Dmitriis-MacBook-Pro rangiffler-client % npm i
Dmitriis-MacBook-Pro rangiffler-client % npm start
```

Фронт стартанет в твоем браузере на порту 3001: http://127.0.0.1:3001/

#### 3. Запустите бэкенд rangiffler-gateway

```posh
Dmitriis-MacBook-Pro rangiffler % ./gradlew :rangiffler-gateway:bootRun
```

Бэк стартанет на порту 8080: http://127.0.0.1:8080/

# Что дальше?

#### 1. В первую очередь, необходимо реализовать сервис rangiffler-auth

Фронтенд полностью готов к использованию сервиса на порту 9000,
твоя задача взять сервис rangiffler-auth и аккуратно переделать его для работы с rangiffler.
Страницы логина / регистрации, а так же стили и графику мы даем:

- deer-logo.svg
- favicon.ico
- styles.css
- login.html
- register.html

Основная задача - аккуратно заменить упоминания о rangiffler в этом сервисе, а в идеале - еще и
разобраться, как он работает. В этом будет полезно
видео [Implementing an OAuth 2 authorization server with Spring Security - the new way! by Laurentiu Spilca](https://youtu.be/DaUGKnA7aro)

#### 2. Как только у вас появилось уже 2 сервиса, есть смысл подумать о докеризации

Чем раньше у ва получится запустить в докере фронт и все бэкенды, тем проще будет дальше.
На самом деле, докеризация не является строго обязательным требованием, но если вы хотите в будущем
задеплоить свой сервис на прод, прикрутить CI/CD, без этого никак не обойдется

#### 3. Подумать о необходимых микросервисах.

У вас должен остаться основной бэкенд, куда будет ходить фронт, но он будет играть роль прокси,
проверяющего вашу аутентифкацию. Все, как и в rangiffler. Это значит, что основная логика уйдет в свои
микросервисы со своими БД. На мой вззгляд, здесь будут уместны сервисы rangiffler-photo,
rangiffler-geo, rangiffler-users. Возможно, у вас другие мысли, какие микросервисы создать - вы
можете проявить свою фантазию

#### 4. Выбрать протоколо взаимодействия между сервисами

В поставляемом фронтенде классический REST. Его можно поменять на GraphQL - но это потребует
переписывания фронта, и тебе придется делать это самому. Поэтому я бы посоветовал оставить между
фронтом и rangiffler-gateway старый добрый REST. А вот взаимодействие между микросервисами можно
делать как угодно! REST, gRPC, SOAP. Делай проект я, однозначно взял бы gRPC - не писать руками кучу
model-классов, получить перформанс и простое написание тестов. Стоит сказать, что здесь не
понадобятся streaming rpc, и все ограничится простыми унарными запросами. Однако если вы хотите
использовать REST или SOAP - я не буду возражать

#### 5. Реализовать микросервисный бэкенд

Это место где, внезапно, СОВА НАРИСОВАНА!
На самом деле, концептуально и технически каждый сервис будет похож на что-то из rangiffler, поэтому
главное внимательность и аккуратность. Любые отхождения от rangiffler возможны - ты можешь захотеть
использовать, например, NoSQL базы или по другому организовать конфигурацию / структуру проекта -
никаких ограничений, лишь бы сервис выполнял свое прямое назначение

#### 6. Подготовить структуру тестового "фреймворка", подумать о том какие прекондишены и как вы будете создавать

#### 7. Реализовать достаточное, на твой взгляд, покрытие e-2- тестами

#### 8. Оформить все красиво!











