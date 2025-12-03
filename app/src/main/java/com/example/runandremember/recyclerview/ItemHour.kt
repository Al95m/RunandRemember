package com.example.runandremember.recyclerview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runandremember.*
import com.example.runandremember.databinding.ItemHourBinding
import com.example.runandremember.openhelper.SQLite_OpenHelper

class ItemHour : AppCompatActivity() {

    private lateinit var binding: ItemHourBinding
    private var eDatabase: Sport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemHourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_SCREEN, Sport::class.java)
                ?: intent.getParcelableExtra(ACTIVITY_HOUR, Sport::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_SCREEN)
                ?: intent.getParcelableExtra(ACTIVITY_HOUR)
        }

        binding.btnAaHour.setOnClickListener {
            val intent = Intent(this, MainActivity5RegisterHour::class.java)
            intent.putExtra(ACTIVITY_DATA_SPORT, eDatabase)
            startActivity(intent)
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val hourList = getHourList()
        if (hourList.isEmpty()) {
            binding.listHour.visibility = View.GONE
            binding.listHourVaci.visibility = View.VISIBLE
        } else {
            binding.listHour.visibility = View.VISIBLE
            binding.listHourVaci.visibility = View.GONE
            binding.listHour.layoutManager = LinearLayoutManager(this)
            val adapter = ItemHourAdapter(hourList, {
                // on click
                val intent = Intent(this, MainActivity5RegisterHour::class.java)
                intent.putExtra(ACTIVITY_DATA_HOUR, it)
                startActivity(intent)
                finish()
            }, {
                // on delete
                showDeleteConfirmationDialog(it)
            })
            binding.listHour.adapter = adapter
        }
    }

    private fun getHourList(): ArrayList<Hour> {
        val dbHelper = SQLite_OpenHelper(this)
        return eDatabase?.id?.let { dbHelper.ReadHourId(it) } ?: arrayListOf()
    }

    private fun showDeleteConfirmationDialog(hour: Hour) {
        AlertDialog.Builder(this)
            .setTitle("¿Quieres eliminar la alarma seleccionada?")
            .setMessage("Se borrará permanentemente")
            .setPositiveButton("Si") { _, _ ->
                deleteHour(hour)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteHour(hour: Hour) {
        val dbHelper = SQLite_OpenHelper(this)
        val success = dbHelper.deleteHour(hour) > 0
        if (success) {
            Toast.makeText(this, "Se eliminó", Toast.LENGTH_LONG).show()
            setupRecyclerView() // Refresh the list
        } else {
            Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ACTIVITY_DATA_SPORT = "activity_data_sport"
        const val ACTIVITY_DATA_HOUR = "activity_data_hour"
    }
}
