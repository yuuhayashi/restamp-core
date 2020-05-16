# ReStamp

## See

* [haya4-compress wiki](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki)

## License

* [MIT License](LICENSE.txt)

-------------------------------------------------------------------

## About
## 連番JPEGファイルのファイル更新時刻を書き換えます

**Restamp**は タイムラプス動画などから一定時間間隔の静止画像を取り出たときの静止画像ファイルの**ファイル更新時刻**を一括して書き換えるツールです。

**Restamp** を使ってタイムラプス動画から切り出した連番JPEGファイルの*ファイル更新日時*を撮影時刻に書き換える方法を紹介します。

* **Restamp** を使ってタイムラプス動画から連番JPEGファイルを切り出す方法については、
[Qiita](https://qiita.com/yuuhayashi@github/items/cdf4ceb064d483f29a35 "タイムラプス動画から静止画を切り出して、位置情報をつけて、Mapillaryサイトに一括アップする手順のまとめ")
を参照のこと

![restamp02](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/restamp02.png)



## 仕組み／原理

 * 一定間隔で撮影された連番ファイルの任意の２枚の撮影時刻を設定します。
 * ２枚の中間にあるファイルの数を数えて、各ファイルの撮影間隔を算出します。
 * 算出された撮影間隔と２枚の撮影時刻を元に、他のすべての撮影時刻を算出し、JPEGファイルのファイル更新日時(updatetime)を撮影時刻に書き換えます。

![restamp01](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/restamp01.png)

  もし、カメラの時刻合わせに失敗していたり、時間ウォーターマークを設定し忘れた場合は、[撮影時刻推定方法](https://github.com/yuuhayashi/Movie2jpg/blob/master/src/UPDATETIME.md) を参照してください。

時間ウォーターマークが設定されている場合は、下記の方法で抽出されたイメージファイルに撮影時刻を設定することができます。


## Download

* [Download](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/Download)


## Setup

* [セットアップ](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/Setup)


## Quick start

ターミナルからコマンドを叩いて実行

コマンドライン：

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


----

