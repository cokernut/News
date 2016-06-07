package top.cokernut.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        if (mData.get(mData.size() - 1) == null) {
            return;
        }
        ViewHolder vh = (ViewHolder) holder;
        vh.txt.setText(mData.get(position).getTitle());
    }

    @Override
    protected int getItemType(int position) {
        if (mData.get(mData.size() - 1) == null) {
            return FOOTER;
        } else {
            return NEW;
        }
    }

    class ViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        public TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.tv_txt);
        }
    }

    class FooterViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        public TextView txt;

        public FooterViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.tv_txt);
        }
    }
}
