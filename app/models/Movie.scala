package models

case class Movie(
                  title: String,
                  country: String,
                  year: Short,
                  original_title: String,
                  french_release: String,
                  synopsis: String,
                  genre: List[String],
                  ranking: Short
                )
