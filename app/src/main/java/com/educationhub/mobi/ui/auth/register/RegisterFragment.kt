package com.educationhub.mobi.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.educationhub.mobi.MainActivity
import com.educationhub.mobi.databinding.FragmentRegisterBinding
import com.educationhub.mobi.ui.auth.AuthenticationViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val authViewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val registerButton: Button = binding.btnRegister
        val emailEditText: EditText = binding.etextEmail
        val passwordEditText: EditText = binding.etextPassword
        val usernameEditText: EditText = binding.etextUsername
        val fullNameEditText: EditText = binding.etextFullname
        val loginText: TextView = binding.textClickableLogin

        loginText.setOnClickListener {
            findNavController().navigateUp()
        }

        registerButton.setOnClickListener {
            createUser(
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                usernameEditText.text.toString(),
                fullNameEditText.text.toString()
            )
        }

        return root
    }

    private fun createUser(email: String, password: String, username: String, fullName: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        if (email.isEmpty()) {
            Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        } else if (password.isEmpty()) {
            Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        } else if (password.length < 8) {
            Toast.makeText(context, "Password must have minimum 8 characters", Toast.LENGTH_SHORT)
                .show()
            return
        } else if (username.isEmpty()) {
            Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        } else if (fullName.isEmpty()) {
            Toast.makeText(context, "Full name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {

                val docRef = db.document("/users/${auth.currentUser!!.uid}")

                val batch = db.batch()

                batch.set(
                    docRef, hashMapOf(
                        "username" to username,
                        "fullName" to fullName,
                        "avatarImageURL" to ""
                    )
                )
                batch.commit()
                Toast.makeText(context, "Register successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}