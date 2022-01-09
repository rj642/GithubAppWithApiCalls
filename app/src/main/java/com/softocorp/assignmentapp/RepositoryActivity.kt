package com.softocorp.assignmentapp

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.softocorp.assignmentapp.databinding.ActivityMainBinding
import com.softocorp.assignmentapp.db.RepositoryDatabase

class RepositoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var repositoryDao = RepositoryDatabase.instance.getRepositoryDao()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        val adapter = RepositoryAdapter(repositoryDao.queryAllRepository())

        binding.apply {
            if (repositoryDao.queryAllRepository().isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                buttonAdd.visibility = View.GONE
            } else {
                recyclerView.visibility = View.GONE
                buttonAdd.visibility = View.VISIBLE
            }

            buttonAdd.setOnClickListener {
                val intent = Intent(this@RepositoryActivity, AddRepositoryActivity::class.java)
                startActivity(intent)
            }

            recyclerView.adapter = adapter
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.findItem(R.id.add_repository)
        item.iconTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val items = item.itemId

        when (items) {
            R.id.add_repository -> {
                startActivity(Intent(this@RepositoryActivity, AddRepositoryActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (binding.recyclerView.adapter as RepositoryAdapter).setNewData(repositoryDao.queryAllRepository())
        if (repositoryDao.queryAllRepository().isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.buttonAdd.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.buttonAdd.visibility = View.VISIBLE
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

}