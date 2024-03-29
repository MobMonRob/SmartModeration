package dhbw.smartmoderation.group.personInfo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import org.briarproject.bramble.api.contact.Contact;
import java.util.ArrayList;
import java.util.Collection;

import dhbw.smartmoderation.R;


public class ContactAdapter extends  RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private ArrayList<Contact> contactList;
    private Contact selectedContact;
    private int index = -1;

    public ContactAdapter(Context context, Collection<Contact> contactList) {
        this.context = context;
        this.contactList = new ArrayList<>();
        updateContacts(contactList);
    }


    public void updateContacts(Collection<Contact> contactList) {
        this.contactList.clear();
        this.contactList.addAll(contactList);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 150));
        ContactViewHolder contactViewHolder = new ContactViewHolder(constraintLayout, context, this);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        Contact contact = this.contactList.get(position);
        String name = contact.getAuthor().getName();
        holder.setContact(contact);
        holder.getTextView().setText(name);

        holder.itemView.setOnClickListener( v -> {

            this.selectedContact = contact;
            index = position;
            notifyDataSetChanged();
        });

        if(index == position) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_grey, null));
        }

        else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.default_color, null));
        }
    }

    @Override
    public int getItemCount() {
        return this.contactList.size();
    }

    public Contact getSelectedContact() {
        return this.selectedContact;
    }

    public Context getContext() {
        return this.context;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView contactName;
        private Contact contact;

        public ContactViewHolder(ConstraintLayout constraintLayout, Context context, ContactAdapter contactAdapter) {

            super(constraintLayout);
            contactName = new TextView(context);
            contactName.setId(View.generateViewId());
            contactName.setTypeface(contactName.getTypeface(), Typeface.BOLD);
            constraintLayout.addView(contactName);

            ConstraintSet textViewConstraintSet = new ConstraintSet();
            textViewConstraintSet.clone(constraintLayout);
            textViewConstraintSet.connect(contactName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 12);
            textViewConstraintSet.connect(contactName.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 50);
            textViewConstraintSet.connect(contactName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 12);
            textViewConstraintSet.applyTo(constraintLayout);
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        public TextView getTextView() {
            return this.contactName;
        }

    }
}
