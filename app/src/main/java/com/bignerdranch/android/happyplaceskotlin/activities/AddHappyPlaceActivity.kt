package com.bignerdranch.android.happyplaceskotlin.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.happyplaceskotlin.R
import com.bignerdranch.android.happyplaceskotlin.database.DatabaseHandler
import com.bignerdranch.android.happyplaceskotlin.databinding.ActivityAddHappyPlaceBinding
import com.bignerdranch.android.happyplaceskotlin.models.HappyPlaceModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.DoubleUnaryOperator
import kotlin.contracts.contract


class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var ab: ActivityAddHappyPlaceBinding
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage: Uri? = null
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    private var mHappyPlaceDetail: HappyPlaceModel? = null

    val resultContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult? ->
        if(result?.resultCode == Activity.RESULT_OK) {
            val contentUri = result.data?.data
            try {
                val selectedImageBitmap = MediaStore.Images.Media
                    .getBitmap(this.contentResolver, contentUri)
                saveImageToInternalStorage = saveImageToInternalStorage(selectedImageBitmap)
                Log.d("Saved", "SavedToRedmi: $saveImageToInternalStorage")
                ab.ivPlaceImage.setImageBitmap(selectedImageBitmap)


            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    var resultContractCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult? ->
        if(result?.resultCode == Activity.RESULT_OK) {
            val thumbnail: Bitmap = result.data!!.extras!!.get("data") as Bitmap
            saveImageToInternalStorage = saveImageToInternalStorage(thumbnail)
            Log.d("Saved", "SavedToRedmi: $saveImageToInternalStorage")
            ab.ivPlaceImage.setImageBitmap(thumbnail)
        }
    }
    private val contract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult? ->
        if(result?.resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        ab = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(ab.root)

        setSupportActionBar(ab.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ab.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressed()
        }

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            mHappyPlaceDetail = intent.
            getSerializableExtra(MainActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }

        dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }
        updateDateInView()

        if(mHappyPlaceDetail != null) {
            supportActionBar?.title = "Edit Happy Place"

            ab.etTitle.setText(mHappyPlaceDetail!!.title)
            ab.etDescription.setText(mHappyPlaceDetail!!.description)
            ab.etDate.setText(mHappyPlaceDetail!!.date)
            ab.etLocation.setText(mHappyPlaceDetail!!.location)
            mLatitude = mHappyPlaceDetail!!.latitude
            mLongitude = mHappyPlaceDetail!!.longitude

            saveImageToInternalStorage = Uri.parse(mHappyPlaceDetail!!.image)
            ab.ivPlaceImage.setImageURI(saveImageToInternalStorage)
            ab.btnSave.text = "UPDATE"
        }

        ab.etDate.setOnClickListener(this)
        ab.tvAddImage.setOnClickListener(this)
        ab.btnSave.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_date -> {
                DatePickerDialog(
                    this@AddHappyPlaceActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf(
                    "Select photo from Gallery",
                    "Capture photo from camera"
                )
                pictureDialog.setItems(pictureDialogItems) { _, which ->
                    when (which) {
                        0 -> choosePhotoFromGallery()
                        1 -> choosePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
            R.id.btn_save -> {

                when {
                    ab.etTitle.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                    }
                    ab.etDescription.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show()
                    }
                    ab.etLocation.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter location", Toast.LENGTH_SHORT).show()
                    }
                    saveImageToInternalStorage == null -> {
                        Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                    } else -> {
                        val happyPlaceModel = HappyPlaceModel(
                            0,
                            ab.etTitle.text.toString(),
                            saveImageToInternalStorage.toString(),
                            ab.etDescription.text.toString(),
                            ab.etDate.text.toString(),
                            ab.etLocation.text.toString(),
                            mLatitude,
                            mLongitude
                        )
                    val dbHandler = DatabaseHandler(this)
                    val addHappyPlace = dbHandler.addHappyPlace(happyPlaceModel)

                    if(addHappyPlace > 0) {
                        setResult(Activity.RESULT_OK)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        contract.launch(intent)

                       /* Toast.makeText(this, "The happy place details are inserted successfully",
                            Toast.LENGTH_SHORT).show()*/
                        finish()
                        }
                    }
                }
            }
        }
    }


    private fun choosePhotoFromCamera() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if(report.areAllPermissionsGranted()) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    resultContractCamera.launch(cameraIntent)

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest?>?,
                token: PermissionToken?) {
                showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }




    private fun choosePhotoFromGallery() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
              if(report.areAllPermissionsGranted()) {
                  val galleryIntent = Intent(Intent.ACTION_PICK,
                      MediaStore.Images.Media.EXTERNAL_CONTENT_URI)


//               startActivityForResult(galleryIntent, GALLERY)

                  resultContract.launch(galleryIntent)

                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest?>?,
                token: PermissionToken?) {
                    showRationalDialogForPermissions()
            }
        }).onSameThread().check()
    }


    private fun showRationalDialogForPermissions() {
        AlertDialog
            .Builder(this)
            .setMessage("" + "It looks like you have turned off permission required " +
        "for this feature. " + "It can be enabled under the Application settings")
            .setPositiveButton("Go To Settings")
            {
                _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        ab.etDate.setText(sdf.format(cal.time).toString())
    }



    private fun saveImageToInternalStorage(bitmap: Bitmap) : Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGES_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }



    companion object {

        private const val IMAGES_DIRECTORY = "HappyPLacesImages"
    }
}