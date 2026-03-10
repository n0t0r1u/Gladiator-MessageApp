package com.mychat.messageapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mychat.messageapp.MainActivity
import com.mychat.messageapp.R


class EditProfileFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        usernameEditText = view.findViewById(R.id.editTextUsername)
        emailEditText = view.findViewById(R.id.editTextEmail)
        saveButton = view.findViewById(R.id.buttonSave)
        logoutButton = view.findViewById(R.id.buttonLogout)

        loadUserData()

        saveButton.setOnClickListener {
            saveUserData()
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }

        return view
    }
    private fun logoutUser() {
        auth.signOut() // Firebase oturumu kapat
        Toast.makeText(context, "Çıkış yapıldı", Toast.LENGTH_SHORT).show()

        // Giriş ekranına yönlendirme (örnek olarak LoginActivity'ye gidiliyor)
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    private fun loadUserData() {
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid

            firestore.collection("Kullanicilar").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        usernameEditText.setText(document.getString("kullaniciAdi"))
                        emailEditText.setText(document.getString("email"))
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Kullanıcı bilgileri alınamadı: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveUserData() {
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid

            val updatedData = hashMapOf(
                "uid" to userId,
                "kullaniciAdi" to usernameEditText.text.toString(),
                "email" to emailEditText.text.toString()
            )

            firestore.collection("Kullanicilar").document(userId).set(updatedData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profil başarıyla güncellendi", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Profil güncellenemedi: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
