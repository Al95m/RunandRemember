package com.example.runandremember

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.runandremember.databinding.ActivityMainActivity6RegisterPlanBinding
import com.example.runandremember.openhelper.SQLite_OpenHelper
import com.example.runandremember.recyclerview.ItemPlan
import com.example.runandremember.recyclerview.ItemPlan.Companion.ACTIVITY_DATA_PLAN
import com.example.runandremember.recyclerview.ItemPlan.Companion.ACTIVITY_DATA_SPORT

const val ACTIVITY_PLAN = "activity_plan"

class MainActivity6RegisterPlan : AppCompatActivity() {

    private lateinit var binding: ActivityMainActivity6RegisterPlanBinding
    private var eDatabase: Sport? = null
    private var pDatabase: Planning? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity6RegisterPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_DATA_SPORT, Sport::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_DATA_SPORT)
        }

        pDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_DATA_PLAN, Planning::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_DATA_PLAN)
        }

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        pDatabase?.let {
            binding.nickPlan.setText(it.descplan)
            binding.btnPlanNew.text = getString(R.string.guardar_actualizacion)
            binding.textActivity6.text = getString(R.string.actualiza_tu_planning)
            binding.btnAtrasRegistroPlan.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnAtrasRegistroPlan.setOnClickListener {
            navigateToItemPlan(eDatabase)
        }

        binding.btnPlanNew.setOnClickListener {
            handleSavePlan()
        }
    }

    private fun handleSavePlan() {
        val planDescription = binding.nickPlan.text.toString().trim()
        if (planDescription.isEmpty()) {
            Toast.makeText(this, "La descripción no puede estar vacía.", Toast.LENGTH_SHORT).show()
            return
        }

        val sportId = eDatabase?.id ?: pDatabase?.sportId
        if (sportId == null) {
            Toast.makeText(this, "Error: No se pudo asociar el plan a un deporte.", Toast.LENGTH_SHORT).show()
            return
        }

        val plan = Planning(
            pDatabase?.id ?: 0,
            planDescription,
            sportId
        )

        val dbConnection = SQLite_OpenHelper(this)

        if (pDatabase != null) {
            val updatePlan = dbConnection.updatePlan(plan)
            if (updatePlan > 0) {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Actualizado correctamente.", Toast.LENGTH_SHORT).show()
                navigateToItemPlan(dbConnection.IdSport(sportId))
            } else {
                Toast.makeText(this, "Error al actualizar.", Toast.LENGTH_SHORT).show()
            }
        } else {
            val placePlan = dbConnection.insertPlan(plan)
            if (placePlan > 0) {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Añadido correctamente", Toast.LENGTH_SHORT).show()
                navigateToItemPlan(eDatabase)
            } else {
                Toast.makeText(this, "Error al añadir.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToItemPlan(sport: Sport?) {
        val intent = Intent(this, ItemPlan::class.java)
        intent.putExtra(ACTIVITY_PLAN, sport)
        startActivity(intent)
        finish()
    }
}
