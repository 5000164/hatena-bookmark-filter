# hatena-bookmark-filter

## 使い方

- リポジトリを clone する
- アプリケーションの設定を行う
    - common/SettingsSample.settings をコピーして common/Settings.settings を作成する
    - common/Settings.settings に任意の内容を設定する
- 実行ファイルを生成する

```bash
sbt assembly
```

- DB をセットアップする

```bash
cd /path/to/hatena-bookmark-filter && java -jar migration/target/scala-2.12/migration.jar
```

- 一定期間ごとに動作するように cron を設定する

```
0,20,40 * * * * cd /path/to/hatena-bookmark-filter && java -Dsettings=common/Settings.settings -Dlogback.configurationFile=collect/src/main/resources/production/logback.xml -jar collect/target/scala-2.12/collect.jar
5,25,45 * * * * cd /path/to/hatena-bookmark-filter && java -Dsettings=common/Settings.settings -Dlogback.configurationFile=post/src/main/resources/production/logback.xml -jar post/target/scala-2.12/post.jar
```
