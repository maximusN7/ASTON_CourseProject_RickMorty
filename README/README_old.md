Repository for Ignatev Maxim's Course Project for ASTON

Планируется Single-Activity App, поэтому создана разметка для MainActivity с нижним navigation bar для переключения между основными Fragments и контейнером для Fragments. 
Основными тремя фрагментами будут: 
- Фрагмент со списком персонажей (CharacterFragment)
- Фрагмент со списком локаций (LocationFragment)
- Фрагмент со списком эпизодов (EpisodeFragment)

На каждом из этих фрагментов располагаются SearchView для дальнейшего внедрения функции поиска, кнопка для открытия AlertDialog с фильтрами и RecyclerView для 
соответствующего списка.

Также, для каждого вида информации (персонаж, локация, эпизод) создан Fragment, в котором будут отображаться детали о выбранном посредством клика в списке
элемента.
Для персонажа это CharacterDetailsFragment, на котором расположены ImageView для аватара, 5 TextView для информации и 5 соответсвтующих им TextView, в которых поясняется, 
что за информация указана в соседнем TextView. Ниже контейнер для локации (origin) в виде элемента, визуально идентичного ячейке для RecyclerView, чтобы пользователь понимал, 
что элемент интерактивен. Аналогично для последней известной локации (location). Ниже располагается RecyclerView со списком эпизодов, в которых персонаж появлялся.
Для локации это LocationDetailsFragment, на котором расположены 3 TextView для информации и 3 соответсвтующих им TextView, в которых поясняется, что за информация 
указана в соседнем TextView. Ниже располагается RecyclerView со списком персонажей, которые находятся в этой локации.
Для эпизода это EpisodeDetailsFragment, на котором расположены 3 TextView для информации и 3 соответсвтующих им TextView, в которых поясняется, что за информация 
указана в соседнем TextView. Ниже располагается RecyclerView со списком персонажей, которые появлялись в этом эпизоде.

Для каждого вида информации была создана разметка ячейки для RecyclerView. 
Для персонажа это character_cell с LinearLayout в котором сверху вниз указаны ImageView с аватаром и 4 TextView с информацией об имени, виде, статусе и гендере.
Для локации это location_cell с LinearLayout в котором сверху вниз указаны 3 TextView с информацией об названии, типе и измерении.
Для эпизода это episode_cell с LinearLayout в котором сверху вниз указаны 3 TextView с информацией об названии, номере эпизода и дате выхода.

Для всех TextView и Buttons доавлены строковые ресуры в файл strings.

Дизайн максимально упрощенный и будет отполирован в конце работы при достаточном остатке времени.

Добавлено переключение трех основных фрагментов посредством нижней навигационной панели. Попытка сделать это в виде архитектуры MVVM.

Добавлен splash screen с иконкой приложения в качестве фона к SplashActivity. Автоматически меняется при полной инициализации приложения.

Загрузка в RecyclerView Для CharacterFragemnt происходит с CharacterModel. По запросу из CharacterViewModel загруженые по url данные передаются в LiveData, 
которая прослушивается в CharacterFragment. В данный момент на загрузку данных дается 1 секунда, затем это будет изменено с учетом coroutines.

Данные в CharacterDetailsFragment поступают из CharacterDetailsModel, где по ссылке и id загружатся из сети. Данные в RecyclerView со списоком персонажей поступают также 
из CharacterDetailsModel. На данном этапе отображаются только те данные, которые не зависят от эпизодов. Работает навигация с RecyclerView со списком персонажей 
в CharacterDetailsFragment.

Загрузка в RecyclerView Для LocationFragemnt происходит с LocationModel. По запросу из LocationViewModel загруженые по url данные передаются в LiveData, 
которая прослушивается в LocationFragment. В данный момент на загрузку данных дается 1 секунда, затем это будет изменено с учетом coroutines.

Данные в LocationDetailsFragment поступают из LocationDetailsModel, где по ссылке и id загружатся из сети. Данные в RecyclerView со списоком персонажей поступают также 
из LocationDetailsModel. Работает навигация с RecyclerView со списком локацийв LocationDetailsFragment.

Загрузка в RecyclerView Для EpisodeFragemnt происходит с EpisodeModel. По запросу из EpisodeViewModel загруженые по url данные передаются в LiveData, 
которая прослушивается в EpisodeFragment. В данный момент на загрузку данных дается 1 секунда, затем это будет изменено с учетом coroutines.

Данные в EpisodeDetailsFragment поступают из EpisodeDetailsModel, где по ссылке и id загружатся из сети. Данные в RecyclerView со списоком персонажей поступают также 
из EpisodeDetailsModel. Работает навигация с RecyclerView со списком эпизодов EpisodeDetailsFragment.

Навигация между элементами работает полностью. На трёх главных Fragment системная кнопка назад закрывает приложение. При переходе по нескольким Fragments серией нажатий 
на элементы переходы заносятся в BackStack. При нажатии при этом на системную кнопку назад, возвращается предыдущий закрытый Fragment до тех пор, пока не дойдет до одного 
из трёх главных Fragments. При переходе с любого другого Fragment на один из главных через нижнюю навигационную панель BackStack очищается.

Добавлена кнопка Back в ToolBar. Отображается на DetailsFragments и действует аналогично системной кнопке Back. Title изменяется в соответствие с текущим Fragment.

Пагинация добавлена для трех основных экранов со списками. Заменены Model на PagingSource для этих списков. Заменены адаптеры для RecyclerView на PaginationAdapter.
На странице 20 элементов, максимум для пагинации 120 элементов одновременно.

Добавлены индикаторы загрузки на главынх страницах - пока не загрузилась первая страница пагинации списка. На каждый элемент - пока не загрузился элемент. На каждый 
аватар - пока не загрузился аватар. При быстрой прокрутке - внизу списка, пока загружается следующая страница пагинации. В случае ошибки загрузки страницы пагинации 
появляется надпись об ошибке вместо индикатора прогресса.

Добавлена функция pull-to-refresh на всех Fragment. До полноценного внедрения coroutines полноценная работа только на фрагментах со списками. При выполнении занового загружается
RecyclerView и его элементы.

Добавлено 3 Диалога, вызываемых по кнопкам Show Filter на Фрагментах со списками. В диалогах указаны возможные фильтры для запросов по спискам.

Добавлено кэширование для трех основных фрагментов со списками. Через RemoteMediator определяется откуда брать данные - из сети или из базы данных.

Работа с данными из network API на всех Fragments. 

Кэширование данных добавлено на всех экранах. На главных экранах со списком кэшируется через RemoteMediator. При переходе на детали кэшируются все данные, которые отображены
на экране дополнительных данных. Всё хранится в базе данных с 3 основными таблицами для Character, Location и Episode соответственно + 3 Join таблицы для хранения пар 
ключ-ключ для связи данных на экране деталей.

Фильтрация и поиск поддерживается на всех трех экранах со списками. Поиск осуществляется через EditText в верхней части экрана. Остальные фильтры перечислены в AlertDialog,
открывающемся по нажатию на кнопку в верхней части экрана. Чтобы применить фильтр нужно активировать соответствующий CheckBox. Без этого данные не будут учитываться при 
фильтрации.

Отображение надписи об отсутствии данных для офлайн режима, если база данных пустая для данного листа. Реализовано для трех основых экранов и для экранов с деталями для списков 
персонажей и эпизодов соответсвующих экранов деталей.

Изменен шрифт, налажена ночная и обычная темы приложения. В диалогах с фильтрами нужно нажать CheckBox, чтобы увидеть поля выбора фильтров.

Базовое внедрение DI при помощи Dagger 2. 