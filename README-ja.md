# ReStamp

**Restamp**は タイムラプス動画などから一定時間間隔の静止画像を取り出たときの静止画像ファイルの**ファイル更新時刻**を一括して書き換えるツールです。

* Java application. (java 8 以降)

* コマンドラインでの操作. 
 * GUI版はこちら ... [ReStamp-gui](http://surveyor.mydns.jp/gitbucket/yuu/ReStamp-gui) ([日本語Wiki](http://surveyor.mydns.jp/gitbucket/yuu/ReStamp-gui/wiki))

* [Restamp wiki](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki) (日本語wikiページ)

-------------------------------------------------------------------

## ライセンス

* [MIT License](LICENSE.txt)


## セットアップ

* [Setup](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/Setup)


## ダウンロード

* [Download](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/Download)


## クイックスタート

コマンドターミナルでの操作

起動方法:

* ダウンロードした '' ファイルがあるディレクトリに移動する
  ```
  $ cd （ダウンロードしたJARファイルがあるディレクトリ）
  ```

* プログラムを起動する
  ```
  $ java  -jar ReStamp-xx.xx.jar <argv[0]> <argv[1]> <argv[2]> <argv[3]> <argv[4]>
  ```
 または
  ```
  $ java  -jar ReStamp-xx.xx.jar <argv[0]> <argv[1]> <argv[2]> <argv[3]> <argv[4]> <arg[5]>
  ```

* パラメータ

| パラメータ番号 | 名称 | 説明 |
|:-----------:|:------------|:------------|
| argv[0]     |  imgDir |     画像ファイルが格納されているディレクトリ<br/>  例：「`./img/100MEDIA `」    |
| argv[1]     | baseFile1  | 時刻補正の基準とする画像ファイル |
| argv[2]     |  baseTime1 | 基準画像ファイルの撮影日時 "yyyy-MM-dd HH:mm:ss JST" <br/>例：「`2019-09-01 16:26:51 JST`」 |
| argv[3]     | baseFile2 | 時刻補正の基準とする画像ファイル |
| argv[4]     | baseTime2  | 基準画像ファイルの撮影日時 "yyyy-MM-dd HH:mm:ss  JST" <br/>例：「`2019-09-01 16:26:51 JST`」|
| argv[5]     |  outputDir |  (option)変換済み画像ファイルの出力フォルダ.<br/>省略した場合は元画像を直接上書きする |

