package com.example.howlstagram_f16.navigation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howlstagram_f16.R
import com.example.howlstagram_f16.databinding.ItemDetailBinding
import com.example.howlstagram_f16.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text
import kotlinx.android.synthetic.main.fragement_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*


class DetailViewFragment : Fragment(){

    private var _binding: ItemDetailBinding? = null
    private val binding get() = _binding!!

    var firestore : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragement_detail, container, false)
        _binding = ItemDetailBinding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        view.detailviewfragment_recyclerview.adapter = DetailViewRecyclerViewAdater()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class DetailViewRecyclerViewAdater : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTDs : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()
        init{
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFiresotreException ->
                contentDTDs.clear()
                contentUidList.clear()
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTDs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
            }
        }
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_detail, p0, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return contentDTDs.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {

            var viewholder = (p0 as CustomViewHolder).itemView

            //UserId
            viewholder.detailviewitem_profile_textview.text = contentDTDs!![p1].userId

            //Image
            Glide.with(p0.itemView.context).load(contentDTDs!![p1].imageUrl).into(viewholder.detailviewitem_imageview_content)

            //Explain of content
            viewholder.detailviewitem_explatin_textview.text = contentDTDs!![p1].explain

            //likes
            viewholder.detailviewitem_favoritecounter_textview.text = "Likes" + contentDTDs!![p1].favoriteCount

            //ProfileImage
            Glide.with(p0.itemView.context).load(contentDTDs!![p1].imageUrl).into(viewholder.detailviewitem_profile_image)
        }

    }
}