package top.cokernut.news.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import top.cokernut.news.R;
import top.cokernut.news.base.BaseRecyclerAdapter;
import top.cokernut.news.model.NewModel;

/**
 * Created by Cokernut on 2016/6/7.
 */
public class NewListAdapter extends BaseRecyclerAdapter<NewModel, BaseRecyclerAdapter.BaseViewHolder> {

    public NewListAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    protected BaseViewHolder createView(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_new_list, parent, false));
    }

    @Override
    protected void bindView(BaseViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            vh.title.setText(mData.get(position).getTitle());
            vh.time.setText(mData.get(position).getCtime());
            if (!TextUtils.isEmpty(mData.get(position).getPicUrl())) {
                vh.img.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher);
                Glide.with(mContext)
                        .load(mData.get(position).getPicUrl())
                        .apply(options)
                        .into(vh.img);
            } else {
                vh.img.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected int getItemType(int position) {
        return super.getItemType(position);
    }

    static class ViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        public TextView title;
        public TextView time;
        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
