package activities


import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.trello_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import firebase.FirestoreClass
import model.User

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        setContentView(R.layout.activity_sign_up)

        setupActionBar()
    }

    private fun setupActionBar(){
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }

        findViewById<Button>(R.id.btn_sign_up).setOnClickListener{
            registerUser()
        }
    }

    /**
     * A function to register a user to our app using the Firebase.
     * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
     */
    private fun registerUser(){
        val etName = findViewById<TextView>(R.id.et_name)
        val etEmail = findViewById<TextView>(R.id.et_email)
        val etPassword = findViewById<TextView>(R.id.et_password)

        val name: String = etName.text.toString().trim { it <= ' ' }
        val email: String = etEmail.text.toString().trim { it <= ' ' }
        val password: String = etPassword.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {

             showProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->


                    // If the registration is successfully done
                    if (task.isSuccessful) {

                        // Firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        // Registered Email
                        val registeredEmail = firebaseUser.email!!

                        val user = User(
                            firebaseUser.uid, name, registeredEmail
                        )

                        // call the registerUser function of FirestoreClass to make an entry in the database.
                        FirestoreClass().registerUser(this@SignUpActivity, user)

                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    /**
     * A function to validate the entries of a new user.
     */
    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
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
     * A function to be called the user is registered successfully and entry is made in the firestore database.
     */
    fun userRegisteredSuccess() {

        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }
}