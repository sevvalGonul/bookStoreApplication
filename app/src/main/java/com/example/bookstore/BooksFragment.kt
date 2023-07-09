package com.example.bookstore

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstore.api.BookApiService
import com.example.bookstore.databinding.FragmentBooksBinding
import com.example.bookstore.model.AuthorsItem
import com.example.bookstore.model.BookData
import com.example.bookstore.model.Deneme
import com.example.bookstore.model.PostBook
import com.example.bookstore.view.BookAdapter
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BooksFragment : Fragment() {
    private lateinit var binding: FragmentBooksBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var rvAdapter : BookAdapter
    private lateinit var apiService : BookApiService
    private lateinit var bookList : List<BookData>
    private val defaultBookImgURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbXCpiYKfm11YUjU715AE4xto0XO6fzBiL8Q&usqp=CAU"
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

        val authors : Array<String> = requestAuthors()
        print(authors)


        //initRecyclerView(bookList)

        binding.addButton.setOnClickListener { addNewBookInfo(authors) }

    }

    private fun requestBooks() {
        var call = apiService.getBooks()
        //aşağıda Liste halinde kitaplar alınıyor
        call.enqueue(object: Callback<List<BookData>> {
            override fun onResponse(call: Call<List<BookData>>, response: Response<List<BookData>>) {
                //binding.progressBarBooks.visibility = View.GONE
                if(response.code() == 200) {
                    var myResponse: List<BookData>? = response.body()
                    print(myResponse)
                    bookList = myResponse!!
                    initRecyclerView(bookList)

                }
            }

            override fun onFailure(call: Call<List<BookData>>, t: Throwable) {
                //binding.progressBarBooks.visibility = View.GONE
                Toast.makeText(requireContext(),"Bir hata oluştu :(", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addNewBookInfo(authors : Array<String>) {
        val inflter = LayoutInflater.from(requireContext())
        val v = inflter.inflate(R.layout.add_item,null)
        /**set view*/
        val bookNameEt = v.findViewById<EditText>(R.id.sbookName)
        val priceEt = v.findViewById<EditText>(R.id.sbookPrice)
        // author edit text silinebilir
        val authorEt = v.findViewById<EditText>(R.id.sbookAuthor)
        val autoText = v.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val textInputLayout = v.findViewById<TextInputLayout>(R.id.textInputLayout)


        // !!!! Burada author'ları get ile alacaksın string resource'lardan değil
        //val authors: Array<String> = resources.getStringArray(R.array.authors)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,authors)
        autoText.setAdapter(arrayAdapter)



        val addDialog = AlertDialog.Builder(requireContext())

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val bookName = bookNameEt.text.toString()
            val priceText = priceEt.text.toString()
            //val author = authorEt.text.toString()

            val author: String = autoText.text.toString()

            if(bookName.isBlank() || priceText.isBlank() || author.isBlank()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
            }
            else {
                val price = priceText.toDouble()

                Toast.makeText(requireContext(),author,Toast.LENGTH_LONG).show()

                // Yeni kitap burada ekleniyor!!!
                //postBookData(bookName,price,author)
                //bookList.add(BookData(bookName,price,author,R.drawable.bookimg))
                //rvAdapter.notifyDataSetChanged()
                //rvAdapter.notifyItemInserted(bookList.size - 1) //???
                //Toast.makeText(requireContext(),"Yeni Kitap Başarı ile Eklendi",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(requireContext(),"İptal",Toast.LENGTH_SHORT).show()

        }

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

    fun postBookData(bookName : String, price : Double, author : String) {

        val dataModel = PostBook(bookName, price, author,defaultBookImgURL)
        val call: Call<Deneme> = apiService.addNewBook(dataModel)
        call!!.enqueue(object : Callback<Deneme> {
            override fun onResponse(call: Call<Deneme>, response: Response<Deneme>) {
                if(response.isSuccessful) {
                    val newlyCreatedBook = response.body()
                    Toast.makeText(requireContext(),"Kitap Eklendi", Toast.LENGTH_SHORT).show()
                    //binding.progressBarBooks.visibility = View.VISIBLE
                    //requestBooks()
                    //updateBooks() gibi bir sey yazilabilir veya lifecycle'dan update edilebilir
                }
                else {
                    Toast.makeText(requireContext(),"Kitap eklenemedi", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Deneme>, t: Throwable) {
                Toast.makeText(requireContext(),"Kitap eklenemedi", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun requestAuthors() : Array<String> {
        val call = apiService.getAuthors()
        //var authors = arrayOf<String>()
        var myResponse = listOf<AuthorsItem>()
        call.enqueue(object : Callback<List<AuthorsItem>> {
            override fun onResponse(
                call: Call<List<AuthorsItem>>,
                response: Response<List<AuthorsItem>>
            ) {
                if(response.isSuccessful) {
                    binding.progressBarBooks.visibility = View.GONE
                    myResponse = response.body()!!
                    print(myResponse!!)
                    //authors =  getAuthorsArray(myResponse!!)
                }
            }

            override fun onFailure(call: Call<List<AuthorsItem>>, t: Throwable) {
                Toast.makeText(requireContext(),"Hata",Toast.LENGTH_SHORT).show()
                binding.progressBarBooks.visibility = View.GONE
            }

        })
        return getAuthorsArray(myResponse)
    }

    fun getAuthorsArray(list : List<AuthorsItem>) : Array<String> {
        val stringArray = arrayListOf<String>()
        for (obj in list) {
            // Nesnenin içindeki belirli özelliğe erişme
            val name = obj.name
            stringArray.add(name)
        }
        return stringArray.toTypedArray()
    }
}