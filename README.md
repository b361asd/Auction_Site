# BuyMe Auction Site

[![Java CI](https://github.com/b361asd/Auction_Site/actions/workflows/maven.yml/badge.svg)](https://github.com/b361asd/Auction_Site/actions/workflows/maven.yml)

BuyMe is an implementation of a web auction site, similar to the likes of Ebay.
It is implemented using Java, [JSP](https://en.wikipedia.org/wiki/JavaServer_Pages) and MySQL.

The project goals are listed in [Project_Goals.md](Project_Goals.md).

## Prerequisites

-   Java 8+
-   [Apache Maven](https://maven.apache.org/)
-   MySQL Workbench
-   MySQL Server
-   [Apache Tomcat](https://tomcat.apache.org/)

## How to run

### Docker

Run

```console
$ docker build -t auction_site .
```

to build the image. To start it, run

```console
$ docker run -p 8888:8080 auction_site
```

Navigate to

```
http://localhost:8888/auctionsite
```

### Local Development

If locally, run:

```console
$ mvn install
```

The `.war` file will be in the `target` directory.

## License

[MIT](LICENSE)
