package did.doesitdeliver;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.parse.FunctionCallback;
        import com.parse.GetDataCallback;
        import com.parse.ParseCloud;
        import com.parse.ParseException;
        import com.parse.ParseFile;
        import com.parse.ParseObject;

        import java.util.ArrayList;
        import java.util.HashMap;

/**
 * Created by saldinger on 9/19/2015.
 */
public class myTasksAdapter extends RecyclerView.Adapter<myTasksAdapter.CoverPhotoViewHolder> {

    static ArrayList<ParseObject> lalaObjects = null;
    ParseFile imageFile;
    private Context newDiscoverContext;


    public myTasksAdapter(Context context, Activity activity, ArrayList<ParseObject> objects, int viewT) {
        lalaObjects = objects;
        newDiscoverContext = context;
    }

    @Override
    public int getItemCount() {
        if(lalaObjects != null) return lalaObjects.size();
        else return 0;
    }

    @Override
    public void onBindViewHolder(final CoverPhotoViewHolder coverPhotoViewHolder, final int i) {
        int h = 100;
        int w= 100;
        try {
            //Set height of cardView before image loaded to avoid staggeredGrid rendering glitches
            h = lalaObjects.get(i).getNumber("thumbnailHeight").intValue();
            w = lalaObjects.get(i).getNumber("thumbnailWidth").intValue();

            if (w > 1400 || h > 1400) {
                //  h /= 10;
                //  w /= 10;
            }
            coverPhotoViewHolder.cover.setMinimumHeight(h);

        }catch (Exception e){}

        cloudRequestAuthorForLala(lalaObjects.get(i), coverPhotoViewHolder);
        coverPhotoViewHolder.title.setText(lalaObjects.get(i).getString("title"));
        coverPhotoViewHolder.lalaPoint.setText(lalaObjects.get(i).getObjectId());
        coverPhotoViewHolder.lalaPoint.setVisibility(View.GONE);

        //Set limits to what is allowed to load -- should never be above this
        if(w <= 1600 && h <= 1600) {
            imageFile = lalaObjects.get(i).getParseFile("thumbnail");

            coverPhotoViewHolder.cover.setParseFile(imageFile);
            coverPhotoViewHolder.cover.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                }
            });
        }

        //When user clicks on a cover photo, it will send to the display activity
        //Display activity could be replaced by a fragment
        coverPhotoViewHolder.cover.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
//                Intent intent = new Intent(newDiscoverContext, NewDisplayLala.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ParseObject theCreator = lalaObjects.get(i).getParseUser("createdBy");
//                intent.putExtra("lalaObjectCreator", theCreator.getObjectId());
//                intent.putExtra("lalaObjectPointer", coverPhotoViewHolder.lalaPoint.getText());
//                newDiscoverContext.startActivity(intent);
            }
        });

    }


    @Override
    public CoverPhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_available_tasks, viewGroup, false);

        return new CoverPhotoViewHolder(itemView);
    }

    public static class CoverPhotoViewHolder extends RecyclerView.ViewHolder {
        protected TextView author;
        protected com.parse.ParseImageView cover;
        protected TextView title;
        protected TextView lalaPoint;

        public CoverPhotoViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.authorUserName);
            cover = (com.parse.ParseImageView)  v.findViewById(R.id.coverPhoto);
            title = (TextView) v.findViewById(R.id.lalaTitle);
            lalaPoint = (TextView) v.findViewById(R.id.lalaPointer);


        }
    }

    private static void cloudRequestAuthorForLala( ParseObject lala, final CoverPhotoViewHolder coverPhotoViewHolder )
    {
        String lalaObjectId = lala.getObjectId();

        HashMap<String, Object> dict = new HashMap<>();
        dict.put( "lalaId", lalaObjectId );

        ParseCloud.callFunctionInBackground(
                "userNameForLala",
                dict,
                new FunctionCallback<String>() {
                    @Override
                    public void done(String s, ParseException e) {
                        if(e == null){
                            coverPhotoViewHolder.author.setText(s);
                        }
                    }
                });

    }
}
