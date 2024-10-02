package com.example.todolist

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var todoEditText: EditText
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var adapter: TodoAdapter
    private lateinit var saveButton: Button
    private var todoList: MutableList<TodoItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoEditText = findViewById(R.id.todoEditText)
        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        saveButton = findViewById(R.id.saveButton)

        adapter = TodoAdapter(todoList) { position -> showEditDeleteDialog(position) }
        todoRecyclerView.adapter = adapter
        todoRecyclerView.layoutManager = LinearLayoutManager(this)

        todoEditText.setOnEditorActionListener { _, _, _ ->
            val task = todoEditText.text.toString()
            if (task.isNotEmpty()) {
                todoList.add(TodoItem(task, false))
                adapter.notifyItemInserted(todoList.size - 1)
                todoEditText.text.clear()
            }
            true
        }
        saveButton.setOnClickListener {
            val task = todoEditText.text.toString()
            if (task.isNotEmpty()) {
                todoList.add(TodoItem(task, false))
                adapter.notifyItemInserted(todoList.size - 1)
                todoEditText.text.clear() // Clear input field after adding
            }
        }
    }

    private fun showEditDeleteDialog(position: Int) {
        if (position >= 0 && position < todoList.size) {
            val options = arrayOf("Edit", "Delete")
            AlertDialog.Builder(this)
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> showEditDialog(position)
                        1 -> {
                            todoList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, todoList.size)
                        }
                    }
                }
                .show()
        } else {

            Toast.makeText(this, "Invalid item selected", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showEditDialog(position: Int) {
        val editText = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(todoList[position].text)
        }
        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                todoList[position].text = editText.text.toString()
                adapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

data class TodoItem(var text: String, var isChecked: Boolean)

class TodoAdapter(
    private val todoList: MutableList<TodoItem>,
    private val onItemDoubleClick: (Int) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = todoList[position]
        holder.bind(todoItem)

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime = 0L

            override fun onClick(v: View?) {
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < 300) {
                    onItemDoubleClick(position)
                }
                lastClickTime = clickTime
            }
        })
    }

    override fun getItemCount(): Int = todoList.size

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.todoCheckBox)
        private val textView: TextView = itemView.findViewById(R.id.todoTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.todoImageView)

        fun bind(todoItem: TodoItem) {
            checkBox.isChecked = todoItem.isChecked
            textView.text = todoItem.text
        }
    }
}
