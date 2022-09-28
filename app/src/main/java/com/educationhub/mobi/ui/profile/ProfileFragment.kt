package com.educationhub.mobi.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.educationhub.mobi.AuthenticationActivity
import com.educationhub.mobi.MainActivity
import com.educationhub.mobi.UserViewModel
import com.educationhub.mobi.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userViewModel.getCurrentUser()

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val notLoginLayout: LinearLayoutCompat = binding.notLoggedInLinearLayout
        val loginLayout: LinearLayoutCompat = binding.loggedInLinearLayout
        val loginBtn: Button = binding.btnLogin
        val logoutBtn: Button = binding.btnLogout

        loginBtn.setOnClickListener {
            val intent = Intent(activity, AuthenticationActivity::class.java)
            startActivity(intent)
        }
        logoutBtn.setOnClickListener {
            userViewModel.logoutUser()
            Toast.makeText(context, "Logged out successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            activity?.finish()
        }

        if (!userViewModel.getCurrentUser()) {
            notLoginLayout.visibility = View.VISIBLE
            loginLayout.visibility = View.GONE
            loginBtn.visibility = View.VISIBLE
        } else {
            notLoginLayout.visibility = View.GONE
            loginLayout.visibility = View.VISIBLE
            loginBtn.visibility = View.GONE

            binding.textEmail.text = userViewModel.currentUser!!.email
            userViewModel.userInfoResponseLiveData.observe(viewLifecycleOwner) {
                if (it.UserInfo != null) {
                    binding.textFullname.text = it.UserInfo!!.fullName
                    binding.textUsername.text = it.UserInfo!!.username
                    if (!it.UserInfo!!.avatarImageURL.isNullOrEmpty()) {
                        binding.avatarImage.load(it.UserInfo!!.avatarImageURL)
                    }
                } else {
                    Log.e("Profile", it.exception?.message.toString())
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}