package reader

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, SparkSession}

object Reader {

  def read(path: String, schema: StructType)(implicit spark: SparkSession): DataFrame = {
    spark
      .read
      .schema(schema)
      .orc(s"$path/*.orc")
  }

}
