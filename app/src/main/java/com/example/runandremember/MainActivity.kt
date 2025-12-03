package com.example.runandremember

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.runandremember.databinding.ActivityMainBinding
import com.example.runandremember.openhelper.EMAIL_USUA
import com.example.runandremember.openhelper.ID_USUA
import com.example.runandremember.openhelper.PASSWORD_USUA
import com.example.runandremember.openhelper.SQLite_OpenHelper
import com.example.runandremember.openhelper.TABLE_USUA
import com.example.runandremember.recyclerview.ItemTraining

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaplayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaplayer = MediaPlayer.create(this, R.raw.wind)
        mediaplayer.start()
        mediaplayer.isLooping = true

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("¿Quieres salir de la aplicación? Se cerrará completamente.")
                    .setPositiveButton("Si") { _, _ ->
                        mediaplayer.stop()
                        finishAffinity()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        })

        binding.music.setOnClickListener {
            if (!mediaplayer.isPlaying) {
                mediaplayer.start()
                binding.music.setImageResource(R.drawable.pause)
            } else {
                mediaplayer.pause()
                binding.music.setImageResource(android.R.drawable.ic_media_play)
            }
        }

        binding.regist.setOnClickListener {
            val intent = Intent(this, MainActivity2Register::class.java)
            mediaplayer.stop()
            startActivity(intent)
            Toast.makeText(applicationContext, "Estás en el formulario de registro.", Toast.LENGTH_SHORT).show()
        }

        binding.inic.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.pass.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena los campos faltantes.", Toast.LENGTH_SHORT).show()
            } else {
                val dbRegister = SQLite_OpenHelper(this)
                if (access(email, pass)) {
                    val usua = dbRegister.ReadEmail(email)
                    val intent = Intent(this, ItemTraining::class.java)
                    mediaplayer.stop()
                    intent.putExtra(ACTIVITY_LOGIN, usua)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrecta.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaplayer.isInitialized) {
            mediaplayer.release()
        }
    }

    private fun access(email: String, password: String): Boolean {
        val db = SQLite_OpenHelper(this)
        val dbNueva = db.readableDatabase
        val select = "$EMAIL_USUA = ? AND $PASSWORD_USUA = ?"
        val selectArgs = arrayOf(email, password)
        val columns = arrayOf(ID_USUA)
        val cursor = dbNueva.query(TABLE_USUA, columns, select, selectArgs, null, null, null)
        val resultCount = cursor.count
        cursor.close()
        return resultCount > 0
    }

    companion object {
        const val ACTIVITY_LOGIN = "activity_login"
    }
}
