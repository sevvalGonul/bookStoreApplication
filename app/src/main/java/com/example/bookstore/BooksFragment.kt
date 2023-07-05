package com.example.bookstore

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstore.api.BookApiService
import com.example.bookstore.databinding.FragmentBooksBinding
import com.example.bookstore.model.BookData
import com.example.bookstore.view.BookAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BooksFragment : Fragment() {
    private lateinit var binding: FragmentBooksBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var rvAdapter : BookAdapter
    private lateinit var apiService : BookApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBooksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Api Call!!!
        apiService = BookApiService.getInstance()

        requestBooks()

        //var bookList = ArrayList<BookData>()
        /*for(i in 1..10) {
            bookList.add(BookData("Anna Karenina", "20", "Tolstoy", R.drawable.anna))
        }*/

        //initRecyclerView(bookList)

        //binding.addButton.setOnClickListener { addNewBookInfo(bookList) }

    }

    private fun requestBooks() {
        var call = apiService.getBooks()
        //aşağıda Liste halinde kitaplar alınıyor
        call.enqueue(object: Callback<List<BookData>> {
            override fun onResponse(call: Call<List<BookData>>, response: Response<List<BookData>>) {
                if(response.code() == 200) {
                    var myResponse: List<BookData>? = response.body()
                    print(myResponse)
                    initRecyclerView(myResponse!!)
                }
            }

            override fun onFailure(call: Call<List<BookData>>, t: Throwable) {
                Toast.makeText(requireContext(),"Bir hata oluştu :(", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addNewBookInfo(bookList : ArrayList<BookData>) {
        val inflter = LayoutInflater.from(requireContext())
        val v = inflter.inflate(R.layout.add_item,null)
        /**set view*/
        val bookNameEt = v.findViewById<EditText>(R.id.sbookName)
        val priceEt = v.findViewById<EditText>(R.id.sbookPrice)
        val authorEt = v.findViewById<EditText>(R.id.sbookAuthor)
        val addImgBtn = v.findViewById<Button>(R.id.addImageBtn)


        val addDialog = AlertDialog.Builder(requireContext())

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val bookName = bookNameEt.text.toString()
            val price = priceEt.text.toString()
            val author = authorEt.text.toString()

            // Yeni kitap burada ekleniyor!!!


            //bookList.add(BookData(bookName,price,author,R.drawable.bookimg))
            //rvAdapter.notifyDataSetChanged()
            rvAdapter.notifyItemInserted(bookList.size - 1) //???
            Toast.makeText(requireContext(),"Yeni Kitap Başarı ile Eklendi",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(requireContext(),"İptal",Toast.LENGTH_SHORT).show()

        }

        //addImgBtn.setOnClickListener { pickImage() }
        addDialog.create()
        addDialog.show()
    }

    fun initRecyclerView(bookList : List<BookData>) {
        //linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager = LinearLayoutManager(binding.mRecycler.context, LinearLayoutManager.VERTICAL, false)
        binding.mRecycler.layoutManager = linearLayoutManager  // Context?
        binding.mRecycler.setHasFixedSize(true)
        rvAdapter  = BookAdapter(requireContext(), bookList) // Context?
        binding.mRecycler.adapter = rvAdapter

    }

    /*private val PICK_IMAGE_REQUEST = 1 // Galeri seçim isteği kodu

     // addImageBtn butonuna tıklanınca galeriye gitmek için onClickListener eklenir
     private fun pickImage(){
        // Galeriye gitmek için izin kontrolü yapılır
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // İzin zaten verilmişse galeriye git
            openGallery()
        } else {
            // İzin verilmemişse izin iste
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        }
    }

    // İzin isteme sonucunu kontrol etmek için onRequestPermissionsResult metodu eklenir
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildiyse galeriye git
                openGallery()
            } else {
                // İzin verilmediyse hata mesajı göster
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Galeriye gitmek için openGallery fonksiyonu tanımlanır
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    // Görsel seçimi sonucunu kontrol etmek için onActivityResult metodu eklenir
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            // Seçilen görseli kullan
            // imageUri değişkeni seçilen görselin URI'sini içerir
        }
    }*/


}