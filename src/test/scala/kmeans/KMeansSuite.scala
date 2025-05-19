package kmeans

import java.util.concurrent.*
import scala.collection.{Map, Seq, mutable}
import scala.collection.parallel.{ParMap, ParSeq}
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.immutable.ParVector
import scala.collection.parallel.mutable.ParArray
import scala.math.*

class KMeansSuite extends munit.FunSuite:
  object KM extends KMeans
  import KM.*

  def checkParClassify(points: ParSeq[Point], means: ParSeq[Point], expected: ParMap[Point, ParSeq[Point]]): Unit =
    assertEquals(classify(points, means), expected, s"classify($points, $means) should equal to $expected")

  test("'classify' should work for empty 'points' and empty 'means'") {
    val points: ParSeq[Point] = IndexedSeq().par
    val means: ParSeq[Point] = IndexedSeq().par
    val expected = ParMap[Point, ParSeq[Point]]()
    checkParClassify(points, means, expected)
  }

  test("'classify' should work for empty 'points'") {
    val points: ParSeq[Point] = ParSeq.empty
    val means: ParSeq[Point] = ParVector(Point(1.0, 1.0, 1.0))
    val expected: ParMap[Point, ParArray[Point]] = ParMap(Point(1.0, 1.0, 1.0) -> ParArray.empty[Point])
    assertEquals(classify(points, means).head, expected.head, s"Point${means.head}->${points} should equal to Point${expected.head._1}->${expected.head._2}")
  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds


