package com.example.extensions

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun <T : Table> T.upsert(
  where: (SqlExpressionBuilder.() -> Op<Boolean>)? = null,
  vararg keys: Column<*> = (primaryKey ?: throw IllegalArgumentException("primary key is missing")).columns,
  body: T.(InsertStatement<Number>) -> Unit
) = InsertOrUpdate<Number>(this, keys = keys, where = where?.let { SqlExpressionBuilder.it() }).apply {
  body(this)
  execute(TransactionManager.current())
}

class InsertOrUpdate<Key : Any>(
  table: Table,
  isIgnore: Boolean = false,
  private val where: Op<Boolean>? = null,
  private vararg val keys: Column<*>
) : InsertStatement<Key>(table, isIgnore) {
  override fun prepareSQL(transaction: Transaction): String {
    val onConflict = buildOnConflict(table, transaction, where, keys = keys)
    return "${super.prepareSQL(transaction)} $onConflict"
  }
}

fun buildOnConflict(
  table: Table,
  transaction: Transaction,
  where: Op<Boolean>? = null,
  vararg keys: Column<*>
): String {
  var updateSetter = (table.columns - keys).joinToString {
    transaction.identity(it)
  }
  where?.let {
    updateSetter += " WHERE $it"
  }
  return "ON CONFLICT (${keys.joinToString { transaction.identity(it) }}) DO UPDATE SET $updateSetter"
}