# ОПИСАНИЕ - „HotelReservation“
HotelReservation - это обычный RESTful web проект на Spring boot, который предоставляет API для управления системой бронирования в отеле.
-

Этот проект нацелен на закрепление приобретенных навыков технологий такие как:
<ul>
    <li>Java 17</li>
    <li>Spring Boot 3.5.6</li>
    <li>Spring Data JPA + Hibernate</li>
    <li>PostgreSQL</li>
    <li>Docker</li>
    <li>Validation</li>
</ul>

---
# Демонстрация некоторых операций.
## Создание сущности.
-----------------------
 Отправим post запрос на сервер с помощью платформы `postman` для работы с API

![create](https://github.com/AndrewLuminous/HotelReservation/blob/main/images/postman1.png)

 ✅ Нам вернулся JSON объект, с измененным кодом ответа `201 created `



## Пометка об измении статуса бронирования.

---
В реализации данного проекта было решение
бизнес логикой не удалять, а помечать об отмене бронирования

![cancel](https://github.com/AndrewLuminous/HotelReservation/blob/main/images/Peek%201.gif)

🔷 В данном случае нам вернулась такая же сущность, но теперь со статусом `CANCELLED`
 
## Поиск по id пользователя.
---
❗Также была произведена работа с возращением конкретного случая ошибки в виде JSON
с временем и датой
![notfound](https://github.com/AndrewLuminous/HotelReservation/blob/main/images/postman2.png)

![found](https://github.com/AndrewLuminous/HotelReservation/blob/main/images/postman3.png)

---
# Остальное
Также покажу контейниризацию баз данных и структуру проекта по фичам

![docker](https://github.com/AndrewLuminous/HotelReservation/blob/main/images/docker.png)

![idea](https://github.com/AndrewLuminous/HotelReservation/blob/main/images/idea.png)
