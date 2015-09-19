package did.doesitdeliver;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.StaggeredGridLayoutManager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.parse.FindCallback;
        import com.parse.ParseException;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;
        import com.parse.ParseUser;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by saldinger on 9/19/2015.
 */
//Each fragment contains a different query for
//cover photos and identical versions of adapters
//to display those cover photos but avoid Recycler pool
//confusion
public class ScreenSlidePageFragment extends Fragment {

    //Holds the page number for each individual fragment
    public static final String ARG_PAGE = "page";

    //private integer variable to hold page number
    private int mPageNumber;

    final ArrayList<ParseObject> objectsAvailableTasks = new ArrayList<>();
    final ArrayList<ParseObject> objectsMyTasks = new ArrayList<>();
    //Uses NewDiscoverAdapter to display world wide Lalas
    NewDiscoverAdapter adapterWorldWide;
    //FeedAdapter is an identical class to NewDiscoverAdapter but prevents
    //confusion from the recycled components
    FeedAdapter adapterFollowing;
    View rootViewFollowing;
    View rootViewWorldWide;
    RecyclerView recyclerListFollowing;
    RecyclerView recyclerListWorldWide;
    LayoutInflater theInflater;


    //Create method, called by the Page Adapter in ScreenSlideActivity
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();

        //Allows page number to be stored and retrieved
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Stores the page number in private integer variable
        mPageNumber = getArguments().getInt(ARG_PAGE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Global inflater variable given instance of parameter inflater
        theInflater = inflater;

        //Page 1 (position 0)
        //mPageNumber == 0
        //Query for World Wide Feed
        if (mPageNumber == 0) {

            //Changed to allow two separate but identical XML layouts
            //Not sure this is necessary now that it uses separate adapters
            rootViewWorldWide = inflater.inflate(R.layout.activity_new_discover, container, false);

            //Query for all Lalas
            //Ordered by createdAt date
            ParseQuery<ParseObject> coverPhotoQuery = ParseQuery.getQuery("Lala");
            coverPhotoQuery.orderByDescending("createdAt");
            coverPhotoQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //If no error from query
                        try {
                            //Add WorldWide ParseObjects to the global arrayList
                            for (int i = 0; i < objects.size(); i++) {
                                objectsWorldWide.add(objects.get(i));
                            }

                            //Instantiates NewDiscoverAdapter to manage WorldWide Objects
                            adapterWorldWide = new NewDiscoverAdapter(getActivity().getBaseContext(), getActivity(), objectsWorldWide, 1);
                            //Inflates a RecyclerView from within activity_new_discover layout
                            recyclerListWorldWide = (RecyclerView) rootViewWorldWide.findViewById(R.id.recyclerViewNewDiscover);
                            recyclerListWorldWide.setHasFixedSize(true);
                            //Choose staggered grid layout manager
                            StaggeredGridLayoutManager glmWW = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            glmWW.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                            recyclerListWorldWide.setLayoutManager(glmWW);
                            recyclerListWorldWide.setAdapter(adapterWorldWide);

                        } catch (Exception e4) {}
                    }
                }
            });
            return rootViewWorldWide;
        }

        //Page 2 (position 1)
        //mPageNumber == 1
        //Query for Following Feed
        if (mPageNumber == 1) {

            //Changed to allow two separate but identical XML layouts
            //Not sure this is necessary now that it uses separate adapters
            rootViewFollowing = inflater.inflate(R.layout.activity_new_discover2, container, false);

            //Must create actual variable to hold this
            ParseUser thisUser = ParseUser.getCurrentUser();

            //Query Follows first to determine current user's followees
            //Ordered by createdAt date for no logical reason
            ParseQuery<ParseObject> coverPhotoQuery = ParseQuery.getQuery("Follow");
            coverPhotoQuery.whereEqualTo("fromUser", thisUser);
            coverPhotoQuery.orderByDescending("createdAt");
            coverPhotoQuery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //local ArrayList to hold followee ParseUser instances to
                        //use Parse Query .containedIn
                        final ArrayList<ParseObject> followingUsers = new ArrayList<>();

                        //Factory loop to create instances of each ParseUser that current user follows
                        for (int i = 0; i < objects.size(); i++) {
                            ParseUser theUserToFollow = objects.get(i).getParseUser("toUser");
                            String theUserIdToFollow = theUserToFollow.getObjectId();
                            ParseObject followUser = ParseObject.createWithoutData("_User", theUserIdToFollow);
                            followingUsers.add(followUser);
                        }

                        //If the user is following at least 1 person, set the visibility of the "makeSomeFriendsBo" text to GONE
                        if(followingUsers.size() != 0) rootViewFollowing.findViewById(R.id.makeSomeFriendsBo).setVisibility(rootViewFollowing.GONE);

                        //Query for Lalas using followingUsers ArrayList to filter
                        //createdBy currentUser's followees ordered by createdAt date
                        ParseQuery<ParseObject> coverPhotoQuery = ParseQuery.getQuery("Lala");
                        coverPhotoQuery.whereContainedIn("createdBy", followingUsers);
                        coverPhotoQuery.orderByDescending("createdAt");
                        coverPhotoQuery.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null) {
                                    try {
                                        //Clear anything currently stored in ArrayList to avoid duplicates
                                        objectsFollowing.clear();

                                        //Add all returned ParseObjects into global ArrayList
                                        for (int i = 0; i < objects.size(); i++) {
                                            objectsFollowing.add(objects.get(i));
                                        }

                                        //Create FeedAdapter (identical to NewDiscoverAdapter)
                                        //Must use to avoid recycler cache confusion
                                        adapterFollowing = new FeedAdapter(getActivity().getBaseContext(), getActivity(), objectsFollowing, 2);
                                        recyclerListFollowing = (RecyclerView) rootViewFollowing.findViewById(R.id.recyclerViewNewDiscover2);
                                        recyclerListFollowing.setHasFixedSize(true);
                                        StaggeredGridLayoutManager glmF = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                        glmF.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                                        recyclerListFollowing.setLayoutManager(glmF);
                                        recyclerListFollowing.setAdapter(adapterFollowing);

                                    } catch (Exception theE) {}
                                }
                            }
                        });
                    }
                }
            });

            return rootViewFollowing;

        }

        //It should never actually reach this code because we have predetermined page numbers
        if(mPageNumber==0) return rootViewWorldWide;
        if(mPageNumber == 1) return rootViewFollowing;
        else return rootViewWorldWide;
    }
}
