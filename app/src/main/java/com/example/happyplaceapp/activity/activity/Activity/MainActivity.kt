package com.example.happyplaceapp.activity.activity.Activity

import com.example.happyplaceapp.databinding.ActivityMainBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaceapp.activity.activity.Adapter.HappyPlaceAdapter
import com.example.happyplaceapp.activity.activity.Model.HappyPlaceModel
import com.example.happyplaceapp.activity.activity.Utils.SwipeToDeleteCallback
import com.example.happyplaceapp.activity.activity.Utils.SwipeToEditCallback
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
       if(arrlis.size>0)
       {
           binding?.recycleView?.visibility= View.VISIBLE
           binding?.general?.visibility=View.INVISIBLE
            adapter.setOnClickListener(object :
                HappyPlaceAdapter.OnClickListener {
                override fun onClick(position: Int, model: HappyPlaceModel) {
                    var intent= Intent(this@MainActivity,HappyPlacesDetailActivity::class.java)
                    intent.putExtra("Extra",model)
                    startActivity(intent)
                }
            })
            val swipeHandler = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding?.recycleView?.adapter as HappyPlaceAdapter
                    adapter.removeAt(this@MainActivity,viewHolder.adapterPosition)
                    getDataFromDataBase()
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(binding?.recycleView!!)
            val swipeHandlerD = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding?.recycleView?.adapter as HappyPlaceAdapter
                    adapter.EditAt(this@MainActivity,viewHolder.adapterPosition, 1)
                }
            }
            val itemTouchHelperD = ItemTouchHelper(swipeHandlerD)
            itemTouchHelperD.attachToRecyclerView(binding?.recycleView!!)
       }
        else
       {
           binding?.recycleView?.visibility= View.INVISIBLE
           binding?.general?.visibility=View.VISIBLE
       }
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