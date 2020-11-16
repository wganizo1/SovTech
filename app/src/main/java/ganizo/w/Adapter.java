package ganizo.w;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.HeroViewHolder> {

    private List<SovTechAPI> sovTechList;
    private Context context;
    private SovTechAPI sovTechAPI;
    private static int currentPosition = 0;

    public Adapter(List<SovTechAPI> sovTechList, Context context) {
        this.sovTechList = sovTechList;
        this.context = context;
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout_jokes, parent, false);
        return new HeroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HeroViewHolder holder, final int position) {
        sovTechAPI = sovTechList.get(position);
        holder.textValue.setText(sovTechAPI.getJoke());
        holder.textDateCreated.setText(String.format(context.getString(R.string.created_title), sovTechAPI.getDateCreated().substring(0, 10)));
        Glide.with(context).load(sovTechAPI.getImageUrl()).into(holder.ivIcon);
        holder.linearLayout.setVisibility(View.GONE);

        if (currentPosition == position) {
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.linearLayout.startAnimation(slideDown);
        }
    }

    @Override
    public int getItemCount() {
        return sovTechList.size();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder {
        TextView textValue;
        ImageView ivIcon;
        TextView textDateCreated;
        LinearLayout linearLayout;

        HeroViewHolder(View itemView) {
            super(itemView);
            textDateCreated = itemView.findViewById(R.id.textDateCreated);
            linearLayout = itemView.findViewById(R.id.llJoke);
            textValue = itemView.findViewById(R.id.textValue);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}

