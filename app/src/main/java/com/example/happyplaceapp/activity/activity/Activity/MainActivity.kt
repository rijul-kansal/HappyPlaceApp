package com.example.happyplaceapp.activity.activity.Activity

import com.example.happyplaceapp.databinding.ActivityMainBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyplaceapp.activity.activity.Adapter.HappyPlaceAdapter
import com.example.happyplaceapp.activity.activity.database.DatabaseHandler

class MainActivity : AppCompatActivity() {
    var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.FloatingToolbar?.setOnClickListener {
            var intent=Intent(this, HappyPlaceActivity::class.java)
            startActivityForResult(intent,1)
        }
        getDataFromDataBase()
    }
    private  fun getDataFromDataBase() {
        var dbHandler =DatabaseHandler(this)
        var arrlis=dbHandler.getHappyPlacesList()
        var adapter=HappyPlaceAdapter(arrlis)
        binding?.recycleView?.layoutManager= LinearLayoutManager(this)
        binding?.recycleView?.adapter=adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK ) {
            if (requestCode == 1) {
               getDataFromDataBase()
            }
        }
    }
}