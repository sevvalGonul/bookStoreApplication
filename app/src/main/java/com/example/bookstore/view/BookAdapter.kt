package com.example.bookstore.view

import com.example.bookstore.model.BookData

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R


class BookAdapter(val c:Context,val bookList:List<BookData>):RecyclerView.Adapter<BookAdapter.BookViewHolder>()
{

    inner class BookViewHolder(val v:View):RecyclerView.ViewHolder(v){
        var bookTitle: TextView
        var price : TextView
        var authorName : TextView
        var bookImage : ImageView

        init {
            bookTitle = v.findViewById(R.id.book_title)
            price = v.findViewById<TextView>(R.id.price)
            authorName = v.findViewById(R.id.author_name)
            bookImage = v.findViewById(R.id.book_image)
            //bookImage.setOnClickListener { popupMenus(it) }
        }

        /*private fun popupMenus(v:View) {
            val position = bookList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)
                        val name = v.findViewById<EditText>(R.id.userName)
                        val number = v.findViewById<EditText>(R.id.userNo)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                    dialog,_->
                                position.userName = name.text.toString()
                                position.userMb = number.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(c,"User Information is Edited",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()

                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c,"Deleted this Information",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.book_item,parent,false)
        return BookViewHolder(v)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook: BookData = bookList[position]
        //holder.bookImage.setImageResource(currentBook.bookImage)
        holder.authorName.text = currentBook.authorName
        holder.bookTitle.text = currentBook.bookTitle
        holder.price.text = currentBook.price.toString()

        Glide.with(holder.bookImage.context).load(currentBook.bookImage).into(holder.bookImage)

        holder.itemView.setOnClickListener{
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_booksFragment_to_detailFragment)
        }
   }

    override fun getItemCount(): Int {
        return  bookList.size
    }
}