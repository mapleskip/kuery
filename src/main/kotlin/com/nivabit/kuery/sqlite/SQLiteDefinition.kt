package com.nivabit.kuery.sqlite

import com.nivabit.kuery.*
import com.nivabit.kuery.ddl.*

class SQLiteDefinition(column: Table.Column, val meta: SQLiteDefinition.Meta) : Definition.Column(column, meta.type) {

    fun primaryKey(autoIncrement: Boolean = false): SQLiteDefinition {
        if (autoIncrement && meta.type != "INTEGER")
            throw UnsupportedOperationException("Autoincrement is only supported on INTEGER columns")

        return SQLiteDefinition(column, meta.copy(primaryKeyConstraint = PrimaryKeyConstraint(autoIncrement)))
    }

    fun foreignKey(references: Table.Column): SQLiteDefinition {
        return SQLiteDefinition(column, meta.copy(foreignKeyConstraint = ForeignKeyConstraint(references)))
    }

    fun unique(): SQLiteDefinition {
        return SQLiteDefinition(column, meta.copy(uniqueConstraint = UniqueConstraint()))
    }

    fun notNull(): SQLiteDefinition {
        return SQLiteDefinition(column, meta.copy(notNullConstraint = NotNullConstraint()))
    }

    data class Meta(
            val type: String,
            val primaryKeyConstraint: PrimaryKeyConstraint? = null,
            val foreignKeyConstraint: ForeignKeyConstraint? = null,
            val uniqueConstraint: UniqueConstraint? = null,
            val notNullConstraint: NotNullConstraint? = null
    )

    class PrimaryKeyConstraint(val autoIncrement: Boolean = false)

    class ForeignKeyConstraint(val references: Table.Column)

    class UniqueConstraint()

    class NotNullConstraint()
}

fun Table.Column.integer(): SQLiteDefinition {
    return SQLiteDefinition(this, SQLiteDefinition.Meta(type = "INTEGER"))
}

fun Table.Column.real(): SQLiteDefinition {
    return SQLiteDefinition(this, SQLiteDefinition.Meta(type = "REAL"))
}

fun Table.Column.text(): SQLiteDefinition {
    return SQLiteDefinition(this, SQLiteDefinition.Meta(type = "TEXT"))
}

fun Table.Column.blob(): SQLiteDefinition {
    return SQLiteDefinition(this, SQLiteDefinition.Meta(type = "BLOB"))
}