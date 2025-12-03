package com.example.runandremember

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.runandremember.databinding.ActivityMainActivity4ScreenTrainingBinding
import com.example.runandremember.recyclerview.ItemHour
import com.example.runandremember.recyclerview.ItemPlan
import com.example.runandremember.recyclerview.ItemTraining.Companion.ACTIVITY_DETAIL_SPORT


const val ACTIVITY_SCREEN = "activity_screen"

class MainActivity4ScreenTraining : AppCompatActivity() {

    private lateinit var binding: ActivityMainActivity4ScreenTrainingBinding
    private var eDatabase: Sport? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity4ScreenTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        eDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_DETAIL_SPORT, Sport::class.java)
                ?: intent.getParcelableExtra(ACTIVITY_REG, Sport::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_DETAIL_SPORT)
                ?: intent.getParcelableExtra(ACTIVITY_REG)
        }

        eDatabase?.let {
            binding.nickImageDepor2.setImageURI(it.image?.toUri())
            binding.nickHours2.text = it.time
            binding.nickDesc2.text = it.description
            binding.nickDay2.text = it.day
            binding.nickSport2.text = it.name
        }

        binding.btnTimeConsult.setOnClickListener {
            val intent = Intent(this, ItemHour::class.java)
            eDatabase?.let {
                intent.putExtra(ACTIVITY_SCREEN, it)
            }
            startActivity(intent)
        }

        binding.btnPlanConsult.setOnClickListener {
            val intent = Intent(this, ItemPlan::class.java)
            eDatabase?.let {
                intent.putExtra(ACTIVITY_SCREEN, it)
            }
            startActivity(intent)
        }

        binding.btnUpdateEntrena.setOnClickListener {
            val intent = Intent(this, MainActivity3RegisterTraining::class.java)
            eDatabase?.let {
                intent.putExtra(ACTIVITY_DETAIL_SPORT, it)
            }
            startActivity(intent)
        }
    }
}
