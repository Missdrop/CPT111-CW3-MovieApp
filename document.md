# 项目文档

## 项目结构

```text
├── movie                   - 电影类
│   ├── <待实现>
│   └── Movie.java
│
├── recommendation          - 推荐引擎
│   └── Engine.java
│
├── storage                 - 数据存储
│   ├── FileManager.java    - 数据库与文件的IO
│   ├── MovieManager.java   - 电影数据库类
│   └── UserManager.java    - 用户数据库类
│
├── user                    - 用户类
│   ├── BasicUser.java
│   ├── PremiumUser.java
│   ├── User.java
│   └── data                - 用户数据的基本类型
│       ├── History.java
│       └── Watchlist.java
│
├── userinterface           - 存放各个GUI
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
└── App.java                - 主类，包含主页的GUI
```

## 功能实现（CW3要求）

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
