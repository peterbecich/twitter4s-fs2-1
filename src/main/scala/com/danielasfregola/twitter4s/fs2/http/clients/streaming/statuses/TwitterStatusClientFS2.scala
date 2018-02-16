package com.danielasfregola.twitter4s.fs2
package http.clients.streaming.statuses

import com.danielasfregola.twitter4s.entities.streaming.StreamingMessage
import com.danielasfregola.twitter4s.entities.enums.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.FilterLevel.FilterLevel
import com.danielasfregola.twitter4s.entities.enums.Language.Language
import com.danielasfregola.twitter4s.entities.streaming.CommonStreamingMessage
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.parameters._
import com.danielasfregola.twitter4s.http.clients.streaming.{StreamingClient, TwitterStream}
import com.danielasfregola.twitter4s.fs2.http.clients.streaming.FS2._
import com.danielasfregola.twitter4s.util.Configurations._
import com.danielasfregola.twitter4s.http.clients.streaming.StreamingClient
import com.danielasfregola.twitter4s.http.clients.streaming.statuses.TwitterStatusClient


import cats.effect.IO
import scala.concurrent.Future

object FS2 {
  implicit class TwitterStatusClientFS2(twitterStatusClient: TwitterStatusClient)
      extends TwitterStatusClient {

    val streamingClient = StreamingClientFS2(twitterStatusClient.streamingClient)

    def sampleStatusesFS2: Unit = println("sample statuses fs2")

    // TODO give TwitterStatusClient.statusUrl more flexible access in twitter4s
    private val statusUrl = s"$statusStreamingTwitterUrl/$twitterVersion/statuses"


    /** Feeds `StreamingMessage`s into a Functional Streams for Scala (FS2) `Sink`.
      * @param languages : Empty by default. List of 'BCP 47' language identifiers.
      *                    For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters" target="_blank">
      *                      https://developer.twitter.com/en/docs/tweets/filter-realtime/guides/basic-stream-parameters</a>
      * @param stall_warnings : Default to false. Specifies whether stall warnings (`WarningMessage`) should be delivered as part of the updates.
      * @param tracks : Empty by default. List of phrases which will be used to determine what Tweets will be delivered on the stream.
      *                 Each phrase must be between 1 and 60 bytes, inclusive.
      *                 For more information <a href="https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html" target="_blank">
      *                  https://developer.twitter.com/en/docs/tweets/filter-realtime/api-reference/post-statuses-filter.html</a>
      * @param filter_level : Default value is none, which includes all available Tweets.
      *                       Set the minimum value of the filter_level Tweet attribute required to be included in the stream.
      * @param sink : Provide a `Sink[IO, StreamingMessage]` that `StreamingMessage`s will be fed into.  <a href="https://github.com/peterbecich/BannoDemo/blob/ebf0598b9d8eb73fb4796850ad3b91d7d9bf4b20/src/main/scala/me/peterbecich/bannodemo/twitter/TwitterSource.scala#L63-L65"> Usage example </a>
      * 
      */
    def sampleStatusesStream(languages: Seq[Language] = Seq.empty,
      stall_warnings: Boolean = false,
      tracks: Seq[String] = Seq.empty,
      filter_level: FilterLevel = FilterLevel.None)
      (sink: fs2.Sink[IO, StreamingMessage]): Future[TwitterStream] = {
      // import twitterStatusClient.streamingClient._
      import streamingClient._
      val parameters = StatusSampleParameters(languages, stall_warnings, tracks, filter_level)
      preProcessing()
      // see StreamingClient
      streamingClient.RichStreamingHttpRequestFS2(Get(s"${statusUrl}/sample.json", parameters)).processStreamFS2(sink)
      // Get(s"${statusUrl}/sample.json", parameters).processStreamFS2(sink)

      // def printTweetText: PartialFunction[StreamingMessage, Unit] = {
      //   case tweet: com.danielasfregola.twitter4s.entities.Tweet => println(tweet.text)
      // }
      // Get(s"${statusUrl}/sample.json", parameters).processStream(printTweetText)

    }
    


  }



}
