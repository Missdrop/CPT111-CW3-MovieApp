# 小组作业Tips

## 代码提交

- 首先，我们应该从原仓库分叉（fork）一份到自己的仓库。然后将fork的仓库clone到本地进行修改。

   > 一切修改都应该在自己的fork中进行。

- 在每完成一个功能 `（例如一个类方法或对一个类方法的更改/优化）` 后，做一次commit，并且用简单的语言描述修改或添加了什么。

- 在修改完成后，提交pull request到原仓库 `（在此之前先更新一下本地和fork仓库的版本，与原仓库同步）` ，代码审查通过后就会合并到原仓库。

   > 一个pull request最好只包含少量内容 `（例如一个类或者对一个类的更改/优化）` ，然后在request里写入清晰的更改内容以便审查。

## 代码规范

- 命名

   - 类名应采用大驼峰命名法（每个单词开头大写，例如FirstSecondThird。
   - 方法和变量名采用小驼峰命名法（首单词不大写，例如firstSecondThird）。
   - 包名全部使用小写字母。

- 格式

   - 前大括号不换行，例如：

      ```java
      void function() {
      }
      ```

      而非

      ```java
      void function()
      {
      }
      ```

   - 每个类方法之间空两行，功能分割的部分可以适当空一行增加可读性。

   - 缩进应为四个空格或者一个tab，在提交代码之前使用ide自带的格式化即可。

## 调试&编译

- 首先，请确保电脑安装了[**带有JavaFX的JDK21**](https://www.azul.com/downloads/?version=java-21-lts&package=jdk-fx#zulu)

- 项目使用了Gradle来进行编译。源代码存储在`app/src/main`中，测试类在`app/src/test`文件夹中。

   > 若要编写测试，请在测试文件夹中新建文件。编译时会自动测试所有测试类并给出报告。

- 资源文件夹使用相对路径，随执行时的目录而变化。在Gradle编译期间，测试用csv文件会自动在运行测试时生成，其保存在`app/resources`

- 编译时只需进入项目根目录，使用

   ```bash
   .\gradlew build
   ```

   > Windows环境下使用 `.\gradlew.bat`

   即可将项目编译为jar，位于`build/libs`目录下。

   - 此外，若要单独执行测试，使用
      ```bash
      .\gradlew test
      ```

      > 当然，也可以选择使用IDE的测试扩展进行测试。

   - 清理编译缓存：
      ```bash
      .\gradlew clean
      ```

- 双击jar文件或者使用

   ```bash
   java -jar "jar文件路径"
   ```

   即可运行编译后的jar文件。

## 用户密码

- 用户密码加密存储，实际密码为：

   | Username	| Password	|
   | ---------	| ---------	|
   | bob		   | bob123	   |
   | eric		| eric123	|
   | alice		| alice123	|
   | diana		| diana123	|
   | charlie	| charlie123|
