package com.example.happyplaceapp.activity.activity.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaceapp.activity.activity.Model.HappyPlaceModel
import com.example.happyplaceapp.databinding.ActivityHappyPlacesDetailBinding

class HappyPlacesDetailActivity : AppCompatActivity() {
    var binding:ActivityHappyPlacesDetailBinding?=null

    var item:HappyPlaceModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHappyPlacesDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.Toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
        }
        binding?.Toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        item=intent.getSerializableExtra("Extra") as HappyPlaceModel

        binding?.imgshow?.setImageURI(Uri.parse(item!!.image))
        binding?.desshow?.text=item!!.description
        binding?.locationshow?.text=item!!.location
    }
}