package com.burak.iot.view

/**
 * Created by burakakgun on 8/31/2018.
 */
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.burak.iot.R
import com.burak.iot.viewmodel.DeviceViewModel


class LoadDevicesDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.saved_devices_list, container, false)
        val recycler_saved_devices = view.findViewById<RecyclerView>(R.id.recycler_saved_devices)
        recycler_saved_devices.setHasFixedSize(true)
        recycler_saved_devices.addItemDecoration(DividerItemDecoration(recycler_saved_devices.context, DividerItemDecoration.VERTICAL))
        val dvm = ViewModelProviders.of(activity!!).get(DeviceViewModel::class.java)
        val adapter = DevicesAdapter {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(recycler_saved_devices.getWindowToken(), 0)
            Snackbar.make(view, getString(R.string.update_device), Snackbar.LENGTH_LONG).show()
            dvm.changeDevice(it)
        }
        recycler_saved_devices.adapter = adapter
        dvm.deviceProcessor.repository.liveData.observe(this, Observer {
            if (it != null) {
                adapter.updateList(it)
            }
        })
        return view
    }
}