package com.example.mvvm_artichecture_sample.base

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : BaseViewModel<*>> : Fragment() {

    private var currLayoutName: String? = null
    private var mClassName: String? = null
    protected var currView: View? = null
    protected var activityContext: Context? = null
    protected var isActive: Boolean = false
    protected abstract val viewModel: V

    open var mViewModel: V? = null

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    protected abstract val layoutId: Int

    protected val application: Application
        get() = if (activity == null)
            throw IllegalStateException("Your activity/fragment is not yet attached to " + "Application.")
        else
            activity!!.application


    /**
     * This method called after initialize view
     */
    protected abstract fun setupView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (currView == null) {
            setLayoutView(layoutId, inflater, container, "")
            setupView()
        }
        return currView
    }


    private fun setLayoutView(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?) {
        currView = inflater.inflate(layoutId, container, false)

        currLayoutName = resources.getResourceEntryName(layoutId)
        mClassName = currLayoutName
        activityContext = context
    }

    open fun setLayoutView(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?,
                           className: String) {
        setLayoutView(layoutId, inflater, container)
        mClassName = className
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwnerLiveData.removeObservers(this)
    }

    protected fun back() {
        if (activity != null) {
            activity!!.onBackPressed()
        }
    }
}
