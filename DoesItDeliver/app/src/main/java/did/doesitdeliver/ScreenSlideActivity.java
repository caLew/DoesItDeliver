package did.doesitdeliver;

import android.support.v7.app.ActionBarActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.ActionBarActivity;
        import android.view.Menu;
        import android.view.MenuItem;

        import java.util.ArrayList;

/**
 * Created by saldinger on 9/19/2015.
 */
//ActionBarActivity is from the support.v7 library and should support android 2.1 (API level 7) and higher
//FragmentManager and FragmentStatePagerAdapter are from the support.v4 library and should support android 1.6 (API level 4) and higher
public class ScreenSlideActivity extends ActionBarActivity {

    //NUM_PAGES sets the allowed pages that you can swipe to
    //used in getCount method in ScreenSlidePagerAdapter
    private static final int NUM_PAGES = 2;

    //The pager (ViewPager) widget handles animation and allows swiping
    //horizontally to access previous and next pages
    private ViewPager mPager;


    //The pager adapter (PagerAdapter) provides the pages to the ViewPager widget
    private PagerAdapter mPagerAdapter;

    //ArrayList that holds the different page fragments
    private ArrayList<ScreenSlidePageFragment> feeds = new ArrayList<>();

    //Global instance of the action bar to allow eventual auto-fill-search-box
    ActionBar myActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_screen_slide);

        //Instantiate action bar
        //SupportActionBar called to comply with support.v7
        myActionBar = getSupportActionBar();

        //Set the title for World Wide Feed
        //this is currently the default page 1
        myActionBar.setTitle("Available Tasks");

        //Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        //Pass the pages to the Pager
        mPager.setAdapter(mPagerAdapter);

        //OnPageChangeListener runs onPageSelected(int pageNumber)
        //each time the page changes
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                //If on first page
                if(position == 0){
                    //set the action bar title to World Wide Feed
                    myActionBar.setTitle("Available Tasks");
                }

                //If on second page
                if(position == 1){
                    //set the action bar title to Following Feed
                    myActionBar.setTitle("My Active Tasks");
                }


                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.menu_screen_slide, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_login:
                openLoginPage();
                return true;
            case R.id.action_post_task:
                openPostPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Method to open new compose activity
    //Only called by menu item option selected
    public void openLoginPage(){
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    //Method to open new my profile activity
    //Only called by menu item option selected
    public void openPostPage(){
        startActivity(new Intent(getApplicationContext(), ScreenSlideActivity.class)); // NEED POST PAGE
    }


    //Pager adapter that creates the different discovery feeds
    //ParseQuery handled within the fragment.create method
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ScreenSlidePageFragment getItem(int position) {
            ScreenSlidePageFragment newFeed = ScreenSlidePageFragment.create(position);
            feeds.add(newFeed);
            return newFeed;
        }

        //Sets the amount of pages
        @Override
        public int getCount() {

            //constant declared globally at beginning of class
            return NUM_PAGES;
        }
    }
}

/*

    private static void cloudRequestGeoLalas( ParseObject geoPoint, Parseobject lala)
    {
       //  String lalaObjectId = lala.getObjectId();
        // you must SEND IDs, NOT PARSEUSER OBJECTS to cloud code. Sucks!

         String user;

        // cloudKode = (accepted? "accept" : "ignore");

        HashMap<String, Object> dict = new HashMap<String, Object>();
        dict.put( "latitude", lat);
        dict.put( "longitude", longit);
        dict.put( "geoPoint", lalaObjectId );
        dict.put( "userId", cloudKode );

        ParseCloud.callFunctionInBackground(
                "userNameForLala",
                dict,
                new FunctionCallback<String>() {
                    @Override
                    public void done(String s, ParseException e) {
                        //if no error
                        if (e == null) {
                            coverPhotoViewHolder.author.setText(s);
                        }
                    }
                });

    }
 */
