package xyz.jcdc.nyancatdaydream.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsUtil {

    companion object {
        val PREFS_NYAN = "prefs_nyan";
        val KEY_COUNT = "key_count";
        val KEY_SPEED = "key_speed"

        @JvmStatic
        fun getNyanPreferences(context: Context) : SharedPreferences {
            return context.getSharedPreferences(PREFS_NYAN, Context.MODE_PRIVATE)
        }

        @JvmStatic
        fun setCount(context: Context, count: Int) {
            var editor : SharedPreferences.Editor = getNyanPreferences(context).edit()
            editor.putInt(KEY_COUNT, count)
            editor.commit()
        }

        @JvmStatic
        fun getCount(context: Context) : Int {
            return getNyanPreferences(context).getInt(KEY_COUNT, 20)
        }

        @JvmStatic
        fun setSpeed(context: Context, speed: Int) {
            var editor: SharedPreferences.Editor = getNyanPreferences(context).edit()
            editor.putInt(KEY_SPEED, speed)
            editor.commit()
        }

        @JvmStatic
        fun getSpeed(context: Context) : Int {
            return getNyanPreferences(context).getInt(KEY_SPEED, 2)
        }
    }
}