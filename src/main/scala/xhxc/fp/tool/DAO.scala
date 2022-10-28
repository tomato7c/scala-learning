package xhxc.fp.tool

import java.lang.reflect.Field
import scala.collection.mutable

object DAO {
  import BetterSyntax._
  def main(args: Array[String]): Unit = {
    val conditions = List("=", "", "")
    val shardings = List("", "")
    val updates = List("", "")
//    select(conditions, shardings)
//    insert(classOf[xxx].getDeclaredFields)
    nonNullUpdateSQL(updates, conditions, shardings)
  }

  private val tab = " " * 4
  private val enter = "\n"

  def insert(fields: Array[Field]): Unit = {
    val arr = fields.filter(it => it.getName != "resolved").map(_.getName)
    println(s"${arr.length} fields")
    val sql = s"INSERT INTO %s(${arr.map(it => it.toSnake()).mkString(",")}) VALUES(${arr.map(":" + _).mkString(",")})"
    println(sql)
  }

  def select(conditions: List[String], shardings: List[String]): Unit = {
    val res = new mutable.StringBuilder
    val sql = s"SELECT * FROM %s WHERE ${conditions.map(toEqualClause).mkString(" AND ")}"
    res.append(tab * 2).append(s"""String sql = format("$sql", getTableName(${shardings.mkString(", ")}))""").append(enter)
    res.append(enter)

    res.append(tab * 2).append("MapSqlParameterSource params = new MapSqlParameterSource()").append(enter)
    res.append(conditions.map(s => s"""${tab * 4}.addValue("$s", $s)""").mkString("", enter, ";" + enter))
    res.append(enter)

    res.append(tab * 2).append("try {").append(enter)
    res.append(tab * 3).append(s"""return shardMasterWriter(${shardings.mkString(", ")}).queryForObject(sql, params, rowMapper)""").append(enter)
    res.append(tab * 2).append("} catch (EmptyResultDataAccessException e) {").append(enter)
    res.append(tab * 3).append("return null;").append(enter)
    res.append(tab * 2).append("}")

    println(res.toString())
  }

  def nonNullUpdateSQL(updates: List[String], conditions: List[String], shardings: List[String]): Unit = {
    val res = new mutable.StringBuilder
    val setClause = updates.map(it => s"${it.toSnake()}=:$it").mkString(",")
    val whereClause = conditions.map(toEqualClause).mkString(" AND ")
    val shardClause = shardings.mkString(", ")
    res.append(tab * 2).append(s"""String sql = format("UPDATE %s SET $setClause WHERE $whereClause", getTableName(TABLE_NAME, $shardClause));""").append(enter)
    // add values
    res.append(tab * 2).append("MapSqlParameterSource params = new MapSqlParameterSource()").append(enter)
    updates.foreach { s =>
      res.append(tab * 4).append(s""".addValue("$s", $s)""").append(enter)
    }
    conditions.foreach { s =>
      res.append(tab * 4).append(s""".addValue("$s", $s)""").append(enter)
    }
    res.append(enter)
    res.append(tab * 2).append(s"""checkState(shardMasterWriter($shardClause).update(sql, params) == 1);""")
    print(res.mkString)
  }

  private def toEqualClause(s: String): String = s"${s.toSnake()}=:$s"
}
