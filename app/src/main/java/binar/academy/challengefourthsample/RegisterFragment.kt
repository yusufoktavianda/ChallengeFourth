package binar.academy.challengefourthsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import binar.academy.challengefourthsample.data.User
import binar.academy.challengefourthsample.data.database.NotesDatabase
import binar.academy.challengefourthsample.databinding.FragmentRegisterBinding
import java.util.concurrent.Executors


class RegisterFragment() : Fragment() {
    private var mDb: NotesDatabase? = null
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    var data: List<User>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernamebuf = StringBuffer()
        val passwordbuf = StringBuffer()
        mDb = NotesDatabase.getInstance(requireContext())
        binding.registerButton.setOnClickListener {
            val username = binding.usernameEdittextRegister.text.toString()
            val email = binding.emailEdittextRegister.text.toString()
            val password = binding.passwordEdittextRegister.text.toString()
            val conpassword = binding.confirmpasswordEdittextRegister.text.toString()

            if (username.equals("") and password.equals("") and email.equals("") and conpassword.equals(
                    ""
                )
            ) {
                Toast.makeText(requireContext(), "Complete the column!", Toast.LENGTH_SHORT).show()
            } else if (username.equals("")) {
                Toast.makeText(requireContext(), "Complete username column!", Toast.LENGTH_SHORT)
                    .show()
            } else if (email.equals("")) {
                Toast.makeText(requireContext(), "Complete email column!", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.equals("")) {
                Toast.makeText(requireContext(), "Complete password column!", Toast.LENGTH_SHORT)
                    .show()
            } else if (conpassword.equals("")) {
                Toast.makeText(
                    requireContext(),
                    "Complete confirm password column!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (conpassword.equals(password)) {
                    val objectRegister = User(
                        null,
                        binding.usernameEdittextRegister.text.toString(),
                        binding.emailEdittextRegister.text.toString(),
                        binding.passwordEdittextRegister.text.toString()
                    )
                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute {
                        val result = mDb?.userDao()?.insertUser(objectRegister)
                        activity?.runOnUiThread {
                            if (result != 0.toLong()) {
                                //sukses
                                Toast.makeText(
                                    requireContext(),
                                    "Sukses menambahkan ${objectRegister.username}",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
                            } else {
                                //gagal
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal menambahkan ${objectRegister.username}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Confirm password is not correct!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            //end of register parsing data
        }
    }
}
