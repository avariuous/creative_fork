[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Creative 🌎
### Плагин Minecraft, который позволяет игрокам создавать и посещать миры.

Creative - это плагин, который позволит игрокам создавать миры, настраивать их, строить что-то, а так-же посещать миры других игроков. Он делается для серверов с ядром `PaperMC` версии `1.16.5`.

#### ⚠️ Важно! Эта версия плагина не стабильна и является демонстрационной.
Этот плагин кишит багами и он явно не претендует на "лучше чем другие креативы".

[![N|Solid](https://media.discordapp.net/attachments/310217885203300358/1050849486886408212/image.png)](https://media.discordapp.net/attachments/310217885203300358/1050849486886408212/image.png)

## Цели создания плагина 🤔

### 🐸 Улучшить свои навыки по программированию на Жабе.

### 🤓 Попробовать сделать что-то похожее на Креатив+ с Mineland.

### 😎 Обеспечить сервера хотя-бы каким-то бесплатным плагином на Креатив с мирами и кодингом.

## Необходимо сделать 📜

- [x] **Создание мира**

- [x] **Меню со списками миров**

- [x] **Настройка своего мира**

- [ ] Лимит миров на игрока

- [ ] Правильная генерация (шаблоны миров + генерация земли только до границ мира)

- [ ] Режим разработчика, кодинг и DEV-мир

- [ ] Распределение прав участников на /build /dev

- [ ] И многое другое...

## Команды

`/creative create` - **Создание мира** (`creativecoding.create`)

`/creative settings` - **Открыть настройки мира** (`creativecoding.menu`)

`/creative menu` - **Открыть меню со всеми мирами** (`creativecoding.menu`)

`/creative delete` - **Удалить мир** (`creativecoding.delete`) (для админов: `creativecoding.deletebypass`)

`/join ID` - **Подключиться к миру**

`/ad` - **Прорекламировать мир**

`/build` - **Запустить мир в режиме строительства**

`/play` - **Запустить мир в режиме игры (бесполезно)**

`/dev` - **Запустить мир в режиме кодинга (бесполезно)**

`/creative reload` - **Перезапустить плагин** (`creativecoding.reload`)

## Обновление 1.2 ALPHA

Команда /ad теперь кликабельна и имеет заддержку в 120 секунд
В коде самого плагина появился объект Plot, который означает мир с плотом. Он имеет параметры в виде названия, описания, значка, владельца мира.
Исправлены некоторые ошибки

## Обновление 1.1 DEMO

Добавлен креатив-чат /cc (/cc on, /cc off)

Добавлено создание файлов миров. Файлы конфига миров хранятся в plugins/CreativeCoding/worlds/айдимира.yml
Файл содержит в себе: название мира, описание мира, значок, ник владельца мира.
Скорее всего не будет работать на Линуксе, ведь я использовал символы \ для определения пути. Если действительно не работает, напишите пожалуйста.

Теперь при командах /build, /coding delete, /dev, /play проверяется ник владельца мира. Чужой игрок без права на удаления чужих миров не сможет удалить не свой мир.

Владелец мира может сменить название, описание, значок мира в /coding settings.
Пожалуйста не кликайте по смене значка мира, если в вашем курсоре нет предмета, иначе значок запишется как AIR и тогда произойдет слом плагина.
Возможно в каких-то обновлениях это пофиксится.

Добавлена команда /ad (недоделана), если без аргументов рекламирует мир, если с аргументами то переходит в мир

Добавлена команда /join, такая же как и /ad.

Код немного переписан, чтобы сделать его более читабельным

## Помочь проекту

Если вы хотите мне чем-то помочь, то вот:

**👾 Discord:** `.timeplay`

**💵 Подарить деньги:** [studio.mineland.net/worlds/timeplay](https://studio.mineland.net/worlds/timeplay)

## Лицензия

Плагин использует лицензию GNU GPL v3.0
То есть, вы можете скачать и изменить исходный код этого плагина, а после этого ещё и распространить.
Однако при распространении вам следует выложить тоже изменённый вами исходный код плагина на GitHub.

The GNU General Public License v3.0
https://www.gnu.org/copyleft/gpl.html
