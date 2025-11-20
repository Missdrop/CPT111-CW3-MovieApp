# 项目文档

除了本文档外，项目也使用JavaDoc在重要的类和方法添加了注释，你可以参考这里[编译JavaDoc](README.md#编译javadoc)

## 目录
- [项目文档](#项目文档)
  - [目录](#目录)
  - [项目结构](#项目结构)
  - [更新日志](#更新日志)
    - [V1.0](#v10)
    - [V1.1](#v11)
    - [V1.2](#v12)
    - [V1.3](#v13)
    - [V1.4](#v14)
    - [V1.5](#v15)
  - [具体说明](#具体说明)
    - [总览](#总览)
    - [异常包](#异常包)
      - [PasswordValidationException](#passwordvalidationexception)
      - [UsernameValidationException](#usernamevalidationexception)
    - [电影包](#电影包)
      - [Movie](#movie)
    - [用户数据包](#用户数据包)
      - [Watchlist](#watchlist)
      - [History](#history)
    - [用户包](#用户包)
      - [User](#user)
      - [PremiumUser](#premiumuser)
      - [BasicUser](#basicuser)
    - [数据存储包](#数据存储包)
      - [FileManager](#filemanager)
      - [MovieManager](#moviemanager)
      - [UserManager](#usermanager)
    - [推荐包](#推荐包)
      - [Engine](#engine)
      - [Sort](#sort)
    - [主类](#主类)
      - [App](#app)
    - [GUI包](#gui包)
      - [GUI逻辑示意图](#gui逻辑示意图)
    - [其他](#其他)
      - [测试类](#测试类)
  - [功能实现（CW3要求）](#功能实现cw3要求)
    - [未登录时](#未登录时)
    - [登录后](#登录后)
    - [其他](#其他-1)
    - [技术要求](#技术要求)
    - [高级功能](#高级功能)
    - [其他你可以想到的功能](#其他你可以想到的功能)

---

## 项目结构

```text
├── exception               - ⑧ 异常类
│   ├── PasswordValidationException
│   └── UsernameValidationException
│
├── movie                   - ① 电影类
│   ├── <待实现>
│   └── Movie.java
│
├── recommendation          - ⑤ 推荐引擎
│   ├── Sort.java
│   └── Engine.java
│
├── storage                 - ④ 数据存储
│   ├── FileManager.java    - 数据库与文件的IO
│   ├── MovieManager.java   - 电影数据库类
│   └── UserManager.java    - 用户数据库类
│
├── user                    - ③ 用户类
│   ├── BasicUser.java
│   ├── PremiumUser.java
│   ├── User.java
│   └── data                - ② 用户数据的基本类型
│       ├── History.java
│       └── Watchlist.java
│
├── userinterface           - ⑦ 存放各个GUI
│   ├── AddNewMovie.java
│   ├── BrowseMovies.java
│   ├── GetPremium.java
│   ├── Recommendation.java
│   ├── ViewHistory.java
│   ├── ViewWatchlist.java
│   ├── ChangePassword.java
│   ├── Login.java
│   ├── Menu.java           - 登陆后的菜单
│   └── Register.java
│
└── App.java                - ⑥ 主类，包含主页的GUI
```

---

## 更新日志

### V1.0

- 首个功能完全的版本，确定了整体架构和逻辑。

### V1.1

- 将User和Manager类会抛出异常的String[]构造器去除，改为让Manager类处理。
- 精细化并一些异常处理，将所有Exception改为更精细的异常类。

### V1.2

- 去除User类的isPremium字段，改为用instanceOf判断子类。
- 更改User类为抽象类，并且构造器改为protected，添加了三个抽象方法，进一步封装了User类。
- 添加exception包，用于处理User类的密码和用户名异常。
- 删去所有UserManager类的验证方法，改为在密码/用户名有问题时抛出异常。

### V1.3

- 更改了UI包中一些类的字段，修改了页面直接相互打开的方法`show()`。
- 重设密码时隐藏Menu，若关闭则退回Menu，重设密码成功后退回App页面。
- Movie的Rating限制了格式为一位小数。
- MovieManager类增加添加时判断电影是否已存在（除Rating其他字段不区分大小写，内容相同）。
- 将MovieManager一些boolean方法改为void并增加抛出异常。
- User类增加一个getUserType方法以代替instanceof
- 现在user.csv文件第五个字段为usertype，可以为Basic或Premium

### V1.4

- 主要为性能优化：
- UI类删除多余的私有字段。
- MovieManager类增加getMovieList方法。
- Engine类使用ArrayList的sort方法排序。
- Engine类存储tempMovieList，不重复新建列表。
- History类新增getDate方法。
- 将List，Set，Map声明为接口类。

### V1.5
- 按课程要求删除GUI中所有Lambda表达式，改为用匿名类实现。
- 删除推荐引擎中List的sort方法，调用新增类Sort类，再传入内部匿名类排序。
- 推荐包新增Sort类。

## 具体说明

这个项目大致上由七部分组成，如上图所示。每一个包我会从类字段，构造器方法和类方法三方面来讲解。

---

### 总览

在运行这个项目的时候，我预期的行为应该是：

- 弹出一个主页，可以登录或者注册。
- 登录之后，打开菜单，其中用户可以浏览电影（高级用户可以往数据库添加新电影），然后把它们添加到观看列表（高级用户可以存储最大一百条，普通用户二十条）或者历史记录。
- 浏览观看列表和历史记录；获取推荐（普通用户只可以按评分推荐，高级用户可以选择年份，流派，评分三种推荐方式）。
- 更改密码和退出登录；把账户转变为高级账户。
- 在每次操作之后，数据库应该更新。
- 假如csv文件是空的，应该新建csv文件。
- 假如csv中数据存在问题，软件应该自动抛弃这些有问题的数据。

经我测试，这些功能应该已经全部实现。但还需要进行更多测试。

---

在这个项目里定义了两种数据类，分别为电影和用户类。

因为课程要求我们把它们的数据存储在两个csv文件里，所以这两个类都实现了一个`toCSV`方法，返回一个可以直接写入到csv文件里的`String`，以便在存储的时候调用这个方法。

关于数据读取，我们从csv文件中读取的通常是`String`型，要把它们转化为对应的电影或者用户对象对这两个类的构造器方法有要求。所以两个类都实现了用一个`String[]`变量的构造方法，以下我们会细讲。

---

### 异常包

由V1.2更新时加入，用于处理用户名或密码的异常。

#### PasswordValidationException

密码不合法异常，当密码不符合要求时应该抛出这个异常。

#### UsernameValidationException

用户名不合法异常，当用户名不符合要求或者重复时应该抛出这个异常。

### 电影包

这个包目前只有电影类，但是CW3的高级功能中存在一项“将 Movie 子类化为不同的类”，有主意的同学可以帮忙想想。

#### Movie

  - 私有字段：Movie类有这些私有字段：`String id`, `String title`, `String genre`, `int year`, `double rating`
  - 构造器方法：Movie类比较简单，仅有一个构造方法。
    - 构造方法对各种异常做了判断，并且可能抛出`IllegalArgumentException`。
  - 类方法：
    - 五个私有字段的`getter`
    - 一个`toCSV`方法，把每个私有字段转化为用逗号分开的String，用于数据存储

---

### 用户数据包

> 实际上在最初我并没有编写这个包，因为没有它程序也可以写的很清楚。但是课程要求实现这个包的内容。

这个包实现了两个类，`History`类和`Watchlist`类，它们都将作为用户类的私有字段使用。

#### Watchlist

首先从比较简单的Watchlist类开始，它实际上是对一个`HashSet<String>`的包装，私有字段也仅有这一项。因为Watchlist不应该存在重复元素，所以使用HashSet，HashSet可以自动阻止加入重复元素，也可以获得更快的IO和查询性能。

- 私有字段：`HashSet<String> watchlist`
- 构造器方法：Watchlist类有三种构造器：
  - 一个不接受参数的构造器，生成一个空的Watchlist
  - 一个接受`HashSet`对象的构造器
  - 一个接受`String[]`型的构造器，会将数组的每一项加入到Watchlist中。
    > 很显然，这个方法会在读取csv文件的时候被使用。
- 类方法：一些基本的操作方法，包括：获取私有字段，往列表加入电影（当电影存在时不会重复加入，会返回false），移除电影，获取列表长度，查询电影是否存在于列表。
  - 一个`toCSV`方法，和之前不同的是用分号隔开。

#### History

然后是History类。这个类我使用HashMap实现，以MovieID作为键，时间作为值，因为电影名和观看时间的键值对应用Map这种数据结构再适合不过了。虽然HashMap会导致观看历史乱序的问题，但是我们用表格显示，可以按字符串排序，所以并不影响用户体验。

同时，HashMap可以避免重复电影加入，当相同电影不同时间的历史记录被加入时，HashMap会自动更新键而不改变值。

- 私有字段：`HashMap<String, String> history`
- 构造器方法：同Watchlist形式。
  - 接受`String[]`的构造器会把每一项（形如"M001@2025-01-01"）拆分成movieID和date两个字符串，分别作为表的键和值存储。其中包含一些异常处理，比如当拆分出的字符串数组长度小于2时（缺少元素），抛弃这个异常数据不存储。
- 类方法：一些基本方法和`toCSV`方法，同Watchlist。另外的，`add()`方法会使用`java.time.LocalDate`包的`now()`方法获取时间，并且转换成字符串储存为值。
- V1.4更新：`getDate`方法：输入MovieID字符串，输出观看时间字符串。

---

### 用户包

这个包包含三个类，User，PremiumUser和BasicUser。

实现了一个用户应有的各种操作，并记录了用户的数据。

它们的关系是：后两个都继承自User类，但是两种User的最大Watchlist长度不同，PremiumUser为100，BasicUser为20。

#### User

User类看似内容很多但其实并不复杂。实现了用户的密码加密，数据IO，验证密码。

V1.2更新：User是一个抽象类，且构造器方法为protected，只可被本包调用。

- 私有字段：`String username`, `String passwordHash`, `Watchlist watchlist`, `History history`。代码的可读性很高，想必不需要解释就可以理解字段的意思。唯一需要解释的就是passwordHash字段，它采用加密的哈希值存储密码。下面会详细解释。
- 构造器方法：两个，一个构建全新用户，一个从数据构建已存在用户。
  - 比起Movie类，它多了一种构造器，只需要输入两个参数：`username, password`。少了watchlist和history，显而易见的，这是建立一个新用户用的方法。
    - V1.2更新：构造器调用密码和用户名的Setters，所以当密码或用户名不符合要求时，构造器会抛出异常。
- 类方法：除去一些基本的getter和setter，需要解释的还有以下几个方法：
  - `toHash`方法：会将密码字符串先调用`String.hashCode()`方法，再将其转化为十六进制数字，返回为字符串格式。
    > 此方法设为private static，是因为它仅仅被本类调用，而且与用户对象本身无关，是用户类的一个辅助方法。
  - `setPassword`方法：会调用toHash方法将明文密码字符串转化为加密密码字符串后再储存密码。
    - V1.2更新：密码不符合要求时会抛出异常。
  - `verifyPassword`方法：检查传入的密码是否和用户密码匹配。
  - `addToHistory`方法：这个方法会在电影加入history时将其从watchlist中移除。这是课程要求。
    > 但是课程要求中并没有对已经在history里的电影被加入watchlist的行为有限制。事实上根据现实情况，我认为想看两遍电影很正常，所以允许上述操作。
- V1.2更新：
  - `setUsername`方法：设置用户名，当用户名不符合要求时会抛出异常，目前仅被构造器调用所以设为private。
- V1.2更新：User类有三个抽象方法
  - `int getMaxWatchlistSize`：获取观看列表最大长度。
  - `boolean canAddMovies`:是否可以增加电影，这个方法会被`userinterface.BrowseMovies`调用。
  - `List<String> getAvailableRecommendationTypes`：一个字符串列表，包含推荐引擎可以选用的类型。
- V1.3更新：增加抽象方法`String getUserType`：获取用户类型。

#### PremiumUser

User的子类，重写了addToWatchlist方法。使得用户观看列表长度得到了限制。

- 私有字段：添加了`MAX_WATCHLIST_SIZE`常量，值为100，设为`private static final int`是因为它应是个不可改变且所有用户统一的常量，作为最大观看列表长度使用。
  - V1.2更新：添加了`AVAILABLE_RECOMMENDATION_TYPES`常量，包含有`"rating", "genre", "year"`。
- 构造器方法：不同的是多了一个从现有用户创建高级用户的构造器，另外两个构造器和User类的区别仅仅是将isPremium设为了true。
- 类方法：一个getter，并且重写了`addToWatchlist`方法，使观看列表达到最大长度后不再加入电影，同时返回false，否则true。
  - V1.2&V1.3更新：重写了toCSV方法，调用父类方法，并且在末尾拼接上了",Premium"来填充premium字段。

#### BasicUser

与PremiumUser的唯一区别是`MAX_WATCHLIST_SIZE`为20，`AVAILABLE_RECOMMENDATION_TYPES`只有"rating"，其他完全相同。

---

### 数据存储包

这个包实现了一个数据管理系统，从底层的文件IO到上层用户和电影的数据库。

FileManager仅提供与csv文件交互的基本方法。

MovieManager和UserManager继承自FileManager类，针对Movie和User的csv文件，并且添加了一个HashMap字段，用于保存所有的用户或电影数据。

#### FileManager

这个类提供了csv文件的IO方法。文件IO通常包含大量异常捕获，因此处理这个类需要格外小心。同时，在往不存在的文件写入时应尝试创建新的文件。

- 私有字段：一个File对象，和一个Scanner对象用于读取数据。
- 构造器方法：可以通过File对象或者一个包含文件目录的String值构造FileManager。
- 类方法：
  - `Scanner getScanner()`：由当前文件获取一个新的Scanner对象。
  - `String[] nextLine()`：这个方法会获取csv文件的一行，然后按逗号把字符串分割为数组，最后返回这个数组。
  - `boolean hasNextLine()`：是否存在下一行。
  - `boolean save(String header, String[] rows)`：这个方法比较关键，具体逻辑和异常处理可以自行查看源代码，它会替换性的写入整个csv文件，以header为csv文件的第一行，字符串数组rows为csv文件的数据行。也就是先写入header行，然后遍历所有rows，写入进文件里。
    > 当文件不存在时这个方法会试图创建新文件和文件夹。
  - `close()`：清空FileManager的字段。


#### MovieManager

MovieManager继承自FileManager，在实例化的时候从csv读取电影数据，然后变成一个用于管理电影的数据库系统，提供了一些查找，增添电影的方法。

- 私有字段：
  - `HashMap<String, Movie> movies`：一个哈希表，用于存储所有电影，键为电影ID，值为一个Movie对象。
    > 哈希表的好处无需多言，但总觉得这样的设计会不会有些多余，因为电影ID既被存储在键中，又被存储在Movie对象中。但是再去改Movie对象的数据结构太麻烦了，牵一发而动全身，UserManager类也一样。
  - `int maxIndex`：记录当前最大的电影索引，用于生成新的电影ID
- 构造器方法：直接使用相对地址`resources/movies.csv`调用父类构造器，然后调用两个私有方法初始化私有字段。
- 类方法；除去一些基本getters，先说一些私有方法：
  - `createFromCSV`：从csv文件获取的String数组构建movie对象，设为static是因为它不需要本类元素参与。做了一些异常判断，会抛出`IllegalArgumentException`。
  - `getMovies()`：无需多言，调用父类的方法来读取csv文件，用获取到的String数组调用`createFromCSV`方法创建movie对象，然后把电影ID和movie对象保存在HashMap里。
  - `idToIndex`：把电影id（形如M023，M+至少三位数字，可以被写成"M%03d"的形式）转换为int，也就是读取M后面的数字。
  - `getMaxIndex()`：遍历所有ID（Map的键），用上面的`idToIndex`方法获取数字id，然后找出最大的数。
- 然后是一些公有方法：
  - `getMovie`：从ID获取对应Movie对象
  - `addMovie`：用Movie对象或者Movie的数据增加电影同时自动生成MovieID。
    - V1.3更新：判断了电影是否重复（除Rating其他字段不区分大小写，内容相同），否则抛出异常。
  - `deleteMovie`（多态）：用movieID或者movie对象删除Map中的电影。
  - `save()`：调用父类save方法，设置header为`"id,title,genre,year,rating"`，遍历所有movie，使用`.toCSV()`转化为String，再储存为一个数组作为父类save方法第二个参数。
- V1.4更新：
  - `getMovieList()`：获取一个包含所有电影的ArrayList。



#### UserManager

UserManager基本上和MovieManager差不多，所以很多东西不再讲了。

- 私有字段：一个存储用户的哈希表
- 构造器方法：相对地址为`resources/users.csv`构造父类对象，初始化哈希表
- 类方法：与MovieManager相同的不再赘述，不一样的是：
  - `updateUser`：用一个新的用户对象替代表中旧的用户对象。因为采取先删除再增加这个对象的方式，所以无需处理异常。它是绝对不可能报错的。
  - `save()`方法header为`"username,password,watchlist,history,usertype"`，其他一样。
  - V1.2&V1.3更新：`getUsers`方法会根据usertype字段的数据判断将用户实例化为PremiumUser或BasicUser。
- 值得重点一提的是三个返回检查方法：
  - `authenticate`：输入用户名和密码，判断能否登录，返回一个布尔值
  - `checkUsername`：检查用户名是否符合要求，是则返回null，否则返回一个提示的字符串
  - `checkPassword`：检查密码是否符合要求，返回值同上

---

### 推荐包

包含一个Engine，实现了一个根据用户数据，按多种方式的推荐器。推荐引擎的算法非常简单，下面会说明。
V1.5更新：增加一个Sort类，实现了Quicksort算法，供Engine调用。

#### Engine

引擎类应该可以通过现有的电影库和用户数据，然后传入两个参数：推荐类型和推荐数量，返回一个排序后的推荐列表。

- 私有字段：一个电影表HashMap（同MovieManager），一个用户喜欢的电影HashSet，一个暂存的排序ArrayList。
- 构造器方法：没什么好说的，传入两个参数构建对象。
- 类方法：可以看到这个类只有一个公有方法也就是`recommendation`，其他的都是作为辅助的私有方法。
  - `recommendation`：一个多态方法，在缺少参数时，默认推荐类型为rating或者推荐数量为5。
    - 推荐引擎返回一个String数组，它首先经过特定类型推荐算法（下面的私有方法），然后按照rating高低排序。假如推荐类型就是rating，那只需按rating排序。
  - `HashSet<String> getLikedMovies()`：获取用户喜欢的电影（观看列表+历史记录），使用HashSet可以自动排除两个列表都有的情况。
  - `int getFavouriteYear()`：获取用户最喜欢的年份，算法为将用户喜欢的电影年份取平均数。
  - `HashMap<String, Integer> getFavouriteGenreMap()`：获取一个表，其中键是电影ID，值是一个分数，这个分数实际上就是用户喜欢的电影里出现了几次这个流派。比如说用户看过三次Action电影，那么Action的值就为3。
  - `void<String> getFavouriteYearMovies()`：将tempMovieList按照以下规则排序：绝对值（电影年龄-用户最喜欢年份）。这可以让越接近用户喜欢的年份的电影排在越前面。
  - `void<String> getFavouriteGenreMovies()`：同上，但是排序方法变为查询`FavouriteGenreMap`，并比较两个电影流派哪个值更高。实则就是查看哪种电影用户看的更多。
  - `void<String> getTopRatedMovies()`：按照Rating排序，并不需要调用别的方法。
    > 值得一提的是，所有排序都是用`movieDatabase.entrySet().stream().sorted(<一个用于比较两个元素的Lambda函数>)`的形式。首先将哈希表转化为一个包含所有entry的Set，然后把这个集合转化为流，再调用流的排序方法，按自定义排序规则排序。

#### Sort

本类实现了快速排序算法，并且有一个内部抽象类Comparator，用于按不同的方式排序。

---

### 主类

从这里开始将进入GUI部分。请先对JavaFx有一定了解再往下看。

~~主类被命名为App实际上是因为Gradle自动生成的。~~

#### App

App类继承自Application类（所有JavaFx的GUI都应该继承这个类）。

- 私有字段：一个UserManager对象，在类加载时就被初始化。设为static是为了防止打开多个userManager导致错误的修改。其他任何GUI的userManager对象都应该由App类传入。
- 类方法：
  - `main`方法：整个程序的入口。会调用父类的launch方法（launch方法会自动加载init，start，stop方法）创建GUI。在GUI运行结束后，会save并close掉userManager对象（将修改后的用户数据写入csv）。
  - `start`方法：有一个标题，和两个按钮，分别可以打开Register和Login页面。页面由一个VBox（Vertical Box，元素竖直排列的Box）套着一个HBox（Horizon Box，元素水平排列的Box）组成。HBox里是两个按钮，VBox里是上面为标题，下面为HBox。

---

### GUI包

这个包的内容过于复杂，因此不对代码详细解释，只描述每个GUI的行为。

类似于App类，Menu类也有一个私有字段：static的MovieManager对象，在类加载时就被初始化。其他任何GUI的menuManager对象都应该由App类传入，以避免修改错误。

#### GUI逻辑示意图
```text
┌─────────────────┐
│    主页 (App)    │
└─────────┬───────┘
    ┌─────┴─────┐
┌───▼───┐   ┌───▼───┐
│ 登录   │   │ 注册   │
│Login  │   │Register│
└───┬───┘   └───┬───┘
    └─────┬─────┘
    ┌─────▼─────┐
    │  主菜单    │
    │   Menu    │
    └─────┬─────┘
    ┌─────┼───────┬──────┬──────┬──────┐
┌───▼─┐ ┌─▼───┐ ┌─▼──┐ ┌─▼──┐ ┌─▼──┐ ┌─▼───────┐
│浏览 │ │ 观看 │ │历史 │ │推荐│ │更改 │ │ 升级账户  │
│电影 │ │ 列表 │ │记录 │ │系统│ │密码 │ │（高级用户）│
└─────┘ └───┬─┘ └────┘ └────┘ └────┘ └──────────┘
      ┌─────▼─────┐
      │ 添加新电影  │
      │（高级用户） │
      └───────────┘

```

---

### 其他

#### 测试类

测试类包含一些测试方法，在运行storage包的测试类时，会自动生成一个测试用csv文件。

测试类并没有测试所有方法，这可能是因为：

1. 我认为这个类方法功能非常简单，不可能出问题。
2. 这个方法是私有方法，它的成功与否可以从测试调用它的方法得到。
3. 忘记测试了。

总而言之，测试类可能存在一些测试不充分的问题，而且GUI是没法通过测试类测试的。需要更多同学来帮助增加测试类或者测试程序。（而且课程有要求）

## 功能实现（CW3要求）

见[CW3要求](CPT111-CW3-2526.pdf)

### 未登录时

- [x] 登录：使用用户名和密码登录
- [x] 退出：退出程序

### 登录后

- [x] 浏览电影：列出文件中存储的所有电影。
- [x] 将电影添加到观看列表：将电影添加到用户的个人观看列表。
- [x] 从观看列表中移除电影：从用户的个人观看列表中移除电影。
- [x] 查看观看列表：列出用户个人观看列表中的所有电影。
- [x] 将电影标记为已观看：将电影添加到用户的个人观看历史中。此外，如果该电影目前位于用户的个人观看列表中，则应将其从观看列表中移除。
- [x] 查看历史：列出用户个人观看历史中的所有电影。
- [x] 获取推荐：根据用户的观看列表和/或观看历史，列出用户的前 N 个推荐电影。
- [x] 登出：退出用户账户。

### 其他

- [x] 每个用户都有一个个人观看列表和观看历史。
- [x] 用户数据（所有用户）存储在一个 CSV 文件中
- [x] 电影存储在一个 CSV 文件中
- [x] 推荐算法（当用户选择“获取推荐”时运行）应该是简单的，并且应该显示前 N 个推荐电影（其中“N”是传递给推荐函数的参数）。例如，它可以根据用户最常观看的流派推荐电影。

### 技术要求

- [x] 使用面向对象原则。至少应实现以下类：Movie、User、Watchlist、History 和 RecommendationEngine。
- [x] 使用 ArrayList 和/或 HashMap 存储电影和用户数据。
- [x] 使用文件 I/O 来加载电影数据和加载/保存用户数据。
- [x] 实现异常处理以防止崩溃。
- [x] 约束：你必须只使用本课程中涵盖的 Java 库。

### 高级功能

- [x] 创建新用户账户的功能。
- [x] 更改用户密码的功能。
- [x] 一个支持多种策略（例如，按类型、年份、评分等推荐）且可在运行时切换的推荐引擎。
- [x] 一个使用 JavaFX 的图形用户界面（GUI），用于浏览电影、管理观看列表和查看推荐（替代命令行菜单）。
- [x] 将 User 子类化为 BasicUser、PremiumUser 等，每个子类具有不同的权限。
- [ ] 将 Movie 子类化为 FeatureFilm、ShortFilm、Documentary 等。
- [x] 在将用户密码保存到 CSV 文件之前进行哈希（加密）的功能。
### 其他你可以想到的功能

- [x] 更改用户权限
- [x] Premium用户可以创建新电影
