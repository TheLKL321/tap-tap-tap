package com.thelkl.taptaptap.utils

import android.os.Bundle
import com.thelkl.taptaptap.EndgameDialogFragment
import java.io.*

// creates a new EndgameDialog and passed required data
fun newEndgameDialogInstance(taps: Int, ifHighscore: Boolean): EndgameDialogFragment {
    val fragment = EndgameDialogFragment()
    val bundle = Bundle()
    bundle.putInt(ENDGAME_DIALOG_FRAGMENT_TAPS_KEY, taps)
    bundle.putBoolean(ENDGAME_DIALOG_FRAGMENT_IFHIGHSCORE_KEY, ifHighscore)
    fragment.arguments = bundle
    fragment.isCancelable = false

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
