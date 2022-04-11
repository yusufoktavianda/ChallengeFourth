package binar.academy.challengefourthsample

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import binar.academy.challengefourthsample.adapter.NotesAdapter
import binar.academy.challengefourthsample.data.database.NotesDatabase
import binar.academy.challengefourthsample.databinding.FragmentEditBinding
import binar.academy.challengefourthsample.databinding.FragmentHomeBinding
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var mDb: NotesDatabase? = null
    private var _binding: FragmentHomeBinding? = null
    private var sharedPref = "kotlinsharedpreference"

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val shareUsernameValue = sharedPreferences.getString("username_key", "defaultname")

        if (shareUsernameValue.equals("defaultname")) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        } else {
            mDb = NotesDatabase.getInstance(view.context)
            binding.homeRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            fetchData()
            binding.fab.setOnClickListener {
//            fabDialog()
                val dialog = AddFragment {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = mDb?.notesDao()?.insertNotes(it)
                        activity?.runOnUiThread {
                            if (result != 0.toLong()) {
                                //sukses
                                fetchData()
                                Toast.makeText(
                                    requireContext(), "Sukses menambahkan ${it.title}",
                                    Toast.LENGTH_LONG
                                ).show()

                            } else {
                                //gagal
                                Toast.makeText(
                                    requireContext(), "Gagal menambahkan ${it.title}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
//            dialog.show(requireActivity().supportFragmentManager, "tag" )
                dialog.show(parentFragmentManager, "tag")
            }
        }
        binding.logoutButtonHome.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            Toast.makeText(requireContext(), "you're logout", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    fun fetchData() {
        GlobalScope.async {
            val listStudent = mDb?.notesDao()?.getAllNotes()
            activity?.runOnUiThread {
                listStudent?.let {
                    val adapter = NotesAdapter(it, edit = { data ->
                        val dialogBinding =
                            FragmentEditBinding.inflate(LayoutInflater.from(requireContext()))
                        val dialogBuilder =
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        dialogBuilder.setView(dialogBinding.root)
                        val dialog = dialogBuilder.create()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialogBinding.titleEdittextUpdate.setText("${data.title}")
                        dialogBinding.noteEdittextUpdate.setText("${data.notes}")
                        dialogBinding.saveButtonUpdate.setOnClickListener {
                            val mDb = NotesDatabase.getInstance(requireContext())
                            data.title = dialogBinding.titleEdittextUpdate.text.toString()
                            data.notes = dialogBinding.noteEdittextUpdate.text.toString()
                            lifecycleScope.launch(Dispatchers.IO) {
                                val result = mDb?.notesDao()?.updateNotes(data)
                                runBlocking(Dispatchers.Main) {
                                    if (result != 0) {
                                        Toast.makeText(
                                            requireContext(),
                                            "${data.title} Berhasil di update!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        fetchData()
                                        dialog.dismiss()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "${data.title} Gagal di update!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog.dismiss()
                                    }
                                }
                            }
                        }
                        dialog?.show()
                    })
                    binding.homeRecycler.adapter = adapter
                }
            }
        }
    }

    private fun fabDialog() {
////        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_add, null, false)
//        val viewb = FragmentAddBinding.inflate(LayoutInflater.from(requireContext()), null, false)
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//        dialogBuilder.setView(viewb.root)
//        val dialog = dialogBuilder.create()
//
////            view.findViewById<Button>(R.id.save_button).setOnClickListener {
//
//            }
//        dialog.show()


//        val builder = AlertDialog.Builder(requireContext())
////        dialogFragment.show(childFragmentManager, null)
//        val inflater = view?.context
//        builder.setView(inflater.inflate(R.layout.fragment_add,null))

    }
}