
package dhbw.smartmoderation.group.overview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dhbw.smartmoderation.R;
import dhbw.smartmoderation.data.model.Group;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private static final String TAG = GroupAdapter.class.getSimpleName();

    private Context context;
    private List<Group> groups;
    private OnGroupListener onGroupListener;

    public GroupAdapter(Context context, Collection<Group> groups, OnGroupListener onGroupListener){
        this.context = context;
        this.onGroupListener = onGroupListener;
        this.groups = new ArrayList<>();
        updateGroups(groups);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout layout = new ConstraintLayout(context);
        layout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 160));
        GroupAdapter.GroupViewHolder groupViewHolder = new GroupAdapter.GroupViewHolder(layout, context, onGroupListener,this);
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
        layout.setBackgroundResource(value.resourceId);
        return groupViewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {

        Group group = groups.get(position);
        Log.d(TAG, "RecyclerView: Groups [" + position + "]: Group name: " + group.getName());
        holder.setGroup(group);
    }

    @Override
    public int getItemCount() {

        return groups.size();
    }

    public void updateGroups(Collection<Group> groups){

        if(groups.size() == 0) {

            int length = this.groups.size();
            this.groups.clear();

            for(int i = 0; i < length; i++) {

                notifyItemRemoved(i);
            }
        }

        else {

            this.groups.clear();
            this.groups.addAll(groups);
            notifyDataSetChanged();

        }
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView groupName;
        private ImageView icon;
        private Context context;
        private OnGroupListener onGroupListener;
        private GroupAdapter adapter;
        private ConstraintLayout layout;
        private Group group;

        public GroupViewHolder(ConstraintLayout layout, Context context, OnGroupListener onGroupListener, GroupAdapter adapter) {
            super(layout);
            this.layout = layout;
            layout.setOnClickListener(this);
            this.context = context;
            this.onGroupListener = onGroupListener;
            this.adapter = adapter;

            groupName = new TextView(context);
            groupName.setId(View.generateViewId());
            groupName.setTextSize(15);
            groupName.setTypeface(groupName.getTypeface(), Typeface.BOLD);
            groupName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.layout.addView(groupName);

            ConstraintSet groupNameConstraintSet = new ConstraintSet();
            groupNameConstraintSet.clone(this.layout);
            groupNameConstraintSet.connect(groupName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            groupNameConstraintSet.connect(groupName.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
            groupNameConstraintSet.connect(groupName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            groupNameConstraintSet.applyTo(this.layout);

            icon = new ImageView(context);
            icon.setId(View.generateViewId());
            icon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            icon.setImageResource(R.drawable.ic_circle_notifications);
            icon.setColorFilter(ContextCompat.getColor(this.context,R.color.default_red));
            this.layout.addView(icon);

            ConstraintSet iconConstraintSet = new ConstraintSet();
            iconConstraintSet.clone(this.layout);
            iconConstraintSet.connect(icon.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            iconConstraintSet.connect(icon.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
            iconConstraintSet.connect(icon.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            iconConstraintSet.applyTo(this.layout);
        }

        public void setGroup(Group group){

            this.group = group;

            this.groupName.setText(this.group.getName());

            if (this.group.hasBeenUpdated()){

                icon.setVisibility(View.VISIBLE);

                this.group.updateChecked();
            }

            else {
                icon.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            if (adapter.groups.size() >= position) {

                onGroupListener.onGroupClick(adapter.groups.get(position).getGroupId());
            }
        }
    }

    public interface OnGroupListener{

        void onGroupClick(Long groupId);
    }
}
