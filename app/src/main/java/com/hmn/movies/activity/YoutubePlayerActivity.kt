@file:Suppress("DEPRECATION")

package com.hmn.movies.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.valueIterator
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmn.movies.R
import com.hmn.movies.adapter.CategoryAdapter
import com.hmn.movies.adapter.DetailAdapter
import com.hmn.movies.callback.RecyclerViewClickInterface
import com.hmn.movies.room.BannerEntity
import com.hmn.movies.util.Constant
import com.hmn.movies.viewmodel.ViewModelImplement
import com.hmn.youtube.downloader.YouTubeUriExtractor
import com.hmn.youtube.downloader.YtFile
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.File

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class YoutubePlayerActivity : AppCompatActivity(),RecyclerViewClickInterface {
    private val STORAGE_PERMISSION_CODE = 1
    private var myDownId: Long = 0
    lateinit var mViewModel: ViewModelImplement
    lateinit var categoryTitle: String
    lateinit var video_url: String
    lateinit var video_title: String
    lateinit var recycler: RecyclerView
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var downloadButton: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        val player = findViewById<YouTubePlayerView>(R.id.a_you_player)
        val viewDown = findViewById<ImageView>(R.id.share_btn)
        val title = findViewById<TextView>(R.id.youtube_title)

        recycler = findViewById(R.id.recycler_view_youtube_activity)
        // mViewModel = ViewModelProviders.of(this).get(ViewModelImplement::class.java)
        mViewModel = ViewModelProvider(this)[ViewModelImplement::class.java]
        categoryTitle = intent.getStringExtra(Constant.CATEGORY_TITLE)!!
        video_url = intent.getStringExtra(Constant.URL)!!
        video_title = intent.getStringExtra(Constant.TITLE)!!
        downloadButton = findViewById(R.id.download_btn)

        title.text = video_title



        lifecycle.addObserver(player)
        player.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(video_url, 0f)
            }

        })

        viewDown.setOnClickListener {
            val i = Intent()
            i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
            startActivity(i)
        }

        relatedRecyclerView()





        downloadButton.setOnClickListener {

            if (isConnectingToInternet(this)) {

                //.......

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val cm = this.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                    ) as ConnectivityManager

                    val activeNork = cm.activeNetworkInfo!!.type
                    val wifi = ConnectivityManager.TYPE_WIFI

                    if (isConnectingToInternet(this)) {
                        if (activeNork == wifi) {
                            extractVideoUrl()
                        } else {
                            Toast.makeText(
                                this,
                                "Required WIFI for download",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Check your permission and connectivity",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                } else {
                    reqPermission()


                }
            }

            //....
            else {
                Toast.makeText(
                    this,
                    "Check your permission and connectivity",
                    Toast.LENGTH_LONG
                ).show()
            }


        }


    }

    private fun relatedRecyclerView() {
        val entity = mViewModel.repository.getAllBanner()
        val relatedList = entity.toMutableList()
        relatedList.removeIf { s -> !s.category.contains(categoryTitle) }
        relatedList.removeIf { s -> s.id.contains(video_url) }
        categoryAdapter = CategoryAdapter(this, relatedList, relatedList.size,this)
        recycler.adapter = categoryAdapter
        val numOfColomn = resources.getInteger(R.integer.gallery_columns)
        recycler.layoutManager = GridLayoutManager(this, numOfColomn)
    }


    private fun extractVideoUrl() {
        val blah = intent.getStringExtra("video_url")!!

        val youtubeLink = "http://youtube.com/watch?v=$blah"


        @SuppressLint("StaticFieldLeak") val ytEx: YouTubeUriExtractor =
            object : YouTubeUriExtractor(this) {
                @SuppressLint("SetWorldReadable")
                @RequiresApi(Build.VERSION_CODES.R)

                override fun onUrisAvailable(
                    videoId: String,
                    videoTitle: String,
                    ytFiles: SparseArray<YtFile>
                ) {

                    if (videoId != null) {
                        val ytIterator = ytFiles.valueIterator()
                        val ytList = ytIterator.asSequence().toMutableList()


                        ytList.removeIf { s -> !s.format.ext.contains("mp4") }
                        ytList.removeIf { s -> s.format.audioBitrate == -1 }
                        val maxarray = ytList.maxBy { it.format.height }
                        var downloadUrl = maxarray!!.url


                        if (downloadUrl.contains("\\/")) {
                            downloadUrl = downloadUrl.replace("\\/", "/")
                        }

                        StrictMode.setVmPolicy(VmPolicy.Builder().build())
                        val file = File(
                            Environment.getExternalStorageDirectory().toString(),
                            "Youtube Video"
                        )
                        val files = file.listFiles()
                        val uniqueTitle = ArrayList<String>()
                        for (i in files.indices) {
                            val tit = files[i].name
                            val newTit = tit.subSequence(0, 10)
                            uniqueTitle.add(newTit.toString())

                        }
                        val extTitle = videoTitle.subSequence(0, 10)
                        if (uniqueTitle.contains(extTitle)) {
                            Toast.makeText(
                                this@YoutubePlayerActivity,
                                "Music video Already exist",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {

                            downloading(downloadUrl)
                            Toast.makeText(
                                this@YoutubePlayerActivity,
                                "Downloading...",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            this@YoutubePlayerActivity,
                            "Not Interner Connection",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }
            }
        ytEx.execute(youtubeLink)
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun downloading(uri: String) {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Down", Toast.LENGTH_LONG).show()
            val request = DownloadManager.Request(Uri.parse(uri))
            request.setTitle(video_title)
            request.setDescription("File Downloaing")
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//            val nameOfFile = URLUtil.guessFileName(
//                uri, null, MimeTypeMap.getFileExtensionFromUrl(
//                    uri
//                )
//            )
            val videoFile = createFile(video_title)
            StrictMode.setVmPolicy(VmPolicy.Builder().build())

            request.setDestinationUri(Uri.fromFile(videoFile))
            Log.d(
                "Files",
                "Path: " + Environment.getExternalStorageDirectory()
                    .toString() + "/YoutubeDownloader"
            )


            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            myDownId = manager.enqueue(request)
        } else {
            reqPermission()
        }


        val br = object : BroadcastReceiver() {

            override fun onReceive(p0: Context?, p1: Intent?) {
                val id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == myDownId) {
                    Toast.makeText(
                        this@YoutubePlayerActivity,
                        "Download Completed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
        registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    private fun createFile(fileName: String): File {
        val state = Environment.getExternalStorageState()
        val filesDir: File
        if (Environment.MEDIA_MOUNTED == state) {
            filesDir = File(
                Environment.getExternalStorageDirectory().toString(), "Youtube Video"
            )
        } else {
            filesDir = File(getExternalFilesDir(null), "Youtube")
        }
        if (!filesDir.exists()) filesDir.mkdirs()
        return File(filesDir, fileName + ".mp4")
    }


    private fun isConnectingToInternet(context: Context): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if (connectivity != null) {
            val info = connectivity.allNetworkInfo


            if (info != null)
                for (i in info)

                    if (i.state == NetworkInfo.State.CONNECTED) {
                        return true

                    }
        }




        return false
    }


    private fun reqPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("")
            builder.setMessage("")
            builder.setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
            builder.setNegativeButton(
                "cancel"
            ) { p0, _ -> p0!!.dismiss() }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Permission DENIED to save the music video ",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val i = Intent()
//
//        startActivity(i)
    }

    override fun onItemClick(position: Int, bannerEntity: BannerEntity) {
        val category = bannerEntity.category
        val i = Intent(applicationContext, YoutubePlayerActivity::class.java)
        i.putExtra(Constant.CATEGORY_TITLE, category)
        i.putExtra(Constant.URL, bannerEntity.id)
        i.putExtra(Constant.TITLE, bannerEntity.title)
        startActivity(i)
        finish()
    }

    override fun onItemCategoryClick(position: Int, bannerEntity: BannerEntity) {
    }
}