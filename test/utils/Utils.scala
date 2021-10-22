package utils

import org.apache.spark.sql.SparkSession

trait Utils {

  implicit val spark = SparkSession
    .builder()
    .enableHiveSupport()
    .config("spark.master", "local")
    .appName("Spark Hive Example")
    .enableHiveSupport()
    .getOrCreate()

}
