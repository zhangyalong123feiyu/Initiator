package com.lenovo.Initiator.view

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.app.AlarmManager
import android.content.Context
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.lenovo.Initiator.R
import com.lenovo.Initiator.adapter.CommandAdapter
import com.lenovo.Initiator.presenter.MainPresenter
import com.lenovo.Initiator.protocol.CommandEvent
import com.lenovo.Initiator.protocol.Config
import com.lenovo.Initiator.protocol.IView
import com.lenovo.Initiator.utils.DevicesUtils
import com.lenovo.Initiator.utils.DownloadUtil
import com.lenovo.Initiator.utils.ServiceIsRunUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(), IView {

    private var TAG: String = MainActivity::class.java.simpleName
    private var listData: ArrayList<String>? = arrayListOf()
    private lateinit var commadRclerView: RecyclerView
    private var commandAdapter: CommandAdapter? = null
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        // register eventBus
        //

        EventBus.getDefault().register(this)
        mainPresenter = MainPresenter(this, this)

        //
        // If the service is not running then run it.
        //

        if (!ServiceIsRunUtil.isServiceRun(this, "com.lenovo.serverapp.DeviceReceiver")) {
            var intent = Intent(this@MainActivity, DeviceService::class.java)
            startService(intent)
        }

        findViewById<Button>(R.id.button).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var intent = Intent(this@MainActivity, DeviceService::class.java)
                startService(intent)
            }
        })

        findViewById<Button>(R.id.button_show).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var dowLoadUtil = DownloadUtil(this@MainActivity)
                dowLoadUtil.showtNotification("30")
                DevicesUtils.killApp(this@MainActivity, "com.lenovo.billor.gd")
            }
        })

        findViewById<Button>(R.id.button_restart).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val mgr = this@MainActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val cn = ComponentName("com.lenovo.billor.gd", "com.lenovo.billing.views.MainActivity")
                intent.component = cn
                startActivity(intent)
            }
        })

        commadRclerView = findViewById(R.id.commandRecy)
        commadRclerView.layoutManager = LinearLayoutManager(this)

        if(!Config.gui){
            moveTaskToBack(true)                               // Hidden GUI
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetMsg(commandEvent: CommandEvent) {
        Log.i("TAG", commandEvent.command)
        listData!!.add(commandEvent.command)

        if (commandAdapter == null) {
            commandAdapter = CommandAdapter(this, listData!!)
            commadRclerView.adapter = commandAdapter
        } else {
            commadRclerView.adapter = commandAdapter
        }
        commandAdapter!!.notifyDataSetChanged()

        if (commandEvent.command.equals("download")) {
            mainPresenter.downloadApk("", this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onGetDownloadSucess(msg: String) {
        Log.i(TAG, msg)
    }

    override fun onGetDownloadFailed(erro: String) {
        Log.i(TAG, erro)
    }
}
