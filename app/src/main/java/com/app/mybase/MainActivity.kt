package com.app.mybase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mybase.base.BaseActivity
import com.app.mybase.databinding.ActivityMainBinding
import com.app.mybase.helper.ApisResponse
import com.app.mybase.model.GetResponse
import com.app.mybase.model.UserInputDto
import com.app.mybase.room.UserEntity
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity() {

    val TAG = this::class.java.name
    lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainViewModel
    lateinit var userRV: RecyclerView
    lateinit var adapter: UserAdapter

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewmodel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.mainViewModel = viewmodel
        binding.lifecycleOwner = this@MainActivity

        adapterSetUp()
        listeners()
        getDataFromLocalDatabase()

    }

    private fun adapterSetUp() {
        userRV = binding.userRv
        userRV.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter()
        userRV.adapter = adapter
    }

    private fun listeners() {
        binding.buttonSubmit.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val emailId = binding.emailEditText.text.toString()
            val mobile = binding.mobileEditText.text.toString()
            var selectedGender = ""
            val selectedRadioButtonId = binding.radioGroupGender.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                selectedGender = findViewById<RadioButton?>(selectedRadioButtonId).text.toString()
            }
            // Verify all details and call post api
            if (name.isNotEmpty() && emailId.isNotEmpty() && mobile.isNotEmpty() && selectedGender.isNotEmpty()) {
                Log.d(TAG, "listeners: $name $mobile $emailId $selectedGender")
                postDetails(UserInputDto(name, mobile, emailId, selectedGender))
            } else {
                Toast.makeText(this, "Please enter correct details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postDetails(userInputDto: UserInputDto) {
        viewmodel.postUserDetails(userInputDto).observe(this, Observer { apiResponse ->
            when (apiResponse) {
                is ApisResponse.Success -> {
                    // Get User details from api
                    getDetails()
                }
                is ApisResponse.Error -> {
                    Toast.makeText(this, apiResponse.exception, Toast.LENGTH_SHORT).show()
                }
                is ApisResponse.Loading -> {
                    viewmodel.showProgress()
                }
                is ApisResponse.Complete -> {
                    viewmodel.hideProgress()
                }
                else -> {}
            }
        })
    }

    private fun getDetails() {
        viewmodel.getUserDetails().observe(this, Observer { apiResponse ->
            when (apiResponse) {
                is ApisResponse.Success -> {
                    Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT)
                        .show()
                    if (apiResponse.response.isNotEmpty()) {
                        updateUserListToUIAndDatabase(apiResponse)
                    } else {
                        Toast.makeText(this, "User details is empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is ApisResponse.Error -> {
                    Toast.makeText(this, apiResponse.exception, Toast.LENGTH_SHORT).show()
                }
                is ApisResponse.Loading -> {
                    viewmodel.showProgress()
                }
                is ApisResponse.Complete -> {
                    viewmodel.hideProgress()
                }
                else -> {}
            }
        })
    }

    private fun updateUserListToUIAndDatabase(apiResponse: ApisResponse.Success<out List<GetResponse>>) {
        val userList = ArrayList<UserEntity>()
        apiResponse.response.forEach {
            userList.add(
                UserEntity(
                    it._id,
                    it.userName,
                    it.phoneNumber,
                    it.eMail,
                    it.gender
                )
            )
        }
        Log.d(TAG, "getUserDetails: userList $userList")
        binding.userListText.visibility = View.VISIBLE
        adapter.updateList(userList)
        // Inserting all the data to the Local database
        viewmodel.insertAll(userList)
    }

    private fun getDataFromLocalDatabase() {
        viewmodel.getAllUser().observe(this, Observer {
            Log.d(TAG, "getAllUser: userList $it")
        })
    }

}