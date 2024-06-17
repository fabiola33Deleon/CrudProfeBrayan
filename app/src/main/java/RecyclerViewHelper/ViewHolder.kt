package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fabiola.deleon.myapplication.R

class ViewHolder (view:View): RecyclerView.ViewHolder(view){
    val txtcard = view.findViewById<TextView>(R.id.txt_productoCard)
    val imgborrar: ImageView = view.findViewById(R.id.img_borrar)
    val imgedit: ImageView = view.findViewById(R.id.img_editar)

}