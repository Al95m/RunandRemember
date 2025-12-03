package com.example.runandremember.recyclerview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runandremember.* 
import com.example.runandremember.databinding.ItemTrainingBinding
import com.example.runandremember.openhelper.SQLite_OpenHelper

class ItemTraining : AppCompatActivity() {

    private lateinit var binding: ItemTrainingBinding
    private var uDatabase: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MainActivity.ACTIVITY_LOGIN, Usuario::class.java)
                ?: intent.getParcelableExtra(ACTIVITY_LOG_USUA, Usuario::class.java)
                ?: intent.getParcelableExtra(ACTIVITY_REG, Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(MainActivity.ACTIVITY_LOGIN)
                ?: intent.getParcelableExtra(ACTIVITY_LOG_USUA)
                ?: intent.getParcelableExtra(ACTIVITY_REG)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })

        setupClickListeners()
        setupRecyclerView()
    }

    private fun setupClickListeners() {
        binding.btnUpdateUsua.setOnClickListener {
            val intent = Intent(this, MainActivity2Register::class.java)
            intent.putExtra(ACTIVITY_SCREEN_ITEM, uDatabase)
            startActivity(intent)
            finish()
        }

        binding.btnSalir.setOnClickListener {
            showExitDialog()
        }

        binding.btnRegisEntrena.setOnClickListener {
            val intent = Intent(this, MainActivity3RegisterTraining::class.java)
            intent.putExtra(ACTIVITY_SCREEN_ITEM, uDatabase)
            startActivity(intent)
            finish()
        }
    }

    private fun setupRecyclerView() {
        val sportList = getSportList()
        if (sportList.isEmpty()) {
            binding.listEntre.visibility = View.GONE
            binding.listEntrenaVaci.visibility = View.VISIBLE
        } else {
            binding.listEntre.visibility = View.VISIBLE
            binding.listEntrenaVaci.visibility = View.GONE
            binding.listEntre.layoutManager = LinearLayoutManager(this)
            val adapter = ItemTrainingAdapter(sportList, {
                // on click
                val intent = Intent(this, MainActivity4ScreenTraining::class.java)
                intent.putExtra(ACTIVITY_DETAIL_SPORT, it)
                startActivity(intent)
            }, {
                // on delete
                showDeleteConfirmationDialog(it)
            })
            binding.listEntre.adapter = adapter
        }
    }

    private fun getSportList(): ArrayList<Sport> {
        val dbHelper = SQLite_OpenHelper(this)
        return uDatabase?.id?.let { dbHelper.ReadSportId(it) } ?: arrayListOf()
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.quieres_salir_de_la_cuenta)
            .setPositiveButton(R.string.si) { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun showDeleteConfirmationDialog(sport: Sport) {
        AlertDialog.Builder(this)
            .setTitle(R.string.quieres_eliminar_el_entrenamiento)
            .setMessage(R.string.se_borrara_permanentemente)
            .setPositiveButton(R.string.si) { _, _ ->
                deleteSport(sport)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun deleteSport(sport: Sport) {
        val dbHelper = SQLite_OpenHelper(this)
        val success = dbHelper.deleteSport(sport) > 0
        if (success) {
            Toast.makeText(this, R.string.se_elimino, Toast.LENGTH_LONG).show()
            setupRecyclerView() // Refresh the list
        } else {
            Toast.makeText(this, R.string.no_se_pudo_eliminar, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ACTIVITY_SCREEN_ITEM = "activity_screen_item"
        const val ACTIVITY_DETAIL_SPORT = "activity_detail_sport"
        const val ACTIVITY_UPDATE_SPORT = "activity_update_sport"
    }
}
