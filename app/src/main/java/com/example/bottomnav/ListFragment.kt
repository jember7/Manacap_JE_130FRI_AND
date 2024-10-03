package com.example.bottomnav

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {

    private lateinit var todoEditText: EditText
    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var adapter: TodoAdapter
    private var todoList: MutableList<TodoItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoEditText = view.findViewById(R.id.todoEditText)
        todoRecyclerView = view.findViewById(R.id.todoRecyclerView)

        adapter = TodoAdapter(todoList) { position -> showEditDeleteDialog(position) }
        todoRecyclerView.adapter = adapter
        todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        todoEditText.setOnEditorActionListener { _, _, _ ->
            val task = todoEditText.text.toString()
            if (task.isNotEmpty()) {
                todoList.add(TodoItem(task, false))
                adapter.notifyItemInserted(todoList.size - 1)
                todoEditText.text.clear()
            }
            true
        }
        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
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
            AlertDialog.Builder(requireContext())
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
            Toast.makeText(requireContext(), "Invalid item selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditDialog(position: Int) {
        val editText = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(todoList[position].text)
        }
        AlertDialog.Builder(requireContext())
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