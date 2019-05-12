package com.thelkl.taptaptap

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import java.io.*

fun showFragment(fm: FragmentManager, fragment: Fragment) {
    fm.beginTransaction().show(fragment).commit()
}

fun hideFragment(fm: FragmentManager, fragment: Fragment) {
    fm.beginTransaction().hide(fragment).commit()
}

// creates a new EndgameDialog and passed required data
fun newEndgameDialogInstance(taps: Int, ifHighscore: Boolean): EndgameDialogFragment {
    val fragment = EndgameDialogFragment()
    val bundle = Bundle()
    bundle.putInt(ENDGAME_DIALOG_FRAGMENT_TAPS_KEY, taps)
    bundle.putBoolean(ENDGAME_DIALOG_FRAGMENT_IFHIGHSCORE_KEY, ifHighscore)
    fragment.arguments = bundle

    return fragment
}

// Serializes an object into a string
fun <T : Serializable> serialize(obj: T?): String {
    if (obj == null) {
        return ""
    }

    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(obj)
    oos.close()

    return baos.toString("ISO-8859-1")
}

// Deserializes a string into an object
@Suppress("UNCHECKED_CAST")
fun <T : Serializable> deserialize(string: String): T {
    val bais = ByteArrayInputStream(string.toByteArray(charset("ISO-8859-1")))
    val ois = ObjectInputStream(bais)

    return ois.readObject() as T
}
