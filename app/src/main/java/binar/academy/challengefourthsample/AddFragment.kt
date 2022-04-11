package binar.academy.challengefourthsample

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import binar.academy.challengefourthsample.data.Notes
import binar.academy.challengefourthsample.data.database.NotesDatabase
import binar.academy.challengefourthsample.databinding.FragmentAddBinding

class AddFragment(private val listNote : (Notes)->Unit) : DialogFragment() {
    var mDb: NotesDatabase? = null
    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentAddBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return super.onCreateDialog(savedInstanceState)
        mDb = NotesDatabase.getInstance(requireContext())
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
            binding.apply {
                saveButton.setOnClickListener {
                    val title : String = binding.titleEdittext.text.toString()
                    val note : String = binding.noteEdittext.text.toString()
                    val user = Notes(
                        null,title,note
                    )
                    Log.d("TAG", user.toString())
                    listNote(user)
                    dialog?.dismiss()
                }
            }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        NotesDatabase.destroyInstance()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mDb = NotesDatabase.getInstance(requireContext())
//        binding.saveButton.setOnClickListener {
//            val objectNotes = Notes(
//                null,
//                binding.titleEdittext.text.toString(),
//                binding.noteEdittext.text.toString()
//            )
//
//            lifecycleScope.launch(Dispatchers.IO) {
//                val result = mDb?.notesDao()?.insertNotes(objectNotes)
//                activity?.runOnUiThread {
//                    if (result != 0.toLong()) {
//                        //sukses
//                        Toast.makeText(
//                            requireContext(), "Sukses menambahkan ${objectNotes.title}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        homeFragment.fetchData()
//                    } else {
//                        //gagal
//                        Toast.makeText(
//                            requireContext(), "Gagal menambahkan ${objectNotes.title}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                }
//            }
//        }
//        dialog?.dismiss()
//    }
}