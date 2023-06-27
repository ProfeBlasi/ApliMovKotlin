package com.example.tpintegrador.DataBase.Repository

import android.annotation.SuppressLint
import android.content.ContentValues
import com.example.tpintegrador.DataBase.DBHelper
import com.example.tpintegrador.DataBase.Entities.Reference
import java.util.*

class ReferenceRepository(private val dbHelper: DBHelper) {

    fun insertReference(reference: Reference): Long {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_REFERENCE_DESCRIPTION, reference.description)
        contentValues.put(DBHelper.COLUMN_REFERENCE_OPENING_DATE, reference.openingDate.time)
        contentValues.put(DBHelper.COLUMN_REFERENCE_CLOSING_DATE, reference.closingDate.time)
        contentValues.put(DBHelper.COLUMN_REFERENCE_CLOSE, reference.close)

        return db.insert(DBHelper.TABLE_NAME_REFERENCE, null, contentValues)
    }

    fun updateReference(reference: Reference): Int {
        val db = dbHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DBHelper.COLUMN_REFERENCE_DESCRIPTION, reference.description)
        contentValues.put(DBHelper.COLUMN_REFERENCE_OPENING_DATE, reference.openingDate.time)
        contentValues.put(DBHelper.COLUMN_REFERENCE_CLOSING_DATE, reference.closingDate.time)
        contentValues.put(DBHelper.COLUMN_REFERENCE_CLOSE, reference.close)

        return db.update(
            DBHelper.TABLE_NAME_REFERENCE, contentValues, "${DBHelper.COLUMN_ID_REFERENCE} = ?",
            arrayOf(reference.id.toString())
        )
    }

    fun deleteReference(referenceId: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DBHelper.TABLE_NAME_REFERENCE, "${DBHelper.COLUMN_ID_REFERENCE} = ?", arrayOf(referenceId.toString()))
    }

    @SuppressLint("Range")
    fun getAllReferences(): List<Reference> {
        val references = mutableListOf<Reference>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM ${DBHelper.TABLE_NAME_REFERENCE}"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_REFERENCE))
                val description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_REFERENCE_DESCRIPTION))
                val openingDateMillis = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_REFERENCE_OPENING_DATE))
                val openingDate = Date(openingDateMillis)
                val closingDateMillis = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_REFERENCE_CLOSING_DATE))
                val closingDate = Date(closingDateMillis)
                val close = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_REFERENCE_CLOSE)) == 1

                val reference = Reference(id, description, openingDate, closingDate, close)
                references.add(reference)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return references
    }
}
