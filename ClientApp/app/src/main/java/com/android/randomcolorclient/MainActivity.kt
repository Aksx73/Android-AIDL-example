package com.android.randomcolorclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.colorgeneratorserver.IColorAidlInterface
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

	private var aidlColorService: IColorAidlInterface? = null

	private val serviceConnection = object : ServiceConnection {
		override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
			aidlColorService = IColorAidlInterface.Stub.asInterface(binder)
			Log.d("TAG", "onServiceConnected called")
		}

		override fun onServiceDisconnected(p0: ComponentName?) {
			aidlColorService = null
			Log.d("TAG", "onServiceDisconnect called")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		setContentView(R.layout.activity_main)
		/*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}*/

		val intent = Intent("com.android.colorgeneratorserver.AIDLColorService")
		intent.setClassName(
			"com.android.colorgeneratorserver",
			"com.android.colorgeneratorserver.AIDLColorService"
		)
		bindService(intent, serviceConnection, BIND_AUTO_CREATE)

		Log.d("TAG", "bindService called")

		val button = findViewById<MaterialButton>(R.id.materialButton)
		val imageView = findViewById<ImageView>(R.id.imageView)

		button.setOnClickListener {
			try {
				val color1 = aidlColorService?.generateColor()
				val color2 = aidlColorService?.generateColor()
				// Create a GradientDrawable with the two retrieved colors
				val gradientDrawable = GradientDrawable(
					GradientDrawable.Orientation.LEFT_RIGHT,
					intArrayOf(color1 ?: 0, color2 ?: 0)
				)
				//gradientDrawable.shape = GradientDrawable.RECTANGLE
				imageView.background = gradientDrawable
			} catch (e: RemoteException) {
				e.printStackTrace()
			}

		}

	}

	override fun onDestroy() {
		super.onDestroy()
		unbindService(serviceConnection)
		Log.d("TAG", "unbindService called")
	}


}