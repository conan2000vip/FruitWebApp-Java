# HealthLog Web Application

## 概要

本プロジェクトは、Java 17 と Spring Boot 3.x を使用して開発する健康管理Webアプリケーションです。
ユーザーはブラウザ上で健康データを登録・確認・編集・削除することができます。

## 使用技術

* Java 17
* Spring Boot 3.x
* Maven
* MySQL 8.0
* HTML / CSS / JavaScript
* Bootstrap
* Spring Security
* Git / GitHub
* Eclipse

## 開発環境

本プロジェクトを実行するために、以下の環境を使用します。

| 項目          | バージョン   |
| ----------- | ------- |
| Java        | 17      |
| Spring Boot | 3.x     |
| Maven       | 3.x     |
| MySQL       | 8.0     |
| IDE         | Eclipse |

## 環境構築

### 1. Java 17の確認

```bash
java -version
```

以下のように Java 17 が表示されることを確認します。

```text
openjdk version "17.x.x"
```

### 2. Mavenの確認

```bash
mvn -version
```

### 3. MySQLの確認

```bash
mysql --version
```

### 4. データベース作成

MySQLにログインします。

```bash
mysql -u root -p
```

プロジェクト用のデータベースを作成します。

```sql
CREATE DATABASE health_app CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## データベース設定

`src/main/resources/application.properties` に以下のように設定します。

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/health_app?useSSL=false&serverTimezone=Asia/Tokyo&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

※ `your_password` には自分のMySQLパスワードを設定します。
※ パスワードなどの個人情報をGitHubに公開しないよう注意してください。

## 実行方法

プロジェクトフォルダで以下のコマンドを実行します。

```bash
mvn spring-boot:run
```

起動後、ブラウザで以下のURLにアクセスします。

```text
http://localhost:8080
```

## GitHub初期設定

ローカルプロジェクトをGitで管理します。

```bash
git init
git add .
git commit -m "Initial commit"
```

GitHubのリポジトリと接続します。

```bash
git branch -M main
git remote add origin https://github.com/username/repository-name.git
git push -u origin main
```

## 注意事項

* Java 17を使用すること
* Spring Boot 3.xを使用すること
* MySQLのデータベースを事前に作成すること
* GitHubにパスワードや秘密情報を公開しないこと
* EclipseでもJDK 17を設定すること
