package persister

import models.MovieWrites
import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.util.Try

object Persister {
  def persist(movie: MovieWrites, databasePath: String)(implicit spark: SparkSession): Try[Unit] = {
    Try(
      spark
      .createDataFrame(Seq(movie))
      .write.mode(SaveMode.Append).format("orc").save(databasePath)
    )
  }
}

