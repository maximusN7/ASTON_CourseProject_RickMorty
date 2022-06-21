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