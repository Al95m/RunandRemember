package com.example.runandremember.openhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.runandremember.Hour
import com.example.runandremember.Planning
import com.example.runandremember.Sport
import com.example.runandremember.Usuario

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "R&RDATABASE"

// Declaramos los valores de las columnas
// TABLA USUARIO
const val TABLE_USUA = "UsuaTable"
const val ID_USUA = "_id"
const val NAME_USUA = "name"
const val SURNAME_USUA = "surname"
const val PASSWORD_USUA = "password"
const val EMAIL_USUA = "email"
const val HEIGHT_USUA = "height"
const val WEIGHT_USUA = "weight"
const val BIRTH_USUA = "birthday"

// TABLA SPORT/ENTRENAMIENTO
const val TABLE_SPORT = "SportTable"
const val ID_SPORT = "_id"
const val IMAGE_SPORT = "image"
const val NAME_SPORT = "name"
const val DESC_SPORT = "description"
const val TIME_SPORT = "time"
const val DAY_SPORT = "day"
const val USER_ID_SPORT = "usua_id"

// TABLA DEL TIEMPO O DE LA HORA
const val TABLE_HOUR = "HourTable"
const val ID_HOUR = "id"
const val TIME_HOUR = "hourtime"
const val SPORT_ID_HOUR = "sport_id"

// TABLA PLANNING
const val TABLE_PLAN = "PlanTable"
const val ID_PLAN = "id"
const val DESC_PLAN = "descplan"
const val SPORT_ID_PLAN = "sport_id"

class SQLite_OpenHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $TABLE_USUA ("
                    + "$ID_USUA INTEGER PRIMARY KEY,"
                    + "$NAME_USUA TEXT,"
                    + "$SURNAME_USUA TEXT,"
                    + "$PASSWORD_USUA TEXT,"
                    + "$HEIGHT_USUA TEXT,"
                    + "$WEIGHT_USUA TEXT,"
                    + "$EMAIL_USUA TEXT,"
                    + "$BIRTH_USUA TEXT)"
        )

        db?.execSQL(
            "CREATE TABLE $TABLE_SPORT ("
                    + "$ID_SPORT INTEGER PRIMARY KEY,"
                    + "$IMAGE_SPORT TEXT,"
                    + "$NAME_SPORT TEXT,"
                    + "$DESC_SPORT TEXT,"
                    + "$TIME_SPORT TEXT,"
                    + "$DAY_SPORT TEXT,"
                    + "$USER_ID_SPORT INTEGER,"
                    + "FOREIGN KEY ($USER_ID_SPORT) REFERENCES $TABLE_USUA($ID_USUA))"
        )

        db?.execSQL(
            "CREATE TABLE $TABLE_HOUR ("
                    + "$ID_HOUR INTEGER PRIMARY KEY,"
                    + "$TIME_HOUR TEXT,"
                    + "$SPORT_ID_HOUR INTEGER,"
                    + "FOREIGN KEY ($SPORT_ID_HOUR) REFERENCES $TABLE_SPORT($ID_SPORT))"
        )

        db?.execSQL(
            "CREATE TABLE $TABLE_PLAN ("
                    + "$ID_PLAN INTEGER PRIMARY KEY,"
                    + "$DESC_PLAN TEXT,"
                    + "$SPORT_ID_PLAN INTEGER,"
                    + "FOREIGN KEY ($SPORT_ID_PLAN) REFERENCES $TABLE_SPORT($ID_SPORT))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PLAN")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_HOUR")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SPORT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUA")
        onCreate(db)
    }

    // USUARIO
    fun IdUsua(id: Int): Usuario? {
        val db = this.readableDatabase
        var usua: Usuario? = null
        db.query(
            TABLE_USUA,
            arrayOf(ID_USUA, NAME_USUA, SURNAME_USUA, PASSWORD_USUA, EMAIL_USUA, HEIGHT_USUA, WEIGHT_USUA, BIRTH_USUA),
            "$ID_USUA = ?",
            arrayOf(id.toString()),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                usua = Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SURNAME_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HEIGHT_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(WEIGHT_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BIRTH_USUA))
                )
            }
        }
        return usua
    }

    fun insertUsua(user: Usuario): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(NAME_USUA, user.name)
            put(SURNAME_USUA, user.surname)
            put(PASSWORD_USUA, user.password)
            put(EMAIL_USUA, user.email)
            put(HEIGHT_USUA, user.height)
            put(WEIGHT_USUA, user.weight)
            put(BIRTH_USUA, user.birth)
        }
        return db.insert(TABLE_USUA, null, cv)
    }

    fun ReadEmail(email: String): Usuario? {
        val db = this.readableDatabase
        var usua: Usuario? = null
        db.query(
            TABLE_USUA,
            arrayOf(ID_USUA, NAME_USUA, SURNAME_USUA, PASSWORD_USUA, EMAIL_USUA, HEIGHT_USUA, WEIGHT_USUA, BIRTH_USUA),
            "$EMAIL_USUA = ?",
            arrayOf(email),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                usua = Usuario(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SURNAME_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PASSWORD_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(EMAIL_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(HEIGHT_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(WEIGHT_USUA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BIRTH_USUA))
                )
            }
        }
        return usua
    }

    fun updateUsua(user: Usuario): Int {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(NAME_USUA, user.name)
            put(SURNAME_USUA, user.surname)
            put(PASSWORD_USUA, user.password)
            put(EMAIL_USUA, user.email)
            put(HEIGHT_USUA, user.height)
            put(WEIGHT_USUA, user.weight)
            put(BIRTH_USUA, user.birth)
        }
        return db.update(TABLE_USUA, cv, "$ID_USUA = ?", arrayOf(user.id.toString()))
    }

    // SPORT
    fun IdSport(id: Int): Sport? {
        val db = this.readableDatabase
        var sport: Sport? = null
        db.query(
            TABLE_SPORT,
            arrayOf(ID_SPORT, IMAGE_SPORT, NAME_SPORT, DESC_SPORT, TIME_SPORT, DAY_SPORT, USER_ID_SPORT),
            "$ID_SPORT = ?",
            arrayOf(id.toString()),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                sport = Sport(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ID_SPORT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_SPORT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(NAME_SPORT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DESC_SPORT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TIME_SPORT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DAY_SPORT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID_SPORT))
                )
            }
        }
        return sport
    }

    fun insertSport(sport: Sport): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(IMAGE_SPORT, sport.image)
            put(NAME_SPORT, sport.name)
            put(DESC_SPORT, sport.description)
            put(TIME_SPORT, sport.time)
            put(DAY_SPORT, sport.day)
            put(USER_ID_SPORT, sport.usuaId)
        }
        return db.insert(TABLE_SPORT, null, cv)
    }

    fun ReadSportId(userIdSport: Int): ArrayList<Sport> {
        val sportList = ArrayList<Sport>()
        val db = this.readableDatabase
        db.query(
            TABLE_SPORT,
            arrayOf(ID_SPORT, IMAGE_SPORT, NAME_SPORT, DESC_SPORT, TIME_SPORT, DAY_SPORT, USER_ID_SPORT),
            "$USER_ID_SPORT = ?",
            arrayOf(userIdSport.toString()),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val sport = Sport(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESC_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DAY_SPORT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID_SPORT))
                    )
                    sportList.add(sport)
                } while (cursor.moveToNext())
            }
        }
        return sportList
    }

    fun ReadSportList(): ArrayList<Sport> {
        val sportList = ArrayList<Sport>()
        val query = "SELECT * FROM $TABLE_SPORT"
        val db = this.readableDatabase
        db.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val sport = Sport(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESC_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME_SPORT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DAY_SPORT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID_SPORT))
                    )
                    sportList.add(sport)
                } while (cursor.moveToNext())
            }
        }
        return sportList
    }

    fun updateSport(sport: Sport): Int {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(IMAGE_SPORT, sport.image)
            put(NAME_SPORT, sport.name)
            put(DESC_SPORT, sport.description)
            put(TIME_SPORT, sport.time)
            put(DAY_SPORT, sport.day)
        }
        return db.update(TABLE_SPORT, cv, "$ID_SPORT = ?", arrayOf(sport.id.toString()))
    }

    fun deleteSport(sport: Sport): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_SPORT, "$ID_SPORT = ?", arrayOf(sport.id.toString()))
    }

    // HOUR
    fun insertHour(hour: Hour): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(TIME_HOUR, hour.hourtime)
            put(SPORT_ID_HOUR, hour.sportId)
        }
        return db.insert(TABLE_HOUR, null, cv)
    }

    fun ReadHourId(sportIdHour: Int): ArrayList<Hour> {
        val hourList = ArrayList<Hour>()
        val db = this.readableDatabase
        db.query(
            TABLE_HOUR,
            arrayOf(ID_HOUR, TIME_HOUR, SPORT_ID_HOUR),
            "$SPORT_ID_HOUR = ?",
            arrayOf(sportIdHour.toString()),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val time = Hour(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SPORT_ID_HOUR))
                    )
                    hourList.add(time)
                } while (cursor.moveToNext())
            }
        }
        return hourList
    }

    fun ReadHourList(): ArrayList<Hour> {
        val hourList = ArrayList<Hour>()
        val query = "SELECT * FROM $TABLE_HOUR"
        val db = this.readableDatabase
        db.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val hour = Hour(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_HOUR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TIME_HOUR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SPORT_ID_HOUR))
                    )
                    hourList.add(hour)
                } while (cursor.moveToNext())
            }
        }
        return hourList
    }

    fun updateHour(hour: Hour): Int {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(TIME_HOUR, hour.hourtime)
        }
        return db.update(TABLE_HOUR, cv, "$ID_HOUR = ?", arrayOf(hour.id.toString()))
    }

    fun deleteHour(hour: Hour): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_HOUR, "$ID_HOUR = ?", arrayOf(hour.id.toString()))
    }

    // PLANNING
    fun insertPlan(plan: Planning): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(DESC_PLAN, plan.descplan)
            put(SPORT_ID_PLAN, plan.sportId)
        }
        return db.insert(TABLE_PLAN, null, cv)
    }

    fun ReadPlanId(sportIdPlan: Int): ArrayList<Planning> {
        val planList = ArrayList<Planning>()
        val db = this.readableDatabase
        db.query(
            TABLE_PLAN,
            arrayOf(ID_PLAN, DESC_PLAN, SPORT_ID_PLAN),
            "$SPORT_ID_PLAN = ?",
            arrayOf(sportIdPlan.toString()),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val plan = Planning(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_PLAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESC_PLAN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SPORT_ID_PLAN))
                    )
                    planList.add(plan)
                } while (cursor.moveToNext())
            }
        }
        return planList
    }

    fun ReadPlanList(): ArrayList<Planning> {
        val planList = ArrayList<Planning>()
        val query = "SELECT * FROM $TABLE_PLAN"
        val db = this.readableDatabase
        db.rawQuery(query, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val plan = Planning(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID_PLAN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DESC_PLAN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SPORT_ID_PLAN))
                    )
                    planList.add(plan)
                } while (cursor.moveToNext())
            }
        }
        return planList
    }

    fun updatePlan(plan: Planning): Int {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(DESC_PLAN, plan.descplan)
        }
        return db.update(TABLE_PLAN, cv, "$ID_PLAN = ?", arrayOf(plan.id.toString()))
    }

    fun deletePlan(plan: Planning): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_PLAN, "$ID_PLAN = ?", arrayOf(plan.id.toString()))
    }
}
