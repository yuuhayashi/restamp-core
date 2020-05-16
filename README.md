# ReStamp


This is a tool for collectively rewriting the ** file update time ** of still image files when extracting still images at regular time intervals from time-lapse movies.

* Java application. (java 8 later)

* Command line interface.

* [Restamp wiki](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki) (Japanese wiki)

-------------------------------------------------------------------

## License

* [MIT License](LICENSE.txt)


## Setup

* [Setup](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/Setup) 


## Download

* [Download](http://surveyor.mydns.jp/gitbucket/yuu/Restamp/wiki/Download)


## Quick start

Command line interface.

How to start:

* move to directory which download jar file.
  ```
  $ cd （ダウンロードしたJARファイルがあるディレクトリ）
  ```

* start command
  ```
  $ java  -jar ReStamp-xx.xx.jar <argv[0]> <argv[1]> <argv[2]> <argv[3]> <argv[4]>
  ```
 または
  ```
  $ java  -jar ReStamp-xx.xx.jar <argv[0]> <argv[1]> <argv[2]> <argv[3]> <argv[4]> <arg[5]>
  ```

* command paramerters

| parameter | name    | discription |
|:-------:|:----------|:------------|
| argv[0] | imgDir    | source folder<br/>  exp.`./img/100MEDIA `|
| argv[1] | baseFile1 | image file for base time 1 |
| argv[2] | baseTime1 | base time1 "yyyy-MM-dd HH:mm:ss JST"<br/>exp. `2019-09-01 16:26:51 JST` |
| argv[3] | baseFile2 | image file for base time 2 |
| argv[4] | baseTime2 | base time 2 "yyyy-MM-dd HH:mm:ss JST"<br/>exp. `2019-09-01 16:26:51 JST`|
| argv[5] | outputDir | (option)output folder.<br/>If omitted, overwrite the source image file. |


----

### See

* [Qiita](https://qiita.com/yuuhayashi@github/items/cdf4ceb064d483f29a35)"タイムラプス動画から静止画を切り出して、位置情報をつけて、Mapillaryサイトに一括アップする手順のまとめ"

