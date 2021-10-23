package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.trello_clone.R
import com.google.firebase.auth.FirebaseAuth
import firebase.FirestoreClass
import model.User

class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        findViewById<Button>(R.id.btn_sign_in).setOnClickListener{
            signInRegisteredUser()
        }

        setupActionBar()
    }

    private fun setupActionBar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_sign_in_activity)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun signInRegisteredUser(){
        val etEmail = findViewById<TextView>(R.id.et_email_sign_in)
        val etPassword = findViewById<TextView>(R.id.et_password_sign_in)

        val email: String = etEmail.text.toString().trim { it <= ' ' }
        val password: String = etPassword.text.toString().trim { it <= ' ' }

        if(validateForm(email, password)){
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        FirestoreClass().loadUserData(this@SignInActivity)
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to get the user details from the firestore database after authentication.
     */
    fun signInSuccess(user: User) {

        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }
    // END
}