package com.softocorp.assignmentapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.softocorp.assignmentapp.databinding.ActivityAddRepositoryBinding
import com.softocorp.assignmentapp.db.RepositoryDatabase
import com.softocorp.assignmentapp.model.RepositoryModel

class AddRepositoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRepositoryBinding

    var repoInformation = RepositoryModel("", "", "", null, "")

    var repositoryDao = RepositoryDatabase.instance.getRepositoryDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        binding.apply {
            buttonAddToRepository.setOnClickListener {
                val url = "https://api.github.com/repos/${etOwnerName.text}/${etRepositoryName.text}"
                val queue = Volley.newRequestQueue(this@AddRepositoryActivity)

                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->
                        if (response.getString("name").isNotEmpty()) {
                            val name = response.getString("name")
                            val fullName = response.getString("full_name")
                            val desc = response.getString("description")
                            val hasIssues = response.getBoolean("has_issues")
                            val htmlLink = response.getString("html_url")
                            repoInformation.name = name
                            repoInformation.full_name = fullName
                            repoInformation.description = desc
                            repoInformation.has_issues = hasIssues
                            repoInformation.html_url = htmlLink
                            repositoryDao.addRepository(repoInformation)
                            onBackPressed()
                        }
                    }, { error ->
                        Toast.makeText(
                            this@AddRepositoryActivity,
                            "Some Error occurred!",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                queue.add(jsonObjectRequest)
            }
        }

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}