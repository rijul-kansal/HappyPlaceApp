package com.example.happyplaceapp.activity.activity.Activity


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.happyplaceapp.activity.activity.Model.HappyPlaceModel
import com.example.happyplaceapp.activity.activity.database.DatabaseHandler
import com.example.happyplaceapp.databinding.ActivityHappyPlaceBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Calendar
import java.util.UUID

class HappyPlaceActivity : AppCompatActivity() {
    var binding:ActivityHappyPlaceBinding?=null

    var flag=0

    var saveImageToInternalStorage:Uri?=null
    var mLattiiTude:Double=0.0
    var mLongiTude:Double=0.0

    var mHappyPlaceActivity:HappyPlaceModel?=null
    companion object{
        private var CAMERA=1
        private var GALLERY=2
        private var ImageDir="HappyPlacesApp";
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityHappyPlaceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        setSupportActionBar(binding?.Toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }

        binding?.Toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.dateEvmain?.setOnClickListener {
            populateDateWithDatepicket()
        }

        binding?.cameraIb?.setOnClickListener {
            askingForPermission(CAMERA)
        }
        binding?.galleryIb?.setOnClickListener {
            askingForPermission(GALLERY)
        }

        binding?.saveBtn?.setOnClickListener {
            saveToDataBase()
        }

        mHappyPlaceActivity=intent.getSerializableExtra("EXTRA") as HappyPlaceModel
        if(mHappyPlaceActivity!=null)
        {
            binding?.titleEvmain?.setText(mHappyPlaceActivity!!.title)
            binding?.descriptionEvmain?.setText(mHappyPlaceActivity!!.description)
            binding?.dateEvmain?.setText(mHappyPlaceActivity!!.date)
            binding?.locationEvmain?.setText(mHappyPlaceActivity!!.location)
            saveImageToInternalStorage=Uri.parse(mHappyPlaceActivity!!.image)
            binding?.imageIv?.setImageURI(saveImageToInternalStorage)
            mLattiiTude=mHappyPlaceActivity!!.latitude
            mLongiTude=mHappyPlaceActivity!!.longitude
        }
    }

    private fun saveToDataBase() {
        var tit=binding?.titleEvmain!!.text.toString()
        var des=binding?.descriptionEvmain!!.text.toString()
        var date=binding?.dateEvmain!!.text.toString()
        var loca=binding?.locationEvmain!!.text.toString()
        if(tit.isNotEmpty() && des.isNotEmpty() && date.isNotEmpty() && loca.isNotEmpty()) {
            var hpm = HappyPlaceModel(
                if(mHappyPlaceActivity==null) 0 else mHappyPlaceActivity!!.id,
                tit,
                saveImageToInternalStorage!!.toString(),
                des,
                date,
                loca,
                mLattiiTude,
                mLongiTude
            )
            var dbhandler = DatabaseHandler(this)
            if(mHappyPlaceActivity==null) {
                var res = dbhandler.addHappyPlace(hpm)
                if (res > 0) {
                    setResult(Activity.RESULT_OK)
                    Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                    finish()
                }
            }else {
                var res = dbhandler.updateRecord(hpm)
                if (res > 0) {
                    setResult(Activity.RESULT_OK)
                    Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK ) {
            if(requestCode== GALLERY) {
                var imageUri = data?.data
                var bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
                saveImageToInternalStorage=saveImageToInternalStorage(bitmap)
                binding?.imageIv?.setImageBitmap(bitmap)
            }
            if(requestCode== CAMERA) {
                var thumbNail=data!!.extras!!.get("data") as Bitmap
                saveImageToInternalStorage=saveImageToInternalStorage(thumbNail)
                binding?.imageIv?.setImageBitmap(thumbNail)
            }
        }
    }
    private fun askingForPermission(code :Int) {
        if(flag==0) {
            flag=1
            requestPermission()
        }
        else {
        if (checkPersmission()) {
           if(code== GALLERY)
           {
               val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
               startActivityForResult(pickImg, GALLERY)
           }
            else if(code== CAMERA)
           {
                val picking=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(picking, CAMERA)
           }
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Please Allow the Happy Place App to Take Images from Camera and from Gallery")
            builder.setPositiveButton("Go To Settings") { dialog, which ->
                var intent= Intent(android.provider.Settings.ACTION_SETTINGS)
                startActivity(intent)
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
            }
        }
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(ImageDir, Context.MODE_PRIVATE)
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
    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED)
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@HappyPlaceActivity, permissions(), 1)

    }
    val storagePermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    val storagePermissions33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA
    )
    fun permissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }
    private fun populateDateWithDatepicket() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            binding?.dateEvmain?.setText("" + dayOfMonth + "." + monthOfYear + "." + year)
        }, year, month, day)
        dpd.show()


    }
}