# homework-tabmo-api

Solution for the Homework Tabmo API - Part One

## Technologies :
  - SBT 1.5.2
  - Scala 2.13.6
  - Spark 3.2.0
  - Play Framework with MVC model 2.8.8



## 1- Project dependencies  :


<code>libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test</code><br>
<code>libraryDependencies += "org.apache.spark" %% "spark-core" % "3.2.0"</code><br>
<code>libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.2.0"</code><br>
<code>libraryDependencies += "org.apache.spark" %% "spark-hive" % "3.2.0"</code><br>



## 2- REST API  :

I used Play framework for processing incoming HTTP Requests into action calls with Controllers and a specific routes definition.

In this application rather than use a memory storage, we’ll store the movies list items in database.

The application provide several endpoints (list, add, getByGenre, computeByYear)

#### 2.1. Setup the Project

<code> > sbt new playframework/play-scala-seed.g8 </code>

#### 2.2. Create a Controller

I created a controller class in the app/controllers directory.

```
@Singleton
class MovieController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
```

The new class extends BaseController and has a constructor that’s compatible with it.

I used the @Inject annotation to instruct the Play Framework to pass the required class dependencies automatically. And, i marked the class as a @Singleton so that the framework will create only one instance. This means it will reuse it for every request.

#### 2.3. Handle the Request in Scala

Example of method that will be called when our server receives a REST request.

```
  def countByYear: Action[AnyContent] = Action { implicit request =>
    // Result in Json Format
    val result = Reader
      .read(DatabasePath, schema)
      .groupBy(col("year"))
      .agg(count(col("title")).as("moviesCount"))
      .as[CountByYear]
      .collect
      .toList
    Ok(Json.toJson(result))
  }
```

Methhod returns an HTTP response.

#### 2.4. Endpoint in Routes

***Post request to add a defined user movie :***
```
POST         /add             controllers.MovieController.addNewMovie
```
***Get request to search movies that match requested genre :***
```
GET         /movies/:genre    controllers.MovieController.SearchByGenre(genre: String)
```
***Get request to list all movies :****
```
GET         /movies             controllers.MovieController.list
```
***Get request to group count of movies by year of production :****
```
GET         /compute          controllers.MovieController.countByYear
```

## 3- Spark for storage and aggregations :

I used Spark to do some operations like :

***1- GroupBy : Count of movies by year of production***

```
  def countByYear: Action[AnyContent] = Action { implicit request =>
    // Result in Json Format
    val result = Reader
      .read(DatabasePath, schema)
      .groupBy(col("year"))
      .agg(count(col("title")).as("moviesCount"))
      .as[CountByYear]
      .collect
      .toList
    Ok(Json.toJson(result))
  }
```

***2- Filter : Searching by genre***


```
  def SearchByGenre(genre: String): Action[AnyContent] = Action { implicit request =>
    // Result in Json Format
    Ok(Json.toJson(Reader
      .read(DatabasePath, schema)
      .filter(array_contains(col("genre"), genre))
      .orderBy(col("year"), col("title"))
      .as[MovieWrites]
      .collect()
    ))
  }
```

***3- Read & persist movies permanently locally :***

Reader :

```
object Reader {

  def read(path: String, schema: StructType)(implicit spark: SparkSession): DataFrame = {
    spark
      .read
      .schema(schema)
      .orc(s"$path/*.orc")
  }

}
```

Persister : 

```
object Persister {
  def persist(movie: MovieWrites, databasePath: String)(implicit spark: SparkSession): Try[Unit] = {
    Try(
      spark
      .createDataFrame(Seq(movie))
      .write.mode(SaveMode.Append).format("orc").save(databasePath)
    )
  }
}
```
## 4- User Tests with postman  :
```
Adding new movie :
```
![image](https://user-images.githubusercontent.com/32568108/138506620-17ed6b40-8cd7-46df-aafb-00d28940c24a.png)

```
List all movies :
```
![image](https://user-images.githubusercontent.com/32568108/138506763-1af14fa8-1699-4439-a4b5-ae6f578306ce.png)

```
List movies by matched genre : 
```
![image](https://user-images.githubusercontent.com/32568108/138506851-2d6e1507-1bd3-4d75-a1e7-efe59747e0a9.png)

```
Count movies by year of production :
```
![image](https://user-images.githubusercontent.com/32568108/138506968-006a9ab3-48bd-4465-a762-25a19bf3eeba.png)
