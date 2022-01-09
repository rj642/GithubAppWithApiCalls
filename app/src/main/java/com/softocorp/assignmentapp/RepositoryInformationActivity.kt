package com.softocorp.assignmentapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.softocorp.assignmentapp.databinding.ActivityRepositoryInformationBinding
import com.softocorp.assignmentapp.db.RepositoryDatabase
import com.softocorp.assignmentapp.model.RepositoryModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class RepositoryInformationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRepositoryInformationBinding

    var repositoryDao = RepositoryDatabase.instance.getRepositoryDao()
    var repositoryInfo = RepositoryModel("", "", "", null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepositoryInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        binding.apply {
            buttonSend.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "This is the repo url: " + repositoryInfo.html_url.toString()
                )
                shareIntent.type = "text/*"
                val sendIntent = Intent.createChooser(shareIntent, null)
                startActivity(sendIntent)
            }
        }

        val id = intent.getLongExtra("id", -1L)
        if (id != -1L) {
            repositoryInfo = repositoryDao.queryRepositoryById(id)
            binding.apply {
                txtRepositoryName.text = repositoryInfo.name
                txtRepositoryDescription.text = repositoryInfo.description
            }
            val owner = repositoryInfo.full_name
            val ownerName = getOwnerName(owner)
            val url = "https://api.github.com/repos/${ownerName}/${binding.txtRepositoryName.text}/issues?state=open"
            val queue = Volley.newRequestQueue(this@RepositoryInformationActivity)

            val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                { response ->
                    Log.d("AssignmentApp", "response is ${response.length()}")
                    val json = response.length()
                    binding.buttonIssues.text = "Issues(${json})"
                }, { error ->
                    Log.d("AssignmentApp", "response error is $error")
                    binding.buttonIssues.text = "Issues(0)"
                })
            queue.add(jsonObjectRequest)
        }

    }

    private fun getOwnerName(owner: String?): String {
        val pattern = Pattern.compile("\\w+")
        val matcher: Matcher = pattern.matcher(owner!!)
        val selection = 0
        var start = 0
        var end = 0
        var currentWord = ""
        while(matcher.find()) {
            start = matcher.start()
            end = matcher.end()
            if (selection in start..end) {
                currentWord = owner.subSequence(start, end).toString()
                break
            }
        }
        return currentWord
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_more_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val items = item.itemId

        when (items) {
            R.id.delete_repo -> {
                val id = intent.getLongExtra("id", -1L)
                if (id != -1L) {
                    repositoryDao.queryDeleteById(id)
                    onBackPressed()
                }
            }
            R.id.view_repo -> {
                val id = intent.getLongExtra("id", -1L)
                if (id != -1L) {
                    repositoryInfo = repositoryDao.queryRepositoryById(id)
                    val url = repositoryInfo.html_url
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(url))
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}