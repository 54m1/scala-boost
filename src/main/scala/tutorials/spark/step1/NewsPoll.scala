package com.github.btmorr
package tutorials.spark.step1

// Add deserialization classes, print the articles in a more readable format
object NewsPoll extends App {
  import Schemas.Ops._
  import tutorials.spark.step0.ApiOps._
  import tutorials.spark.step0.SparkInit

  // Before running this app, the NEWSAPI_KEY environment variable must be set
  val newsApiKey = sys.env.getOrElse( "NEWSAPI_KEY", throw new Exception( "NEWSAPI_KEY environment variable must be set before running this application" ) )

  val sc = SparkInit.sparkContext

  val source = "bbc-news"
  val requestString = s"https://newsapi.org/v1/articles?source=$source&sortBy=top&apiKey=$newsApiKey"

  val rdd = sc.parallelize( Seq( requestString ) )
  val contentsRDD = rdd.map( makeNewsApiRequest )
  val articlesRDD = contentsRDD
    .map( deserialize )
    .flatMap( _.articles )
  val articles = articlesRDD.collect

//  articles foreach prettyPrintArticle


  sc.stop()
}
