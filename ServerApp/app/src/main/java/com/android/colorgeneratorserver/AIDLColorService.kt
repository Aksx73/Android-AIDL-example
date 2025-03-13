package com.android.colorgeneratorserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import kotlin.random.Random


class AIDLColorService : Service() {

	override fun onBind(intent: Intent): IBinder {
		return binder
	}

	private val binder: IColorAidlInterface.Stub = object : IColorAidlInterface.Stub(){
		@Throws(RemoteException::class)
		override fun generateColor(): Int {
			val random = Random.Default
			val color = 0xff000000.toInt() or (random.nextInt(0x1000000))
			Log.d("TAG", "generateColor: $color")
			return color
		}
	}

}