package com.example.runandremember

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import com.example.runandremember.databinding.ActivityMainActivity2RegisterBinding
import com.example.runandremember.openhelper.EMAIL_USUA
import com.example.runandremember.openhelper.ID_USUA
import com.example.runandremember.openhelper.SQLite_OpenHelper
import com.example.runandremember.openhelper.TABLE_USUA
import com.example.runandremember.recyclerview.ItemTraining

const val ACTIVITY_LOG_USUA = "activity_log_usua"

class MainActivity2Register : AppCompatActivity() {

    private lateinit var binding: ActivityMainActivity2RegisterBinding
    private var uDatabase: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity2RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ItemTraining.ACTIVITY_SCREEN_ITEM, Usuario::class.java)
                ?: intent.getParcelableExtra(ItemTraining.ACTIVITY_UPDATE_SPORT, Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ItemTraining.ACTIVITY_SCREEN_ITEM)
                ?: intent.getParcelableExtra(ItemTraining.ACTIVITY_UPDATE_SPORT)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(this@MainActivity2Register)
                    .setMessage("¿Quieres salir? Dale a la flecha de arriba para salir.")
                    .setPositiveButton("De acuerdo", null)
                    .show()
            }
        })

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        uDatabase?.let {
            binding.nam.setText(it.name)
            binding.surna.setText(it.surname)
            binding.pass2.setText(it.password)
            binding.emai.setText(it.email)
            binding.heig.setText(it.height)
            binding.weig.setText(it.weight)
            binding.anio.setText(it.birth)
            binding.btnRegis.text = "Actualiza usuario"
            binding.textActivity2.text = "Actualízate"
        }
    }

    private fun setupClickListeners() {
        binding.btnAtras.setOnClickListener {
            if (uDatabase != null) {
                val intent = Intent(this, ItemTraining::class.java)
                intent.putExtra(ACTIVITY_LOG_USUA, uDatabase)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnRegis.setOnClickListener {
            handleRegisterOrUpdate()
        }
    }

    private fun handleRegisterOrUpdate() {
        val name = binding.nam.text.toString().trim()
        val surname = binding.surna.text.toString().trim()
        val password = binding.pass2.text.toString().trim()
        val email = binding.emai.text.toString().trim()
        val height = binding.heig.text.toString().trim()
        val weight = binding.weig.text.toString().trim()
        val birth = binding.anio.text.toString().trim()

        if (name.isEmpty() || surname.isEmpty() || password.isEmpty() || email.isEmpty() || birth.isEmpty()) {
            Toast.makeText(this, "Rellena los datos faltantes.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emai.error = "Por favor, ingresa un email válido."
            return
        }

        val dbOption = SQLite_OpenHelper(this)

        if (uDatabase == null && readCompEmail(email)) {
            Toast.makeText(this, "Ya existe un usuario con el mismo email.", Toast.LENGTH_LONG).show()
            return
        }

        val usua = Usuario(
            uDatabase?.id ?: 0,
            name, surname, password, email, height, weight, birth
        )

        if (uDatabase != null) {
            val updateUsua = dbOption.updateUsua(usua)
            if (updateUsua > 0) {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Datos actualizados correctamente.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ItemTraining::class.java)
                uDatabase = usua
                intent.putExtra(ACTIVITY_LOG_USUA, uDatabase)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error al actualizar los datos.", Toast.LENGTH_LONG).show()
            }
        } else {
            val placeUsua = dbOption.insertUsua(usua)
            if (placeUsua > 0) {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun readCompEmail(email: String): Boolean {
        val db = SQLite_OpenHelper(this)
        val dbNueva = db.readableDatabase
        val select = "$EMAIL_USUA = ?"
        val selectArgs = arrayOf(email)
        val columns = arrayOf(ID_USUA)
        dbNueva.query(TABLE_USUA, columns, select, selectArgs, null, null, null)?.use { cursor ->
            return cursor.count > 0
        }
        return false
    }
}
