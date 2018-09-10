package com.burak.iot.view

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.burak.iot.R
import com.burak.iot.databinding.ActivityMainBinding
import com.burak.iot.databinding.DetailDialogBinding
import com.burak.iot.model.notification.Notification
import com.burak.iot.utils.ShareImageUtil
import com.burak.iot.utils.SwipeToDeleteCallback
import com.burak.iot.utils.SwipeToShareCallback
import com.burak.iot.viewmodel.DeviceViewModel
import com.burak.iot.viewmodel.NotificationViewModel
import com.google.firebase.FirebaseApp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val shareImage by lazy { ShareImageUtil() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dvm = ViewModelProviders.of(this).get(DeviceViewModel::class.java)
        val nvm = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        binding.devicesVm = dvm
        binding.notificationVm = nvm
        recyclerView.setHasFixedSize(true)
        val adapter = NotificationAdapter { item -> showDetailDialog(item) }
        recyclerView.adapter = adapter
        val swipeHandlerDelete = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                nvm.notificationListener?.let {
                    nvm.notificationProcessor.deleteNotification(it, adapter.notificationSummaries[viewHolder.adapterPosition].sentDate)
                }
                adapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
        val swipeHandlerShare = object : SwipeToShareCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                shareImage.shareImage(adapter.notificationSummaries[viewHolder.adapterPosition].imageUrl, this@MainActivity)
                adapter.notifyItemChanged(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelperDelete = ItemTouchHelper(swipeHandlerDelete)
        itemTouchHelperDelete.attachToRecyclerView(recyclerView)
        val itemTouchHelperShare = ItemTouchHelper(swipeHandlerShare)
        itemTouchHelperShare.attachToRecyclerView(recyclerView)
        dvm.loadDevicesSummaries().observe(this, Observer {
            nvm.notificationProcessor.getNotificationList(nvm)
            if (dvm.deviceProcessor.repository.liveData.value?.isNotEmpty()!!) {
                textViewAdd.setText(R.string.no_notification)
            }
            invalidateOptionsMenu()
        })
        nvm.loadNotificationSummaries().observe(this, Observer {
            if (it!!.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                textViewAdd.visibility = View.GONE
                adapter.updateList(it)
            } else {
                textViewAdd.visibility = View.VISIBLE
                adapter.updateList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (binding.devicesVm?.db?.getDevices()?.isNotEmpty()!!)
            menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_devices -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.add(R.id.coordinatorLayout, LoadDevicesDetailFragment())
                transaction.addToBackStack("loadDevicesDetailFragment")
                transaction.commitAllowingStateLoss()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    class DataBindingComponent {
        companion object {
            @JvmStatic
            @BindingAdapter("load_image")
            fun ImageView.setImageUrl(url: String) {
                Picasso.get().load(url).into(this)
            }
        }
    }

    private fun showDetailDialog(notification: Notification) {
        val dialog = Dialog(this)
        val detailDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.detail_dialog, null, false) as DetailDialogBinding
        detailDialogBinding.item = notification
        dialog.setContentView(detailDialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
    }

    fun addDevice(viewClicked: View) {
        val view = layoutInflater.inflate(R.layout.add_device_dialog, null)
        val alertDialog = AlertDialog.Builder(this@MainActivity).create()
        val etComments = view.findViewById(R.id.etComments) as EditText
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ -> binding.devicesVm!!.addDevice(etComments.text.toString()) }
        alertDialog.setView(view);
        alertDialog.show();
    }
}