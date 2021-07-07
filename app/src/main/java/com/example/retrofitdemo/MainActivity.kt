package com.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retrofitService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        //getRequestWithPathParameter()
        getRequestWithQueryParameter()
    }

    private fun getRequestWithQueryParameter() {
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retrofitService.getSortedAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result = " " + "Album Id : ${albumsItem.id}" + "\n" +
                            " " + "Album Title : ${albumsItem.title}" + "\n" +
                            " " + "User Id : ${albumsItem.userId}" + "\n\n\n"
                    textView.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameter() {
        val pathResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, title, Toast.LENGTH_LONG).show()
        })
    }
}