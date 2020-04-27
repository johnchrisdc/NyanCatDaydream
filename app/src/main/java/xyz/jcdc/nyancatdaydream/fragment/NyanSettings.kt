package xyz.jcdc.nyancatdaydream.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.leanback.preference.LeanbackPreferenceFragment
import androidx.leanback.preference.LeanbackSettingsFragment
import androidx.preference.*
import xyz.jcdc.nyancatdaydream.R
import xyz.jcdc.nyancatdaydream.utils.SharedPrefsUtil

class NyanSettings : LeanbackSettingsFragment() {

    override fun onPreferenceStartScreen(caller: PreferenceFragment?, pref: PreferenceScreen?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragment?, pref: Preference?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPreferenceStartInitialScreen() {
        startPreferenceFragment(PrefFragment())
    }

    class PrefFragment : LeanbackPreferenceFragment(), Preference.OnPreferenceChangeListener {
        var preference_count: SeekBarPreference? = null
        var preference_speed: SeekBarPreference? = null

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.nyan_settings);

            preference_count = findPreference<SeekBarPreference>("preference_count")
            preference_count?.onPreferenceChangeListener = this
            preference_count?.value = SharedPrefsUtil.getCount(activity)


            preference_speed = findPreference<SeekBarPreference>("preference_speed")
            preference_speed?.onPreferenceChangeListener = this
            preference_speed?.value = SharedPrefsUtil.getSpeed(activity)
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            when(preference) {
                preference_count -> {
                    var value: Int = newValue as Int
                    SharedPrefsUtil.setCount(activity, value)

                }
                preference_speed -> {
                    var value: Int = newValue as Int
                    SharedPrefsUtil.setSpeed(activity, value)
                }
            }

            return true
        }

    }
}