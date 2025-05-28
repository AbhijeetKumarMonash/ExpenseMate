package com.example.expensemate

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import java.util.*

object CalendarUtils {
    fun addEventToCalendar(context: Context, title: String, description: String, date: String) {
        val parts = date.split("/")
        if (parts.size != 3) return

        val day = parts[0].toInt()
        val month = parts[1].toInt() - 1 // Calendar months are 0-based
        val year = parts[2].toInt()

        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )

        val selection = "${CalendarContract.Calendars.VISIBLE} = 1"

        val calendarId = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(CalendarContract.Calendars._ID)
                val nameIndex = cursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
                val id = cursor.getLong(idIndex)
                val name = cursor.getString(nameIndex)
                Log.d("CalendarUtils", "Using calendar: ID=$id, Name=$name")
                id
            } else {
                Log.e("CalendarUtils", "No calendars found")
                return
            }
        } ?: return

        val startMillis = Calendar.getInstance().apply {
            set(year, month, day, 9, 0)
        }.timeInMillis

        val endMillis = startMillis + (60 * 60 * 1000) // 1 hour later

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        Log.d("CalendarUtils", "Inserted calendar event at URI: $uri")
    }
}
