package binar.academy.challengefourthsample.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import binar.academy.challengefourthsample.MainActivity
import binar.academy.challengefourthsample.R
import binar.academy.challengefourthsample.data.Notes
import binar.academy.challengefourthsample.data.database.NotesDatabase
import binar.academy.challengefourthsample.databinding.ItemNotesBinding

class NotesAdapter(
    private val notesList: List<Notes>,
    private val edit : (Notes) -> Unit
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemBinding = ItemNotesBinding.bind(view)
        val editIV = view.findViewById<ImageView>(R.id.ivEdit)
        fun noteBind(data: Notes) {
            with(itemBinding) {
                titleTextviewItem.setText(data.title)
                notesTextviewItem.setText(data.notes)
            }
        }
        fun deleteItem(data:Notes){
            itemBinding.ivDelete.setOnClickListener {
                AlertDialog.Builder(it.context).setPositiveButton("Ya") { _, _ ->
                    val mDb = NotesDatabase.getInstance(itemView.context)

                    Thread {
                        val result = mDb?.notesDao()?.deleteNotes(data)

                        (itemView.context as MainActivity).runOnUiThread {
                            if (result!=0){
                                Toast.makeText(it.context,"Data ${data.title} berhasil dihapus",
                                    Toast.LENGTH_LONG).show()
                                (itemView.context as MainActivity).recreate()
                            }else{
                                Toast.makeText(it.context,"Data ${data.title} Gagal dihapus",
                                    Toast.LENGTH_LONG).show()
                            }
                        }

                    }.start()
                }.setNegativeButton("Tidak"
                ) { p0, _ ->
                    p0.dismiss()
                }
                    .setMessage("Apakah Anda Yakin ingin menghapus data ${data.title}").setTitle("Konfirmasi Hapus").create().show()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = notesList[position]
        holder.noteBind(data)
        holder.deleteItem(data)
        holder.editIV.setOnClickListener {
            edit.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


}