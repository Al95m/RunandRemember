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
import com.example.runandremember.databinding.ItemPlanBinding
import com.example.runandremember.openhelper.SQLite_OpenHelper

class ItemPlan : AppCompatActivity() {

    private lateinit var binding: ItemPlanBinding
    private var eDatabase: Sport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_SCREEN, Sport::class.java)
                ?: intent.getParcelableExtra(ACTIVITY_PLAN, Sport::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_SCREEN)
                ?: intent.getParcelableExtra(ACTIVITY_PLAN)
        }

        binding.btnAaPlan.setOnClickListener {
            val intent = Intent(this, MainActivity6RegisterPlan::class.java)
            intent.putExtra(ACTIVITY_DATA_SPORT, eDatabase)
            startActivity(intent)
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val planList = getPlanList()
        if (planList.isEmpty()) {
            binding.listPlan.visibility = View.GONE
            binding.listPlanVaci.visibility = View.VISIBLE
        } else {
            binding.listPlan.visibility = View.VISIBLE
            binding.listPlanVaci.visibility = View.GONE
            binding.listPlan.layoutManager = LinearLayoutManager(this)
            val adapter = ItemPlanAdapter(planList, {
                // on click
                val intent = Intent(this, MainActivity6RegisterPlan::class.java)
                intent.putExtra(ACTIVITY_DATA_PLAN, it)
                startActivity(intent)
                finish()
            }, {
                // on delete
                showDeleteConfirmationDialog(it)
            })
            binding.listPlan.adapter = adapter
        }
    }

    private fun getPlanList(): ArrayList<Planning> {
        val dbHelper = SQLite_OpenHelper(this)
        return eDatabase?.id?.let { dbHelper.ReadPlanId(it) } ?: arrayListOf()
    }

    private fun showDeleteConfirmationDialog(planning: Planning) {
        AlertDialog.Builder(this)
            .setTitle("¿Quieres eliminar el plan seleccionado?")
            .setMessage("Se borrará permanentemente")
            .setPositiveButton("Si") { _, _ ->
                deletePlan(planning)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deletePlan(planning: Planning) {
        val dbHelper = SQLite_OpenHelper(this)
        val success = dbHelper.deletePlan(planning) > 0
        if (success) {
            Toast.makeText(this, "Se eliminó", Toast.LENGTH_LONG).show()
            setupRecyclerView() // Refresh the list
        } else {
            Toast.makeText(this, "No se pudo eliminar", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val ACTIVITY_DATA_SPORT = "activity_data_sport"
        const val ACTIVITY_DATA_PLAN = "activity_data_plan"
    }
}
