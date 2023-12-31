[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Creative+ 🌎
### Let your players create a world.

Creative+ - plugin, that allows players to create their worlds, set up them, build something, and visit other players worlds. Plugin is made for `PaperMC` version `1.16.5`.

[![N|Solid](https://media.discordapp.net/attachments/990341925922017350/1152482140697661561/image.png?width=798&height=600)

## Goals 🤔

### 🐸 Increase Java skills.

### 🤓 Try to create something like Creative+ from Mineland or DiamondFire

### 😎 Create a plugin for all servers

## Needs to be created 📜

- [x] **Создание мира**

- [x] **Меню со списками миров**

- [x] **Настройка своего мира**

- [x] Лимит миров на игрока

- [ ] World Templates

- [x] **Режим разработчика, кодинг и DEV-мир**

- [x] **Распределение прав участников на /build /dev**

- [ ] Much more...

## Команды

`/menu /games /worlds` - **Open**

`/world` - **Open world settings**

`/world delete` - **Delete world** (`creative.delete`) (for admins: `creative.deletebypass`)

`/join ID` - **Join the world**

`/ad` - **Share your world**

`/build` - **Launch world in build mode**

`/play` - **Launch world in play mode**

`/dev` - **Launch development world**

`/creative reload` - **Reload plugin** (`creative.reload`)

`/creative resetlocale` - **Reset locale messages.** Useful only when developing plugin (`creative.resetlocale`)

## Old versions

<details>
  <summary> Demo 1.1 | 2022 </summary>
  - Добавлен креатив-чат /cc (/cc on, /cc off)

  - Добавлено создание файлов миров. Файлы конфига миров хранятся в plugins/CreativeCoding/worlds/айдимира.yml
  Файл содержит в себе: название мира, описание мира, значок, ник владельца мира.

  - Теперь при командах /build, /coding delete, /dev, /play проверяется ник владельца мира. Чужой игрок без права на удаления чужих миров не сможет удалить не свой мир.

  - Владелец мира может сменить название, описание, значок мира в /coding settings.

  - Добавлена команда /ad (недоделана), если без аргументов рекламирует мир, если с аргументами то переходит в мир

  - Добавлена команда /join, такая же как и /ad.

  - Код немного переписан, чтобы сделать его более читабельным
</details>
<details>
  <summary> Demo 1.0 | 2022 </summary>
  Это первая версия плагина!

  - Создана основа для Креатива

  - Создано меню с мирами

  - Создана простая генерация плоского мира
</details>

## Support me

If you want to support me, then you can:

**👾 Contact me on Discord:** `.timeplay`

**💵 Gift me a money:** [studio.mineland.net/worlds/timeplay](https://studio.mineland.net/worlds/timeplay)

## License

Plugin is licensed by GNU GPL v3.0 (actually no certificate, but pls trust me)
So, you can download and modify source of plugin, and share it.
But when sharing a plugin you need to public source code on GitHub.

The GNU General Public License v3.0
https://www.gnu.org/copyleft/gpl.html
