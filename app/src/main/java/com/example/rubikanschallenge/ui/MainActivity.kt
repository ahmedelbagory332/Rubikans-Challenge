package com.example.rubikanschallenge.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rubikanschallenge.R
import com.example.rubikanschallenge.adapter.UserAdapter
import com.example.rubikanschallenge.model.Users
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() , SearchView.OnQueryTextListener{

    private val viewModel: UserViewModel by viewModels()
    @Inject
    lateinit var userAdapter: UserAdapter
    private lateinit var userList: RecyclerView
    lateinit var errorImage: ImageView
    private lateinit var progressBar: ProgressBar
    private var usersList: ArrayList<Users> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList  = findViewById(R.id.recycler_view)
        errorImage  = findViewById(R.id.error)
        progressBar = findViewById(R.id.progress_bar)
        userList.adapter = userAdapter

        viewModel.getUsers()
        viewModel.users.observe(this, Observer {

            if(it.isLoading){
                progressBar.visibility = View.VISIBLE
                userAdapter.submitList(emptyList())
            }else if(it.users.isNotEmpty()){
                userAdapter.submitList(it.users)
                usersList = it.users as ArrayList<Users>
                progressBar.visibility = View.GONE
            }else{
                errorImage.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Log.d("bego error",it.error)
                progressBar.visibility = View.GONE
            }

        })

        userAdapter.onItemClick = { userClick ->

            viewModel.getUser(userClick.id)
            viewModel.user.observe(this, Observer {

                if(it.isLoading){
                    progressBar.visibility = View.VISIBLE
                    Toast.makeText(this,"fetching user details...",Toast.LENGTH_SHORT).show()
                }else if(it.user!=null){
                    showUserDialog(it.user)
                    progressBar.visibility = View.GONE
                }else{
                    progressBar.visibility = View.GONE
                    showUserDialog( userClick)
                    Log.d("bego error",it.error)
                }

            })
        }

        userAdapter.onButtonClick = { userClick ->

            editUserDialog(userClick)

        }

        viewModel.updateUser.observe(this, Observer {

            if(it.isLoading){
                Toast.makeText(this,"updating user details...",Toast.LENGTH_SHORT).show()
            }else if(it.updatedAt!=null){
                Toast.makeText(this,"user updated at ${it.updatedAt}",Toast.LENGTH_SHORT).show()
                viewModel.getUsers()
            }else if(it.error.isNotEmpty()){
                Toast.makeText(this, it.error,Toast.LENGTH_SHORT).show()
                Log.d("bego error",it.error)
            }

        })



    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        return true
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val newList: ArrayList<Users> = ArrayList()
        for (item in usersList) {
            if (item.firstName.lowercase(Locale.ROOT).contains(newText!!.lowercase(Locale.ROOT))) {
                newList.add(item)
            }
        }
        userList.adapter = userAdapter
        userAdapter.submitList(newList)
        return true
    }

    private fun showUserDialog(user: Users){

        val dialog: Dialog =  Dialog(this)
        dialog.setContentView(R.layout.show_user_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val textViewFirstName: TextView = dialog.findViewById(R.id.tvFirstName)
        val textViewLastName: TextView = dialog.findViewById(R.id.tvLastName)
        val textViewEmail: TextView = dialog.findViewById(R.id.tvEmail)
        val userProfilePhoto: ImageView = dialog.findViewById(R.id.imageView)

        Glide.with(this)
            .load(user.avatar)
            .into(userProfilePhoto)

        textViewFirstName.text = user.firstName
        textViewLastName.text = user.lastName
        textViewEmail.text = user.email

        dialog.show()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun editUserDialog(user: Users){

        val dialog: Dialog =  Dialog(this)
        dialog.setContentView(R.layout.edit_user_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val userName: EditText = dialog.findViewById(R.id.etName)
        val editUserName: Button = dialog.findViewById(R.id.editName)


            editUserName.setOnClickListener {
                if (userName.text.isNotEmpty()){
                    viewModel.updateUser(user.id, userName.text.toString())

                }else{
                    Toast.makeText(this,"Enter name to update the user",Toast.LENGTH_SHORT).show()
                }

                dialog.cancel()

            }




        dialog.show()
    }
}

