# BuyMe Auction Site

BuyMe is an implementation of an auction site, similar to the likes of Ebay.
It is implemented using Java, [JSP](https://en.wikipedia.org/wiki/JavaServer_Pages) and MySQL.
The project goals are listed in the file [Project_Goals.md](Project_Goals.md).

## Prerequisites
* Java 8
* MySQL Workbench
* MySQL Server
* [Apache Tomcat](https://tomcat.apache.org/)
* For IDEs, either [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/) or the [Eclipse IDE for Enterprise Java Developers](https://www.eclipse.org/downloads/packages/release/2019-12/r/eclipse-ide-enterprise-java-developers) are preferred. 

## How to run

`.class` files from Java compilation are purposely included to facilitate easy startup.

To get the project running.
1. Make sure Tomcat and Java 8 is installed and running.
2. Clone the project
3. `cd` into the `Webcontent` folder
4. `zip` all files into a `.zip` file and rename the extension into `.war`
5. Open Tomcat in the browser. The url is usually `localhost:8080`
6. Click `Manager App` and enter your username and password.
7. Under `WAR file to deploy`, select the `.war` file created.
8. Under `Applications`, you should see the name of the `.war` file created. Click on that to enter the application.

# License
[MIT](LICENSE)
