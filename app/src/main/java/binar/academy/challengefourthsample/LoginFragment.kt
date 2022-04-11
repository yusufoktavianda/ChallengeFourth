package binar.academy.challengefourthsample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import binar.academy.challengefourthsample.data.database.NotesDatabase
import binar.academy.challengefourthsample.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private val sharedPrefFile = "kotlinsharedpreference"
    private var _binding: FragmentLoginBinding? = null
    var mDb: NotesDatabase? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mDb = NotesDatabase.getInstance(requireActivity())
        val username = StringBuffer()
        val password = StringBuffer()

        binding.loginButton.setOnClickListener {
            val sharedPreferences: SharedPreferences =
                requireActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            super.onViewCreated(view, savedInstanceState)
            val sharedUsername: String = binding.usernameEdittextLogin.text.toString()
            val sharedPassword: String = binding.passwordEdittext.text.toString()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            Thread {

                val result = mDb?.userDao()?.getAccount(sharedUsername)
                activity?.runOnUiThread {
                    result?.forEach {
                        username.append(it.username)
                        password.append(it.password)

                    }
                    Log.d("Login", username.toString())
                    Log.d("Login", result.toString())

                    if (sharedUsername.isBlank() && sharedPassword.isBlank()){
                        Toast.makeText(requireContext(), "Complete the column!", Toast.LENGTH_SHORT).show()
                    }else if (sharedUsername.isBlank()){
                        Toast.makeText(requireContext(), "Complete username column!", Toast.LENGTH_SHORT).show()
                    }else if (sharedPassword.isBlank()){
                        Toast.makeText(requireContext(), "Complete password column!", Toast.LENGTH_SHORT).show()
                    }else{
                        if(sharedUsername == username.toString() && sharedPassword==password.toString()){
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            editor.putString("username_key", sharedUsername)
                            editor.putString("password_key", sharedPassword)
                            editor.apply()
                        }else if (sharedUsername == username.toString() && sharedPassword != password.toString()){
                            Toast.makeText(requireContext(), "Your password is not correct!", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), "Username is not found!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }.start()


        }


        binding.registerTextview.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}