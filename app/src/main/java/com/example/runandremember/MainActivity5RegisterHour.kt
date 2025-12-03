package com.example.runandremember

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.runandremember.alarmclock.AlarmReceiver
import com.example.runandremember.databinding.ActivityMainActivity5RegisterHourBinding
import com.example.runandremember.openhelper.SQLite_OpenHelper
import com.example.runandremember.recyclerview.ItemHour
import com.example.runandremember.recyclerview.ItemHour.Companion.ACTIVITY_DATA_HOUR
import com.example.runandremember.recyclerview.ItemHour.Companion.ACTIVITY_DATA_SPORT
import java.text.SimpleDateFormat
import java.util.*

const val ACTIVITY_HOUR = "activity_hour"

class MainActivity5RegisterHour : AppCompatActivity() {

    private lateinit var binding: ActivityMainActivity5RegisterHourBinding
    private var eDatabase: Sport? = null
    private var hDatabase: Hour? = null
    private var calend = Calendar.getInstance()
    private var alarms: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity5RegisterHourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_DATA_SPORT, Sport::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_DATA_SPORT)
        }

        hDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_DATA_HOUR, Hour::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_DATA_HOUR)
        }

        alarms = getSystemService(ALARM_SERVICE) as? AlarmManager

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        updateTimeInView()
        hDatabase?.let {
            binding.nickTime2.setText(it.hourtime)
            binding.btnTimeNew.text = getString(R.string.actualizar_horario)
            binding.textActivity5.text = getString(R.string.actualiza_tu_horario)
            binding.btnAtrasRegistroHour.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnAtrasRegistroHour.setOnClickListener {
            navigateToItemHour(eDatabase)
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calend.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calend.set(Calendar.MINUTE, minute)
            updateTimeInView()
        }

        binding.nickTime2.setOnClickListener {
            TimePickerDialog(
                this, timeSetListener, calend.get(Calendar.HOUR_OF_DAY), calend.get(Calendar.MINUTE), true
            ).show()
        }

        binding.btnTimeNew.setOnClickListener {
            handleSaveHour()
        }
    }

    private fun handleSaveHour() {
        val hourTime = binding.nickTime2.text.toString()

        val sportId = eDatabase?.id ?: hDatabase?.sportId
        if (sportId == null) {
            Toast.makeText(this, "Error: No se pudo asociar la hora a un deporte.", Toast.LENGTH_SHORT).show()
            return
        }

        val hour = Hour(
            hDatabase?.id ?: 0,
            hourTime,
            sportId
        )

        val dbConnection = SQLite_OpenHelper(this)

        if (hDatabase != null) {
            val updateHour = dbConnection.updateHour(hour)
            if (updateHour > 0) {
                val sportForAlarm = dbConnection.IdSport(sportId)
                if (sportForAlarm != null) {
                    hDatabase?.id?.let { setAlarm(it, sportForAlarm) }
                    setResult(Activity.RESULT_OK)
                    Toast.makeText(this, "Actualizado correctamente.", Toast.LENGTH_SHORT).show()
                    navigateToItemHour(sportForAlarm)
                } else {
                    Toast.makeText(this, "Error: Deporte no encontrado para la alarma.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error al actualizar.", Toast.LENGTH_SHORT).show()
            }
        } else {
            val sportForAlarm = eDatabase
            if (sportForAlarm == null) {
                Toast.makeText(this, "Error: No se encontró el deporte para la nueva hora.", Toast.LENGTH_SHORT).show()
                return
            }

            val placeHour = dbConnection.insertHour(hour)
            if (placeHour > 0) {
                val newHourId = dbConnection.ReadHourList().lastOrNull()?.id
                if (newHourId != null) {
                    setAlarm(newHourId, sportForAlarm)
                    setResult(Activity.RESULT_OK)
                    Toast.makeText(this, "Añadido correctamente.", Toast.LENGTH_SHORT).show()
                    navigateToItemHour(sportForAlarm)
                } else {
                    Toast.makeText(this, "Error al obtener la nueva hora.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error al añadir.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAlarm(hourId: Int, sport: Sport) {
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("ticker", sport.name)
            putExtra("title", sport.name)
            putExtra("text", sport.description)
            putExtra("date", sport.day)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, hourId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarms?.setRepeating(AlarmManager.RTC_WAKEUP, calend.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun navigateToItemHour(sport: Sport?) {
        val intent = Intent(this, ItemHour::class.java)
        intent.putExtra(ACTIVITY_HOUR, sport)
        startActivity(intent)
        finish()
    }

    private fun updateTimeInView() {
        val format = "HH:mm"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        binding.nickTime2.setText(sdf.format(calend.time))
    }
}
