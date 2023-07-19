# Strava
Приложение “My Strava” - клиент для сервиса Strava.<br/>

Strava - это сервис для отслеживания активности спортсменов с помощью мобильных устройств и социальная сеть для спортсменов,<br/>
где спортсмены могут сравнивать свои результаты, ставить цели, общаться и т. д.<br/>

<b>Функционал приложения:</b><br/>
Для пользователя, который авторизовался в приложении с помощью аккаунта Strava:<br/>

- Просмотр тренировок пользователя;
- Просмотр детальной информации о тренировке;
- Добавление новой тренировки пользователя или редактирование существующей тренировки;
- Просмотр информации о профиле пользователя;
- Оффлайн-режим;
- Логаут (выход из аккаунта пользователя).


<b>Стек:</b>
- XML-разметка & Compose
- Single activity
- Single module
- Coroutines
- MVVM
- MVI Kotlin
- Room
- Retrofit
- AppAuth (авторизация по стандарту OAuth 2.0)
- Koin
- Clean Architecture

<br/>
После февраля 2022 года работать с сайтом strava.com надо через VPN.<br/>
Зарегистрируйтесь как разработчик по ссылке <a href="https://www.strava.com/settings/api">https://www.strava.com/settings/api</a><br/>
и получите CLIENT_ID и CLIENT_SECRET.<br/>
В файл keystore.properties необходимо записать Ваши CLIENT_ID и CLIENT_SECRET.<br/>

<br/>
<kbd>
  <img src="/screenshots/OnBoarding.png"> 
</kbd>
&#160;
<kbd>
  <img src="/screenshots/Training_list.png"> 
</kbd>
&#160;
<kbd>
  <img src="/screenshots/Training_detail.png"> 
</kbd>

<br/>
<br/>

<kbd>
  <img src="/screenshots/Training_edit.png"> 
</kbd>
&#160;
<kbd>
  <img src="/screenshots/Sport_picker.png"> 
</kbd>
&#160;
<kbd>
  <img src="/screenshots/Date_picker.png"> 
</kbd>

<br/>
<br/>

<kbd>
  <img src="/screenshots/Time_picker.png"> 
</kbd>
&#160;
<kbd>
  <img src="/screenshots/Duration_picker.png"> 
</kbd>
&#160;
<kbd>
  <img src="/screenshots/Profile.png"> 
</kbd>

 