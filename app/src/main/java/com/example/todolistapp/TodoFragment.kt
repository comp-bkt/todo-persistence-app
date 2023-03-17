package com.example.todolistapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.util.*

class TodoFragment : Fragment() {
    private var mTodo: Todo? = null
    private lateinit var mEditTextTitle: EditText
    private lateinit var mButtonDate: Button
    private lateinit var mCheckBoxIsComplete: CheckBox
    private lateinit var mPhotoButton: ImageButton
    private lateinit var mPhotoView: ImageView
    private var mPhotoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         Fragment accessing the intent from the hosting Activity as in the following code snippet
         allows for simple code that works.

        UUID todoId = (UUID) getActivity()
                .getIntent().getSerializableExtra(TodoActivity.EXTRA_TODO_ID);

         The disadvantage: TodoFragment is no longer reusable as it is coupled to Activities whoes
         intent has to contain the todoId.

         Solution: store the todoId in the fragment's arguments bundle.
            See the TodoFragment newInstance(UUID todoId) method.

         Then to create a new fragment, the TodoActivity should call TodoFragment.newInstance(UUID)
         and pass in the UUID it retrieves from its extra argument.

        */
        val todoId = requireArguments().getSerializable(ARG_TODO_ID) as UUID?
        mTodo = TodoModel.get(activity)!!.getTodo(todoId)
        mPhotoFile = TodoModel.get(activity)!!.getPhotoFile(mTodo, context)
    }

    override fun onPause() {
        super.onPause()
        TodoModel.get(activity)!!.updateTodo(mTodo)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        mEditTextTitle = view.findViewById<View>(R.id.todo_title) as EditText
        mEditTextTitle.setText(mTodo!!.title)
        mEditTextTitle!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // This line is intentionally left blank
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mTodo!!.title = s.toString()
            }

            override fun afterTextChanged(s: Editable) {
                // This line is intentionally left blank
            }
        })
        mButtonDate = view.findViewById(R.id.todo_date)
        mButtonDate.setText(mTodo!!.date.toString())
        mButtonDate.isEnabled = false
        mCheckBoxIsComplete = view.findViewById(R.id.todo_complete)
        mCheckBoxIsComplete.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("DEBUG **** TodoFragment", "called onCheckedChanged")
            mTodo!!.isComplete = if (isChecked == true) true else false
        }
        mPhotoButton = view.findViewById<View>(R.id.todo_camera) as ImageButton
        mPhotoView = view.findViewById<View>(R.id.todo_photo) as ImageView
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = requireActivity().packageManager
        val canTakePhoto = mPhotoFile != null && packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        mPhotoButton.isEnabled = canTakePhoto
        val todoTakePicture = registerForActivityResult<Uri, Boolean>(
                ActivityResultContracts.TakePicture()
        ) { result: Boolean ->
            if (result) {
                val uri = FileProvider.getUriForFile(this@TodoFragment.activity!!,
                        "com.example.todolistapp.fileprovider",
                        mPhotoFile!!)
                this@TodoFragment.activity!!.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            } else {
                return@registerForActivityResult
            }
        }
        mPhotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                val uri = FileProvider.getUriForFile(activity!!,
                        "com.example.todolistapp.fileprovider",
                        mPhotoFile!!)
                todoTakePicture.launch(uri)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
        mPhotoView = view.findViewById<View>(R.id.todo_photo) as ImageView
        updatePhotoView()
        return view
    }

    private fun checkCameraPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                        this.requireActivity().applicationContext, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    private fun updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile!!.exists()) {
            mPhotoView.setImageDrawable(null)
        } else {
            val bitmap = PictureUtils.getScaledBitmap(mPhotoFile!!.path, activity)
            mPhotoView.setImageBitmap(bitmap)
        }
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private val requestPermissionLauncher = registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
        } else {
            Toast.makeText(
                    this@TodoFragment.activity,
                    "Permission to use camera has not been granted",
                    Toast.LENGTH_SHORT)
                    .show()
        }
    }

    companion object {
        private const val ARG_TODO_ID = "todo_id"
        private const val REQUEST_PHOTO = 2

        /*
    Rather than the calling the constructor directly, Activity(s) should call newInstance
    and pass required parameters that the fragment needs to create its arguments.
     */
        fun newInstance(todoId: UUID?): TodoFragment {
            val args = Bundle()
            args.putSerializable(ARG_TODO_ID, todoId)
            val fragment = TodoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}