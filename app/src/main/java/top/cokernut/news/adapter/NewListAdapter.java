package top.cokernut.news.adapter;

import android.content.Context;
import android.net.Uri;
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
    private static final int NEW = 1;
    private static final int FOOTER = 2;

    public NewListAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    protected BaseViewHolder createView(ViewGroup parent, int viewType) {
        if (NEW == viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_new_list, parent, false));
        } else {
            return new FooterViewHolder(mInflater.inflate(R.layout.item_new_list_footer, parent, false));
        }
    }

    @Override
    protected void bindView(BaseViewHolder holder, int position) {
        if (mData.get(position) == null) {
            return;
        }
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
        if (mData.get(position) == null) {
            return FOOTER;
        } else {
            return NEW;
        }
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

    static class FooterViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        public TextView txt;

        public FooterViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.tv_txt);
        }
    }
}
