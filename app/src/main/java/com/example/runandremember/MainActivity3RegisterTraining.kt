package com.example.runandremember

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.runandremember.databinding.ActivityMainActivity3RegisterTrainingBinding
import com.example.runandremember.openhelper.SQLite_OpenHelper
import com.example.runandremember.recyclerview.ItemTraining
import com.example.runandremember.recyclerview.ItemTraining.Companion.ACTIVITY_DETAIL_SPORT
import com.example.runandremember.recyclerview.ItemTraining.Companion.ACTIVITY_SCREEN_ITEM
import com.example.runandremember.ACTIVITY_LOG_USUA
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

const val ACTIVITY_REG = "activity_reg"

class MainActivity3RegisterTraining : AppCompatActivity() {

    private lateinit var binding: ActivityMainActivity3RegisterTrainingBinding
    private var uDatabase: Usuario? = null
    private var eDatabase: Sport? = null
    private var reloadImage: Uri? = null
    private val DIREC_IMAGE = "RunAndRememeber"
    private var calend = Calendar.getInstance()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.extras?.getParcelable("data", Bitmap::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.extras?.get("data") as? Bitmap
            }
            if (thumbnail != null) {
                reloadImage = loadImage(thumbnail)
                binding.nickImageDepor.setImageBitmap(thumbnail)
                Toast.makeText(this, "Añadido correctamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            openCamera()
        } else {
            Toast.makeText(
                applicationContext,
                "No tienes permisos para acceder a la cámara.",
                Toast.LENGTH_SHORT
            ).show()
            showSettingsDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainActivity3RegisterTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_SCREEN_ITEM, Usuario::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_SCREEN_ITEM)
        }

        eDatabase = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ACTIVITY_DETAIL_SPORT, Sport::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(ACTIVITY_DETAIL_SPORT)
        }

        setupSpinner()
        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        eDatabase?.let {
            binding.nickDesc.setText(it.description)
            binding.nickHours.setText(it.time)
            binding.nickDay.setText(it.day)
            reloadImage = it.image?.toUri()
            binding.nickImageDepor.setImageURI(reloadImage)
            binding.nickSport.visibility = View.VISIBLE
            binding.texthora.visibility = View.GONE
            binding.btnAtrasreg.visibility = View.VISIBLE
            binding.btnSave.text = "Actualizar"
        }
    }

    private fun setupSpinner() {
        val opciones = resources.getStringArray(R.array.valores_deportes)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        binding.nickSport.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btnAtrasreg.setOnClickListener {
            if (eDatabase != null) {
                val intent = Intent(this, MainActivity4ScreenTraining::class.java)
                intent.putExtra(ACTIVITY_REG, eDatabase)
                startActivity(intent)
            } else {
                val intent = Intent(this, ItemTraining::class.java)
                intent.putExtra(ACTIVITY_LOG_USUA, uDatabase)
                startActivity(intent)
            }
            finish()
        }

        binding.nickImageDepor.setOnClickListener {
            openCamera_click()
            Toast.makeText(this, "Abriendo la cámara...", Toast.LENGTH_SHORT).show()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calend.set(Calendar.YEAR, year)
            calend.set(Calendar.MONTH, month)
            calend.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        binding.nickDay.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener, calend.get(Calendar.YEAR), calend.get(Calendar.MONTH), calend.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSave.setOnClickListener {
            handleSave()
        }
    }

    private fun handleSave() {
        val sportName = binding.nickSport.selectedItem.toString()
        val description = binding.nickDesc.text.toString().trim()
        val hours = binding.nickHours.text.toString().trim()
        val day = binding.nickDay.text.toString().trim()

        if (sportName.isEmpty() || description.isEmpty() || hours.isEmpty() || day.isEmpty()) {
            Toast.makeText(this, "Rellena los datos faltantes.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = uDatabase?.id ?: eDatabase?.usuaId
        if (userId == null) {
            Toast.makeText(this, "Error: No se pudo asociar el entrenamiento a un usuario.", Toast.LENGTH_SHORT).show()
            return
        }

        val sport = Sport(
            eDatabase?.id ?: 0,
            reloadImage.toString(),
            sportName,
            description,
            hours,
            day,
            userId
        )

        val dbConnection = SQLite_OpenHelper(this)

        if (eDatabase != null) {
            val updateSport = dbConnection.updateSport(sport)
            if (updateSport > 0) {
                eDatabase = sport
                uDatabase = dbConnection.IdUsua(userId)
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Entrenamiento actualizado correctamente.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ItemTraining::class.java)
                intent.putExtra(ACTIVITY_REG, uDatabase)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar el entrenamiento.", Toast.LENGTH_SHORT).show()
            }
        } else {
            val placeSport = dbConnection.insertSport(sport)
            if (placeSport > 0) {
                setResult(Activity.RESULT_OK)
                Toast.makeText(this, "Entrenamiento añadido correctamente.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ItemTraining::class.java)
                intent.putExtra(ACTIVITY_REG, uDatabase)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al añadir el entrenamiento.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera_click() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun openCamera() {
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(camaraIntent)
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setMessage("No diste permiso para acceder a tu cámara, puedes cambiarlo en ajustes de usuario del sistema.")
            .setPositiveButton("Ve a los ajustes.") { _, _ ->
                try {
                    val packages = Uri.fromParts("package", packageName, null)
                    val setting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    setting.data = packages
                    startActivity(setting)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateDateInView() {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.nickDay.setText(sdf.format(calend.time).toString())
    }

    private fun loadImage(bitmap: Bitmap): Uri {
        val map = ContextWrapper(applicationContext)
        val filePath = map.getDir(DIREC_IMAGE, Context.MODE_PRIVATE)
        val file = File(filePath, "${UUID.randomUUID()}.jpg")
        try {
            val out: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }
}
